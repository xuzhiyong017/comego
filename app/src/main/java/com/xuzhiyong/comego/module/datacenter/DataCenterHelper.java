package com.xuzhiyong.comego.module.datacenter;

import com.duowan.fw.util.JStringUtils;
import com.google.gson.Gson;
import com.xuzhiyong.comego.module.DModule;

public class DataCenterHelper {

	public static Gson gson = new Gson();

	public static final int DataCenterVersion = 1;

	public static String buildUserDbName(long uid) {
		return JStringUtils.combineStr("ada_datacenter_v(", DataCenterVersion, ")", uid, ".db");
	}

	public static String buildAppDbName() {
		return JStringUtils.combineStr("ada_datacenter_v(", DataCenterVersion, ")", ".db");
	}

	/// current user db
	public static JDb userDb() {
		return DModule.ModuleDataCenter.cast(DataCenterInterface.class).userDb();
	}

	/// current appDb
	public static JDb appDb() {
		return DModule.ModuleDataCenter.cast(DataCenterInterface.class).appDb();
	}

	// 封装基本的数据库操作
	public static String[] sSQL_VALUES = null;

	static {
		sSQL_VALUES = new String[63];
		StringBuilder sb = new StringBuilder();
		sb.append("(?");
		for (int i = 1; i < sSQL_VALUES.length; ++i) {
			sSQL_VALUES[i] = sb.toString() + ")";
			sb.append(",?");
		}
	}

	public static String buildSql_WhereEqual(String[] keys) {
		StringBuilder sb = new StringBuilder();
		sb.append(" WHERE ");
		sb.append(keys[0]);
		sb.append("=?");

		if (keys.length > 1) {
			for (int i = 1; i < keys.length; ++i) {
				sb.append(" AND ");
				sb.append(keys[i]);
				sb.append("=?");
			}
		}
		return sb.toString();
	}

	public static String buildSql_insertOrReplace(String tableName, String[] keys) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT OR REPLACE INTO ");
		sb.append(tableName);
		sb.append(" (" + keys[0]);
		for (int i = 1; i < keys.length; ++i) {
			sb.append(",");
			sb.append(keys[i]);
		}
		sb.append(") VALUES ");
		sb.append(sSQL_VALUES[keys.length]);
		return sb.toString();
	}
}
