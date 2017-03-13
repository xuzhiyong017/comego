package com.duowan.fw.kvo;

import com.duowan.fw.KvoField;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public class DefaultKvoField extends KvoField {

	public void setFieldValue(Object object, Object value) throws IllegalAccessException {
		field.set(object, value);
	}

	public Object getFieldValue(Object object) throws IllegalAccessException, NoSuchFieldException {
		return field.get(object);
	}
}
