package com.xuzhiyong.comego.module.datacenter;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public class DefaultCursorField extends CursorField {

	@Override
	public Object get(Object object) throws IllegalAccessException {
		return field.get(object);
	}
}
