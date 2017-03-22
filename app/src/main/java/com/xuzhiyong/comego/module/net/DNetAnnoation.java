package com.xuzhiyong.comego.module.net;

import com.duowan.fw.ThreadBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DNetAnnoation {
	int group();
	int sub();
	int thread() default ThreadBus.Main;
	int order() default 0;
}
