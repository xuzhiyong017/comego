package com.duowan.fw.kvo;

import com.duowan.fw.ThreadBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Design by JerryZhou@outlook.com
 * v 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface KvoAnnotation{
	public static final String DefaultAuthor = "Jerry";
	public static final String DefaultGroup = "";
	public static final String DefaultDescription = "Binding Usage";
	public static final int DefaultControl = 1;

	/**
	 * */
	public static final int KvoControlSkipOldTrigger = 1;

	String name();
	String group() default DefaultGroup;
	String author() default DefaultAuthor;
	String description() default DefaultDescription;
	Class<?> targetClass() default Kvo.KvoSource.class;
	int order() default 0;
	int thread() default ThreadBus.Whatever;
	int control() default DefaultControl;

	int flag() default 0;
}
