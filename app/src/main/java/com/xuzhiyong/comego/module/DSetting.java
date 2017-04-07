package com.xuzhiyong.comego.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.root.BaseContext;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hydra on 2015/10/15.
 *
 */
public class DSetting extends Kvo.KvoSource {

	private static HashMap<String, SpField> settingFieldMap = new HashMap<>();

	static {
		Field[] fs = DSetting.class.getDeclaredFields();
		for(Field f : fs) {
			SpField sf = new SpField(f);
			if(sf.type != null) {
				//默认是都有KvoAnnotation的
				settingFieldMap.put(f.getAnnotation(KvoAnnotation.class).name(), sf);
			}
		}
	}

	public static final int DSettingVersion = 1603291612;

	//全局设置
	private static DSetting sGlobalSetting = new DSetting(0L);

	//uid是0，默认是全局设置选项
	private static DSetting sCurrentUserSetting = new DSetting(0L);

	public static synchronized void loadUserSetting(long uid) {
		//不每次重建DSetting，而是用同一个对象去重新load From SharedPreferences
		//这样绑定Setting的值以后就不用监听user change了
		if(sCurrentUserSetting.mUid != uid) {
			sCurrentUserSetting.reInit(uid);
		}
	}

	public static DSetting userSetting() {
		return sCurrentUserSetting;
	}

	public static DSetting globalSetting() {
		return sGlobalSetting;
	}

	/**
	 * 如果key带有后缀，就没有办法使用Kvo通知
	 * @param key
	 * @param keySuffix
	 * @param value
	 * @param <T>
	 */
	public static <T> void setSettingValue(String key, String keySuffix, T value) {
		sCurrentUserSetting.setValue(key, keySuffix, value);
	}

	public static <T> T getSettingValue(String key, String keySuffix, T defaultValue) {
		return (T)sCurrentUserSetting.getValue(key, keySuffix, defaultValue);
	}

	public static <T> void setSettingValue(String key, T value) {
		sCurrentUserSetting.setValue(key, null, value);
	}

	public static <T> T getSettingValue(String key, T defaultValue) {
		return (T)sCurrentUserSetting.getValue(key, null, defaultValue);
	}

	private static class SpField {
		Field field;
		SpFieldType type;

		public SpField(Field f) {
			field = f;
			type = SpFieldType.getSpFieldType(f);
		}
	}

	private enum SpFieldType {

