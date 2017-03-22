package com.xuzhiyong.comego.module.datacenter;

import java.lang.reflect.Field;

/**
 * Created by yujian on 2016/4/4.
 *
 */
public abstract class CursorField implements Comparable<CursorField> {

	public CursorFieldType type;
	public Field field;
	public String fieldName;
	public int columnIndex;
	public boolean primaryKey;

	public abstract Object get(Object object) throws IllegalAccessException;

	@Override
	public int compareTo(CursorField another) {
		if(this.columnIndex == another.columnIndex) {
			throw new IllegalStateException("two column can't have same columnIndex !!!");
		}
		return this.columnIndex - another.columnIndex;
	}
}