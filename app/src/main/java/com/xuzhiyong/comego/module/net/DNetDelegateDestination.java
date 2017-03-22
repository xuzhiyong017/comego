package com.xuzhiyong.comego.module.net;

import com.duowan.fw.EventDelegate;
import com.duowan.fw.FwEvent.EventArg;
import com.duowan.fw.FwEvent.FwEventDestination;
import com.duowan.fw.FwEvent.FwEventDestinationBuilder;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JUtils;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class DNetDelegateDestination extends FwEventDestination {
	@Override
	protected void invokeReal(final Object realTarget,
			final EventDelegate realDelegate,
			final EventArg event){
		Object[] vars = EventArg.vars(event);
		Proto proto = (Proto)vars[0];
		realDelegate.invoke(new Object[]{proto});
	}

	private static Class<?>[] ProtoArgs = new Class<?>[]{Proto.class};
	
	public static FwEventDestination buildDestination(Object target, String method){
		DNetDelegateDestination destination = new DNetDelegateDestination();
		destination.thread = ThreadBus.Main;
		destination.delegate = EventDelegate.buildDelegate(target, method, ProtoArgs);
		if (destination.delegate != null) {
			DNetAnnoation annoation = destination.delegate.mEntry.getAnnotation(DNetAnnoation.class);
			if (annoation != null) {
				destination.thread = annoation.thread();
				destination.order = annoation.order();
			}
			return destination;
		}
		JLog.error(target, "fw event failed to: " + method);
		return null;
	}
	
	public static FwEventDestination buildDestination(Object target, Method method, DNetAnnoation annotation){
		DNetDelegateDestination destination = new DNetDelegateDestination();
		destination.thread = ThreadBus.Main;
		EventDelegate delegate = new EventDelegate();
		delegate.mTarget = new WeakReference<Object>(target);
		delegate.mEntry = method;
		destination.delegate = delegate;
		if (annotation != null) {
			destination.thread = annotation.thread();
			destination.order = annotation.order();
		}
		if (destination.delegate != null) {
			return destination;
		}
		JLog.error(target, "fw event failed to: " + method);
		return null;
	}
	
	public static FwEventDestinationBuilder BUILDER = new FwEventDestinationBuilder(){

		@Override
		public FwEventDestination buildDestination(Object target, String name) {
			return DNetDelegateDestination.buildDestination(target, name);
		}

		@Override
		public FwEventDestination buildDestination(Object target, Method method) {
			JUtils.jAssert(false);
			return null;
		}

		@Override
		public FwEventDestination buildDestination(Object target,
		                                           Method method, Annotation xannotation) {
			DNetAnnoation annotation = DNetAnnoation.class.cast(xannotation);
			if (annotation != null) {
				return DNetDelegateDestination.buildDestination(target, method, annotation);
			}
			return null;
		}

		@Override
		public Annotation annotation(Method method) {
			return method.getAnnotation(DNetAnnoation.class);
		}

		@Override
		public Object key(Annotation xannotation) {
			if (xannotation instanceof DNetAnnoation) {
				DNetAnnoation annotation = DNetAnnoation.class.cast(xannotation);
				return NetHelper.makeUri(annotation.group(), annotation.sub());
			}
			return null;
		}
		
	};
}
