package com.xuzhiyong.comego.module.datacenter;

import android.database.Cursor;

import com.duowan.fw.kvo.databinding.JObservableDouble;
import com.duowan.fw.kvo.databinding.JObservableFloat;
import com.duowan.fw.kvo.databinding.JObservableInt;
import com.duowan.fw.kvo.databinding.JObservableLong;
import com.duowan.fw.kvo.databinding.JObservableShort;
import com.duowan.fw.kvo.databinding.JObservableString;

import java.util.HashMap;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public enum CursorFieldType {
	BLOB("BLOB", byte[].class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getBlob(columnIndex);
		}
	},
	FLOAT("REAL", Float.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getFloat(columnIndex);
		}
	},
	PRIMITIVE_FLOAT("REAL", Float.TYPE) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getFloat(columnIndex);
		}
	},
	OBSERVABLE_FLOAT("REAL", JObservableFloat.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getFloat(columnIndex);
		}
	},
	DOUBLE("REAL", Double.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getDouble(columnIndex);
		}
	},
	PRIMITIVE_DOUBLE("REAL", Double.TYPE) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getDouble(columnIndex);
		}
	},
	OBSERVABLE_DOUBLE("REAL", JObservableDouble.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getDouble(columnIndex);
		}
	},
	INT("INT", Integer.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getInt(columnIndex);
		}
	},
	PRIMITIVE_INT("INT", Integer.TYPE) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getInt(columnIndex);
		}
	},
	OBSERVABLE_INT("INT", JObservableInt.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getInt(columnIndex);
		}
	},
	LONG("INT", Long.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getLong(columnIndex);
		}
	},
	PRIMITIVE_LONG("INT", Long.TYPE) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getLong(columnIndex);
		}
	},
	OBSERVABLE_LONG("INT", JObservableLong.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getLong(columnIndex);
		}
	},
	STRING("TEXT", String.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getString(columnIndex);
		}
	},
	OBSERVABLE_STRING("TEXT", JObservableString.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getString(columnIndex);
		}
	},
	SHORT("INT", Short.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getShort(columnIndex);
		}
	},
	PRIMITIVE_SHORT("INT", Short.TYPE) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getShort(columnIndex);
		}
	},
	OBSERVABLE_SHORT("INT", JObservableShort.class) {
		@Override
		public Object getObject(Cursor cursor, int columnIndex) {
			return cursor.getShort(columnIndex);
		}
	};

	public abstract Object getObject(Cursor cursor, int columnIndex);

	private final Class<?> cls;
	private String sqlInsertName;

	// cache all cursor field types in container
	private static HashMap<Class<?>, CursorFieldType> allCfs = new HashMap<>();

	static {
		for(CursorFieldType type : values()) {
			allCfs.put(type.getCls(), type);
		}
	}

	CursorFieldType(String sqlInsertName, Class<?> cls) {
		this.cls = cls;
		this.sqlInsertName = sqlInsertName;
	}

	public Class<?> getCls() {
		return this.cls;
	}

	public String getSqlInsertName() {
		return this.sqlInsertName;
	}

	public static CursorFieldType getCursorType(Class<?> cls) {
		return allCfs.get(cls);
	}
}
