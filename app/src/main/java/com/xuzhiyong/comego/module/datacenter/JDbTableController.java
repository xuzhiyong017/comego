package com.xuzhiyong.comego.module.datacenter;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.kvo.databinding.JDatabindingObservable;
import com.duowan.fw.util.JConstCache.CacheResult;
import com.duowan.fw.util.JFP;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.KvoAnnotationFlags;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class JDbTableController<T extends KvoSource> {

	public String table;
	
	public String dropsql, createsql, savesql;
	
	public Class<T> clazz;
	
	public int cacheId;
	
	public long flag;
	
	public ArrayList<CursorField> fields = new ArrayList<>();
	public ArrayList<CursorField> primaryKeys = new ArrayList<>();
	public String[] primaryKeyNames;
	
	public JDbTableController(Class<T> _clazz, int cacheId){
		this(_clazz.getSimpleName().substring(1), _clazz, cacheId);
	}
	
	public JDbTableController(String _table, Class<T> _clazz, int cacheId){
		setupTable(_table, _clazz, cacheId);
	}
	
	public void setupTable(String _table, Class<T> _clazz, int cacheId){
		this.table = _table;
		this.clazz = _clazz;
		this.cacheId = cacheId;
		
		setupFields();
		
		if (fields.size() > 0) {
			dropsql = String.format("DROP TABLE IF EXISTS %s", table);
			
			savesql = buildInsertOrReplaceSql();
			
			createsql = buildCreateSql();
		}
	}
	
	private void setupFields() {
		for (Field field : clazz.getDeclaredFields()) {
			KvoAnnotation annotation = field.getAnnotation(KvoAnnotation.class);
			if (annotation != null && KvoAnnotationFlags.isSaveDb(annotation.flag())) {
				
				CursorFieldType type = CursorFieldType.getCursorType(field.getType());
				if(type == null) {
					JLog.error(this, "unknown cursor type field : " + field);
				}

				CursorField cursorField = null;

				if(JDatabindingObservable.class.isAssignableFrom(field.getType())) {
					cursorField = new DatabindCursorField();
				} else {
					cursorField = new DefaultCursorField();
				}

				int annotationFlag = annotation.flag();

				cursorField.field = field;
				cursorField.fieldName = field.getName();
				cursorField.type = type;
				cursorField.primaryKey = KvoAnnotationFlags.isDbPrimaryKey(annotationFlag);
				cursorField.columnIndex = annotation.order();
				
				fields.add(cursorField);
				
				if(cursorField.primaryKey) {
					primaryKeys.add(cursorField);
				}
			}
		}
		
		Collections.sort(fields);
		Collections.sort(primaryKeys);
		
		primaryKeyNames = new String[primaryKeys.size()];
		for(int i = 0; i < primaryKeys.size(); i++) {
			primaryKeyNames[i] = primaryKeys.get(i).fieldName;
		}
	}
	
    private String buildInsertOrReplaceSql(){
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("INSERT OR REPLACE INTO ").append(table)
    		.append(" (").append(fields.get(0).fieldName);
    	
    	for(int i = 1; i < fields.size(); ++i){
    		sb.append(",").append(fields.get(i).fieldName);
    	}
    	
    	sb.append(") VALUES ");
    	sb.append(DataCenterHelper.sSQL_VALUES[fields.size()]);
    	
    	return sb.toString();
    }
    
    private String buildCreateSql() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("CREATE TABLE ").append(table).append(" (");
    	
    	for(int i = 0; i < fields.size(); ++i){
    		CursorField cf = fields.get(i);
    		sb.append(cf.fieldName).append(" ").append(cf.type.getSqlInsertName());
    		if(i != fields.size() - 1) {
    			sb.append(", ");
    		}
    	}
    	
    	if(primaryKeys.isEmpty()) {
    		sb.append(")");
    	} else {
    		sb.append(", PRIMARY KEY(");
    		
    		for(int i = 0; i < primaryKeys.size(); i++) {
    			CursorField cf = primaryKeys.get(i);
    			sb.append(cf.fieldName);
    			if(i != primaryKeys.size() - 1) {
    				sb.append(", ");
    			}
    		}
    		
    		sb.append("))");
    	}
    	
    	return sb.toString();
    }
    
	public void create(JDb db) {
		db.execSQLWithReadableDatabase(dropsql);
		db.execSQLWithReadableDatabase(createsql);
	}

	public CacheResult queryCache(JDb db, Object key, boolean autoCreate) {
		return db.cache(cacheId, key, autoCreate);
	}

	/**
	 * 从缓存中读取对象, 如果缓存没有从数据库读取对象<p>
	 * 如果有多个primaryKey, 要按照数据库中主键的顺序
	 */
	public T queryRow(JDb db, Object...keys){
		return query(db, keys).valueOf(clazz);
	}
    
	/**
	 * 从缓存里面读取数据，没有从数据库读取数据，并把协议数据映射回数据库<p>
	 * 如果有多个primaryKey, 要按照数据库中主键的顺序
	 * @param keys 记得带上key！！
	 */
	public T queryTarget(JDb db, Object proto, Object...keys) {
		CacheResult result = query(db, keys);

		if(proto != null) {
			fillFromProto(db, result, proto);
		}

		return result.valueOf(clazz);
	}

	//从DB里读数据，如果标识位表明已经读过，就不再读
	public CacheResult query(JDb db, Object...keys) {
		Object cacheKey = key(keys);
		CacheResult result = queryCache(db, cacheKey, true);
		
		if (!result.isFlag(JDb.sCacheFlag_SelectDb)) {
			String[] strKeys = new String[keys.length];
			for(int i = 0; i < keys.length; i++) {
				strKeys[i] = keys[i].toString();
			}

			boolean hit = internalSelect(db, result, strKeys);
		}
		
		return result;
	}

	private boolean internalSelect(JDb db, CacheResult result, String...values) {
		boolean haveHit = false;
		
		QueryRowsParams params = new QueryRowsParams();
		params.keys = primaryKeyNames;
		params.values = values;
		
		Cursor cursor = params.getQueryRowsCursor(db, table);

        try {
            if (null != cursor && cursor.moveToNext()) {
                haveHit = true;
                fillFromCursor(db, result, cursor);
            } else {
                result.setFlag(JDb.sCacheFlag_SelectDb);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

		return haveHit;
	}

	/**
	 * 无cache的表查询接口
	 * @param <T>
	 */
	public interface NoCacheTableQueryListener<T> {
		public T newObject(Cursor cursor);
	}

	/**
	 * 适用于 <b>无cache的表查询</b>
	 * 如果有多个primaryKey，要按照数据库中主键的顺序
	 */
	@Nullable
	public T internalSelectWithoutCache(JDb db, NoCacheTableQueryListener<T> qi, Object...values) {
		QueryRowsParams params = new QueryRowsParams();
		params.keys = primaryKeyNames;
		if(values != null) {
			String[] queryValues = new String[values.length];

			for(int i = 0; i < values.length; i++) {
				queryValues[i] = values[i].toString();
			}

			params.values = queryValues;
		}

		Cursor cursor = params.getQueryRowsCursor(db, table);

		T dst = null;

		try {
			if (null != cursor && cursor.moveToNext()) {
				dst = qi.newObject(cursor);
				fromCursor(db, dst, cursor);
			}
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}

		return dst;
	}

	/**
	 * 适用于 <b>无cache的表查询</b>
	 */
	public List<T> queryRowsWithoutCache(JDb db, QueryRowsParams params, NoCacheTableQueryListener<T> qi) {
		long ts = System.currentTimeMillis();

		JLog.debug(this, "[JDB] queryWithoutCache Begin " + " in table: " + table);

		Cursor cursor = params.getQueryRowsCursor(db, table);

		List<T> dataList = new ArrayList<T>(null != cursor ? cursor.getCount() : 0);

		if(cursor == null) {
			JLog.error(this, "query rows get cursor null");
			return dataList;
		}

		try {
			while(cursor.moveToNext()) {
				T dst = qi.newObject(cursor);

				fromCursor(db, dst, cursor);

				dataList.add(dst);
			}
		} finally {
			cursor.close();
		}

		JLog.debug(this, "[JDB] queryWithoutCache End " + " in table: " + table
				+ " with " + dataList.size() + " take " + (System.currentTimeMillis()
				- ts) + " millis");

		return dataList;
	}

	public List<T> queryRows(JDb db, QueryRowsParams params) {
		
		long ts = System.currentTimeMillis();
		
		JLog.debug(this, "[JDB] SelectRows Begin " + " in table: " + table);
		
		Cursor cursor = params.getQueryRowsCursor(db, table);
		
		if(cursor == null) {
			JLog.error(this, "query rows get cursor null");
			return new ArrayList<T>();
		}
		
		JLog.debug(this, "[JDB] queryLimit Query " + " in table: " + table + " take " + (System.currentTimeMillis() - ts) + " millis");
		
		List<T> datas = fillListFromCursor(db, cursor);
		
		JLog.debug(this, "[JDB] queryLimit End " + " in table: " + table
				+ " with " + datas.size() + " take " + (System.currentTimeMillis()
				- ts) + " millis");
		
    	return datas;
	}
	
	public List<T> queryRows(JDb db, String[] keys, String[] values) {
		QueryRowsParams params = new QueryRowsParams();
		params.keys = keys;
		params.values = values;
		
		return queryRows(db, params);
	}
	
	private List<T> fillListFromCursor(JDb db, Cursor cursor) {
		if (null == cursor) {
            return new ArrayList<T>(0);
        }
		
        List<T> datas = new ArrayList<T>(cursor.getCount());
        
		try {
    		while(cursor.moveToNext()) {
    			Object[] cursorKeys = new Object[primaryKeys.size()];
    			for(int i = 0; i < cursorKeys.length; i++) {
    				CursorField cf = primaryKeys.get(i);
    				cursorKeys[i] = cf.type.getObject(cursor, cf.columnIndex);
    			}
    			CacheResult result = db.cache(cacheId, key(cursorKeys));
    			
    			fillFromCursor(db, result, cursor);
    			
    			datas.add(result.valueOf(clazz));
    		}
    	} finally {
    		cursor.close();
    	}
		
		return datas;
	}
	
	public void fillFromCursor(JDb db, CacheResult result, Cursor cursor){
		if (result.isNew || !result.isFlag(JDb.sCacheFlag_LoadDb)) {
			fromCursor(db, result.valueOf(clazz), cursor);
			
			result.setFlag(JDb.sCacheFlag_SelectDb);
			
			result.setFlag(JDb.sCacheFlag_LoadDb);
		}
	}
	
	// 从数据库读取结构体
	public void fromCursor(JDb db, T dst, Cursor cursor) {
		for (int i = 0; i < fields.size(); i++) {
			CursorField cf = fields.get(i);
			dst.setValue(cf.fieldName, cf.type.getObject(cursor, cf.columnIndex));
		}
	}

	public void fillFromProto(JDb db, CacheResult result, Object proto) {
		fromProto(db, result.valueOf(clazz), proto);
		
		result.setFlag(JDb.sCacheFlag_LoadNet);

		internalSave(db, result);
	}
	
	public abstract void fromProto(JDb db, T info, Object proto);
	
	public abstract Object key(Object...cursorKeys);

	// 存储到数据库

	/**
	 * fixme:当直接存储数据库字段时，没有置标志位：
	 * result.setFlag(JDb.sCacheFlag_SelectDb);
	 * result.setFlag(JDb.sCacheFlag_LoadDb);
	 *
	 * @param db
	 * @param t
	 */
	public void save(final JDb db, T t){
		try {
			final Object[] args = new Object[fields.size()];
			for (int i = 0; i < fields.size(); i++) {
				args[i] = fields.get(i).get(t);
			}
			JDb.postSafe(new Runnable() {

				@Override
				public void run() {
					db.save(savesql, args);
				}
			});
		} catch (IllegalAccessException e) {
			JLog.error(this, "save failed error : " + e.toString());
		} catch (IllegalArgumentException e) {
			JLog.error(this, "save failed error : " + e.toString());
		} catch (NullPointerException e) {
			JLog.error(this, "save failed error : " + e.toString());
		}
	}
	
	private void internalSave(JDb db, CacheResult result){
		save(db, result.valueOf(clazz));
		
		result.setFlag(JDb.sCacheFlag_SelectDb);
		
		result.setFlag(JDb.sCacheFlag_LoadDb);
	}

	public void saveRows(final JDb db, final Collection<T> datas) {
		JDb.postSafe(new Runnable() {

			@Override
			public void run() {
				db.callTransaction(new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						for (T t : datas) {
							save(db, t);
						}
						return null;
					}
				});
			}
		});
	}
	
	public void delete(final JDb db, T t) {
		Object[] values = new Object[primaryKeys.size()];
		try {
			for (int i = 0; i < primaryKeys.size(); i++) {
				values[i] = primaryKeys.get(i).get(t);
			}
			deleteRows(db, primaryKeyNames, values);
		} catch (IllegalArgumentException e) {
			JLog.error(this, "delete failed : " + t + " error : " + e);
		} catch (IllegalAccessException e) {
			JLog.error(this, "delete failed : " + t + " error : " + e);
		}
	}
	
	/**
	 * keys 或者 values 传空时 为删除全部
	 * @param db
	 * @param keys
	 * @param values
	 */
	public void deleteRows(final JDb db, final String[] keys, final Object[] values) {
		JDb.postSafe(new Runnable() {

			@Override
			public void run() {
				if (JFP.empty(keys) || JFP.empty(values)) {
					db.deleteAll(table);
				} else {
					db.delete(table, keys, values);
				}
			}
		});
	}
	
	public static class QueryRowsParams {

        public enum QueryOrder {
            Asc("ASC"), Desc("DESC");

            private final String orderStr;

            QueryOrder(String str) {
                orderStr = str;
            }

            public String orderStr() {
                return orderStr;
            }
        }

		public static final QueryOrder[] OrderDesc = new QueryOrder[]{QueryOrder.Desc};
		public static final QueryOrder[] OrderAsc = new QueryOrder[]{QueryOrder.Asc};

        public static QueryRowsParams makeParams(int limit, QueryOrder order, String field) {
            QueryRowsParams params = new QueryRowsParams();
            if (order == QueryOrder.Asc) {
                params.orders = QueryRowsParams.OrderAsc;
            } else {
                params.orders = QueryRowsParams.OrderDesc;
            }
            params.limit = limit;
            params.orderByFields = new String[]{field};
            return params;
        }

		/**
		 * @param db
		 * @param keys
		 * @param values
		 * @param orderByFields  	按照某个字段排序，比如 "uid", "version"，如果不限，就填空
		 * @param orders		0 是 asc, 1 是 desc，配合orderBy使用
		 * @param limit		不限就填 -1
		 */
		public String[] keys;
		public String[] values;
        public String[] keyValueRelations;
		public String[] orderByFields;
		public QueryOrder[] orders;
		public int limit;
		
		public String buildQueryRowsSql(String table) {
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * FROM ").append(table);
			
			if(!JFP.empty(keys) && !JFP.empty(values)) {
				sb.append(" WHERE");

                if(JFP.empty(keyValueRelations)) {
                    for(int i = 0; i < keys.length; i++) {
                        sb.append(" ").append(keys[i]).append(" = ?");
                        if(i != keys.length - 1) {
                            sb.append(" AND");
                        }
                    }
                } else {
                    for(int i = 0; i < keys.length; i++) {
                        sb.append(" ").append(keys[i]).append(" ").append(keyValueRelations[i]).append(" ?");
                        if(i != keys.length - 1) {
                            sb.append(" AND");
                        }
                    }
                }
			}

            //默认是一定要带上ASC 或者 DESC的，不能用Default的
			if(!JFP.empty(orderByFields) && !JFP.empty(orders)
                    && orderByFields.length == orders.length) {
				sb.append(" ORDER BY ");

                for(int i = 0; i < orderByFields.length; i++) {
                    sb.append(orderByFields[i]).append(" ")
                            .append(orders[i]);

                    if(i != orderByFields.length - 1) {
                        sb.append(", ");
                    }
                }
			}
			
			if(limit != 0) {
				sb.append(" LIMIT ?");
			}
			
			return sb.toString();
		}
		
		public String[] buildQueryRowsArgs() {
			ArrayList<String> args = new ArrayList<String>();
			
			if(!JFP.empty(values)) {
				for(String value : values) {
					args.add(value);
				}
			}
			
			if(limit != 0) {
				args.add(String.valueOf(limit));
			}
			
			return args.isEmpty() ? null : args.toArray(new String[args.size()]);
		}
		
		public Cursor getQueryRowsCursor(JDb db, String table) {
			return db.rawSelect(buildQueryRowsSql(table), buildQueryRowsArgs());
		}
	}
}
