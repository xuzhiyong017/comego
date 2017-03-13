package com.duowan.fw;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface FwEventAnnotation {
	String event();
	int order() default 0;
	int thread() default ThreadBus.Whatever;
}
