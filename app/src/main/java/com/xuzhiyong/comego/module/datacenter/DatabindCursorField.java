package com.xuzhiyong.comego.module.datacenter;

import com.duowan.fw.kvo.databinding.JDatabindingObservable;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public class DatabindCursorField extends CursorField {

	@Override
	public Object get(Object object) throws IllegalAccessException {
		return ((JDatabindingObservable)(field.get(object))).getValue();
	}
}
