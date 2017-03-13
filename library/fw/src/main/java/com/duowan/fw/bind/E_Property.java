package com.duowan.fw.bind;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * */
public class E_Property implements E_Property_I {	
	
	public E_Property(String method, Class<?>[] paramTypes) {
		mMethod = method;
		mParamTypes = paramTypes;
	}
	
	private String mMethod; 
	private Class<?>[] mParamTypes;
	
	public String method() {
		return mMethod;
	}
	public Class<?>[] paramTypes() {
		return mParamTypes;
	}
}