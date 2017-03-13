package com.duowan.fw;

import com.duowan.fw.kvo.KvoAnnotation;

import java.lang.reflect.Field;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public abstract class KvoField {
	public Field field;
	public KvoAnnotation annotation;

	public abstract void setFieldValue(Object object, Object value) throws IllegalAccessException;
	public abstract Object getFieldValue(Object object) throws IllegalAccessException, NoSuchFieldException;
}
