package com.duowan.fw.kvo.databinding;

import com.duowan.fw.KvoField;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public class DatabindingKvoField extends KvoField {

	@SuppressWarnings("unchecked")
	@Override
	public void setFieldValue(Object object, Object value) throws IllegalAccessException {
		((JDatabindingObservable)field.get(object)).setValue(value);
	}

	@Override
	public Object getFieldValue(Object object) throws IllegalAccessException, NoSuchFieldException {
		return ((JDatabindingObservable)field.get(object)).getValue();
	}
}