		BOOLEAN(Boolean.class) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putBoolean(key, (Boolean) value).apply();
			}
		},
		PRIMITIVE_BOOLEAN(Boolean.TYPE) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putBoolean(key, (Boolean) value).apply();
			}
		},
		FLOAT(Float.class) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putFloat(key, (Float) value).apply();
			}
		},
		PRIMITIVE_FLOAT(Float.TYPE) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putFloat(key, (Float) value).apply();
			}
		},
		INT(Integer.class) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putInt(key, (Integer) value).apply();
			}
		},
		PRIMITIVE_INT(Integer.TYPE) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putInt(key, (Integer) value).apply();
			}
		},
		LONG(Long.class) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putLong(key, (Long) value).apply();
			}
		},
		PRIMITIVE_LONG(Long.TYPE) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putLong(key, (Long) value).apply();
			}
		},
		STRING_SET(Set.class) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				try {
					sp.edit().putStringSet(key, (Set<String>) value).apply();
				} catch (Exception e) {
					JLog.error(this, "mSharedPreferences get String Set Error : " + e);
				}
			}
		},
		STRING(String.class) {
			@Override
			public void putObject(SharedPreferences sp, String key, Object value) {
				sp.edit().putString(key, (String) value).apply();
			}
		};

		private static HashMap<Class<?>, SpFieldType> allCfs = new HashMap<Class<?>, SpFieldType>();

		static {
			for(SpFieldType type : values()) {
				allCfs.put(type.getCls(), type);
			}
		}

		public abstract void putObject(SharedPreferences sp, String key, Object value);

		private Class<?> clazz;

		public Class<?> getCls() {
			return clazz;
		}

		SpFieldType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public static SpFieldType getSpFieldType(Field field) {
			KvoAnnotation annotation = field.getAnnotation(KvoAnnotation.class);

			if(annotation == null) {
				return null;
			}

			Class<?> cls = field.getType();

			SpFieldType type = allCfs.get(cls);

			//判断是否为Set<String>类型，其实也并不是非常严谨
			//但是也没有想到更好的方法
			if(type == null) {
				Type genericType = field.getGenericType();
				//判断是否泛型 && 是否是Set.class的子类
				if(genericType instanceof ParameterizedType &&
						Set.class.isAssignableFrom(field.getType())) {
					ParameterizedType pt = (ParameterizedType) genericType;
					if(String.class.isAssignableFrom((Class<?>)(pt.getActualTypeArguments()[0]))) {
						type = STRING_SET;
					}
				}
			}
			return type;
		}

        public static SpFieldType getSpFieldType(Class<?> claz) {
            return allCfs.get(claz);
        }
	}

	/**************************Instance Fields and Methods***************************************/

	public static final String Kvo_SettingVersion = "version";
	@KvoAnnotation(name = Kvo_SettingVersion)
	public int version = DSettingVersion;

	//上一次显示引导页的版本号
	public static final String Kvo_show_guide_version = "show_guide_version";
	@KvoAnnotation(name = Kvo_show_guide_version)
	public String show_guide_version = "";

    public static final String Kvo_gift_list_version = "gift_list_version";
    @KvoAnnotation(name = Kvo_gift_list_version)
    public long gift_list_version = 0L;

	public static final String Kvo_bubble_version = "bubble_version";
	@KvoAnnotation(name = Kvo_bubble_version)
	public long bubble_version = 0L;

    public static final String Kvo_gift_select_id = "gift_select_id";
    @KvoAnnotation(name = Kvo_gift_select_id)
    public int gift_select_id = 0;

	public static final String Kvo_chip_gift_select_id = "chip_gift_select_id";
    @KvoAnnotation(name = Kvo_chip_gift_select_id)
    public int chip_gift_select_id = 0;

    public static final String Kvo_withdraw_cash_rate = "withdraw_cash_rate";
    @KvoAnnotation(name = Kvo_withdraw_cash_rate)
    public long withdraw_cash_rate = 0L;

    public static final String Kvo_sign_in_timestamp = "sign_in_timestamp";
    @KvoAnnotation(name = Kvo_sign_in_timestamp)
    public long sign_in_timestamp = 0L;

    public static final String Kvo_task_todo_timestamp = "task_todo_timestamp";
    @KvoAnnotation(name = Kvo_task_todo_timestamp)
    public long task_todo_timestamp = 0L;

	public static final String Kvo_room_game_sound = "room_game_sound";
    @KvoAnnotation(name = Kvo_room_game_sound)
    public int room_game_sound = 0;

	public static final String Kvo_home_page_list_index = "home_page_list_index";
    @KvoAnnotation(name = Kvo_home_page_list_index)
    public int home_page_list_index = 0;

	private long mUid;
	private SharedPreferences mSharedPreferences;
	private Map<String, Object> mValueMap;

	public DSetting(long uid) {
		reInit(uid);
	}

	private void reInit(long uid) {
		mUid = uid;

		mSharedPreferences = BaseContext.gContext.getSharedPreferences("com.duowan.ada.setting_"
				+ mUid, Context.MODE_PRIVATE);

		reset();

		loadFromPreference();
	}

	private void reset() {
		mValueMap = new ConcurrentHashMap<>();

		setValue(Kvo_SettingVersion, DSettingVersion);
		setValue(Kvo_show_guide_version, "");
		setValue(Kvo_gift_list_version, 0L);
		setValue(Kvo_bubble_version, 0L);
		setValue(Kvo_gift_select_id, 0);
		setValue(Kvo_chip_gift_select_id, 0);
		setValue(Kvo_withdraw_cash_rate, 0L);
		setValue(Kvo_sign_in_timestamp, 0L);
		setValue(Kvo_task_todo_timestamp, 0L);
		setValue(Kvo_room_game_sound, 0);
		setValue(Kvo_home_page_list_index,0);
	}

	public Object getValue(String key, String keySuffix, Object defaultValue) {
		String xkey = JStringUtils.combineStr(key, keySuffix == null ? "" : keySuffix);
		Object value = mValueMap.get(xkey);
		if(value != null) {
			return value;
		}
		value = mSharedPreferences.getAll().get(xkey);
		if (value != null) {
			mValueMap.put(xkey, value);
			return value;
		}
		return defaultValue;
	}

	public void setValue(String key, String keySuffix, Object newValue) {
		setValue(key, newValue);

		String spKey = JStringUtils.combineStr(key, keySuffix == null ? "" : keySuffix);

		mValueMap.put(spKey, newValue);

		SpField sf = settingFieldMap.get(key);
        if (sf == null) {
            // for no kvo
            if (newValue != null) {
                SpFieldType sft = SpFieldType.getSpFieldType(newValue.getClass());
                if (sft != null) {
                    sft.putObject(mSharedPreferences, spKey, newValue);
                }
            }
        } else {
            // for kvo
			sf.type.putObject(mSharedPreferences, spKey, newValue);
		}
	}

	private void loadFromPreference() {
		int version = mSharedPreferences.getInt(Kvo_SettingVersion, 0);
		//如果版本号升级，清空以前的所有，并把默认值存进去
		if(version != DSettingVersion) {
			saveDefaultValues();
		} else {
			Map<String, Object> values = new HashMap<>(mSharedPreferences.getAll());
			Iterator<Map.Entry<String, Object>> ite = values.entrySet().iterator();
			while (ite.hasNext()) {
				Map.Entry<String, Object> entry = ite.next();
				//这里的key，是存储的key，有可能是带后缀的，并不在DSetting的Field中
				String key = entry.getKey();
				Object value = entry.getValue();

				mValueMap.put(key, value);

				SpField sf = settingFieldMap.get(key);
				if(sf != null) {
					setValue(key, value);
				}
			}
		}
	}

	private void saveDefaultValues() {
		mSharedPreferences.edit().clear().apply();

		Iterator<Map.Entry<String, SpField>> ite = settingFieldMap.entrySet().iterator();
		while (ite.hasNext()) {
			Map.Entry<String, SpField> entry = ite.next();
			String key = entry.getKey();
			SpField sf = entry.getValue();

			try {
				//如果是有后缀的key，就不能通过Field来查找到
				if(!KvoAnnotationFlags.settingKeyHasSuffix(sf.field
						.getAnnotation(KvoAnnotation.class).flag())) {
					Object defaultValue = sf.field.get(this);

					mValueMap.put(key, defaultValue);

					sf.type.putObject(mSharedPreferences, entry.getKey(), defaultValue);
				}
			} catch (IllegalAccessException e) {
				JLog.error(this, "field not access " + e);
			}
		}
	}
}
