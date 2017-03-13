package com.duowan.fw.kvo;

import com.duowan.fw.EventDelegate;
import com.duowan.fw.FwEvent.EventArg;
import com.duowan.fw.KvoField;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.bind.E_Property_I;
import com.duowan.fw.kvo.Kvo.KvoDestination;
import com.duowan.fw.kvo.Kvo.KvoEvent;
import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.util.JLog;

/**
 * Design by JerryZhou@outlook.com
 * v 1.0.0
 * combile Kvo and Propertys system
 */
public class KvoProperty {
	public static class KvoPropertyDestination extends KvoDestination{
		public E_Property_I property;
		
		@Override
		protected void invokeReal(final Object realTarget, 
				final EventDelegate realDelegate, 
				final EventArg xevent){
			final KvoEvent event = (KvoEvent)(xevent);
			realDelegate.invoke(new Object[]{event.newValue});	
		}
		
		public static KvoDestination buildPropertyDestination(Object target, E_Property_I pp){
			EventDelegate delegate = EventDelegate.buildDelegate(target, pp.method(), pp.paramTypes());
			if (delegate != null) {
				KvoPropertyDestination destination = new KvoPropertyDestination();
				destination.delegate = delegate;
				destination.property = pp;
				destination.thread = ThreadBus.Main;
				return destination;
			}
			return null;
		}
	}
	
	// kvo interface for binding add
	public static void addKvoBinding(KvoSource source, String name, Object target, E_Property_I property){
		try {
			KvoDestination kvoDestination = KvoPropertyDestination.buildPropertyDestination(target, property);
			if (kvoDestination != null) {
				KvoField kvoField = source.kvoField(name);
				source.addKvoBinding(kvoField, name, target, kvoDestination);
			}else {
				JLog.error(source, "kvo binding failed: " + name + " to: " + property.toString());
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeKvoBinding(KvoSource source, String name, Object target, E_Property_I property){
		try {
			KvoDestination kvoDestination = KvoPropertyDestination.buildPropertyDestination(target, property);
			if (kvoDestination != null) {
				source.removeKvoBinding(name, target, kvoDestination);
			}else {
				JLog.error(source, "kvo binding failed: " + name + " to: " + property.toString());
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
