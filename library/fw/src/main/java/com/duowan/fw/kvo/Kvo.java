package com.duowan.fw.kvo;

import android.os.Handler;

import com.duowan.fw.EventDelegate;
import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEvent.DestinationSortedList;
import com.duowan.fw.FwEvent.FwEventDestination;
import com.duowan.fw.FwEvent.FwEventDestinationBuilder;
import com.duowan.fw.FwEvent.FwEventDispatcher;
import com.duowan.fw.KvoField;
import com.duowan.fw.kvo.databinding.DatabindingKvoField;
import com.duowan.fw.kvo.databinding.JDatabindingObservable;
import com.duowan.fw.util.JConstant;
import com.duowan.fw.util.JFlag;
import com.duowan.fw.util.JLog;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Design by JerryZhou@outlook.com
 * v 1.0.0
 * one way binding: base on public filed
 * useage:
 * THE FIRST SITUATION:
 * class KVOData : extends KvoSource{							// must inherited from KvoSource
 * 		public static final String Kvo_example = "example";		// string must be same with the field name
 * 		@KvoAnnotation(name=Kvo_example)						// must add a KvoAnnotion to any can binding filed
 * 		public String example;
 * }
 * 
 * class AcceptKvoChange {
 * private KVOData data = new KVOData();
 * public AcceptKvoChange(){
 *		// handle to deal with binding: will conect data.example to onKvoDataExampleChangeAuto
 * 		Kvo.addKvoBinding(data, KVOData.Kvo_example, this, "onKvoDataExampleChange");
 * 		// auto binding: will conect data.example to onKvoDataExampleChangeAuto
 * 		Kvo.autoBindingTo(data, this);
 * 
 * 		// trigger the kvo notify: will trigger "onKvoDataExampleChange" and "onKvoDataExampleChangeAuto" called
 * 		data.setValue(KVODat.Kvo_example, "new Hello world!!");
 * }
 * public void onKvoDataExampleChange(KvoEvent event){
 * 		String newExampleString = (String)(event.newValue);
 * }
 * 
 * @KvoAnnotation(name=KVOData.Kvo_example)
 * public void onKvoDataExampleChangeAuto(KvoEvent event){
 * 		String newExampleString = (String)(event.newValue);
 * }
 * */
public class Kvo {
	
	// event args
	public static class KvoEvent extends FwEvent.EventArg{
		public String name;
		public KvoSource from;
		public Object oldValue;
		public Object newValue;
		public long ts;
		
		public <T> T caseNewValue(Class<T> clazz){
			return clazz.cast(newValue);
		}
		
		public <T> T caseOldValue(Class<T> clazz){
			return clazz.cast(oldValue);
		}
		
		public <T> T caseNewValue(Class<T> clazz, T def){
			T value = clazz.cast(newValue);
			if(value == null){
				return def;
			}
			return value;
		}
		
		public <T> T caseOldValue(Class<T> clazz, T def){
			T value = clazz.cast(oldValue);
			if(value == null){
				return def;
			}
			return value;
		}
	}

	// kvo destination
	public static Class<?>[] KvoEventArgs = new Class<?>[]{KvoEvent.class};
	public static class KvoDestination extends FwEventDestination{
		public volatile long ts;
		public int control;
		public int flag;

		/**
		 * */
		public static final int KvoDstFlagSkipOldTrigger = KvoAnnotation.KvoControlSkipOldTrigger;

		@Override
		public boolean invoke(final FwEvent.EventArg event){
			KvoEvent kvoEvent = KvoEvent.class.cast(event);
			if (kvoEvent.ts > 0 && (this.control & KvoDstFlagSkipOldTrigger) != 0) {
				if (kvoEvent.ts <= this.ts) {
					JLog.error(JLog.KError, "kvo error: wrong event (" + kvoEvent.name + ") ts send to destination "
							+ this.toString());
					return true;
				}
				this.ts = kvoEvent.ts;
			}

			return super.invoke(event);
		}

		public static KvoDestination buildKvoDestination(Object target, String name){
			KvoDestination destination = new KvoDestination();
			destination.delegate = EventDelegate.buildDelegate(target, name, KvoEventArgs);
			if (destination.delegate != null) {
				KvoAnnotation annotation = destination.delegate.mEntry.getAnnotation(KvoAnnotation.class);
				if (annotation != null) {
					destination.order = annotation.order();
					destination.thread = annotation.thread();
					destination.flag = annotation.flag();
					destination.control = annotation.control();
				}
				return destination;
			}
			JLog.error(target, "kvo failed to: " + name);
			return null;
		}
		
		public static KvoDestination buildKvoDestination(Object target, Method method){
			KvoDestination destination = new KvoDestination();
			EventDelegate delegate = new EventDelegate();
			delegate.mTarget = new WeakReference<Object>(target);
			delegate.mEntry = method;
			destination.delegate = delegate;
			KvoAnnotation annotation = destination.delegate.mEntry.getAnnotation(KvoAnnotation.class);
			if (annotation != null) {
				destination.order = annotation.order();
				destination.thread = annotation.thread();
				destination.flag = annotation.flag();
				destination.control = annotation.control();
			}
			return destination;
		}
		
		public static KvoDestination buildKvoDestination(Object target, Method method, KvoAnnotation annotation){
			KvoDestination destination = new KvoDestination();
			EventDelegate delegate = new EventDelegate();
			delegate.mTarget = new WeakReference<Object>(target);
			delegate.mEntry = method;
			destination.delegate = delegate;
			destination.order = annotation.order();
			destination.thread = annotation.thread();
			destination.flag = annotation.flag();
			destination.control = annotation.control();
			return destination;
		}
		
		public static final FwEventDestinationBuilder BUILDER = new FwEventDestinationBuilder() {
			
			@Override
			public Object key(Annotation xannotation) {
				if (xannotation instanceof KvoAnnotation) {
					KvoAnnotation annotation = KvoAnnotation.class.cast(xannotation);
					return annotation.name();
				}
				return null;
			}
			
			@Override
			public FwEventDestination buildDestination(Object target, Method method,
					Annotation xannotation) {
				KvoAnnotation annotation = KvoAnnotation.class.cast(xannotation);
				return KvoDestination.buildKvoDestination(target, method, annotation);
			}
			
			@Override
			public FwEventDestination buildDestination(Object target, Method method) {
				return KvoDestination.buildKvoDestination(target, method);
			}
			
			@Override
			public FwEventDestination buildDestination(Object target, String name) {
				return KvoDestination.buildKvoDestination(target, name);
			}
			
			@Override
			public Annotation annotation(Method method) {
				return method.getAnnotation(KvoAnnotation.class);
			}
		};
	}
	
	// we can listing to the kvo behavior
	public static interface KvoSourceChange{
		void onAddBinding(KvoSource source, String name, Object target, KvoDestination destination);
		void onRemoveBinding(KvoSource source, String name, Object target, KvoDestination destination);
	}
	
	// kvo flags
	public static final long KvoFlagSupportChange = 1;
	public static final long KvoFlagSupportCallTheBindingInit = 1<<1;
	public static final long KvoFlagSupportThreadSafe = (1<<2);
	public static final long KvoFlagSupportTriggerReason = (1<<3);
	public static final long KvoFlagSupportTriggerReasonAddBinding = (1<<4);
	public static final long KvoFlagSupportTriggerReasonRemoveBinding = (1<<5);
	public static final long KvoFlagSupportTriggerReasonSetValue = (1<<6);
	public static final long KvoFlagSupportTriggerBatch = (1<<7);
	public static final long KvoFlagSupportCallTheBindingInitInMain = (1<<8);
	
	// kvo event source
	public static class KvoSource extends FwEventDispatcher{
		// meta change listeners
		private ConcurrentLinkedQueue<KvoSourceChange> mChanges;
		// connections
		private HashMap<String, KvoField> mKvoValues;
		// group changes
		private HashMap<String, BatchKvoRunnabe> mKvoGroupChanges;
		// all kvo filed
		private static HashMap<Class<?>, HashMap<String, KvoField>> allKvoFields 
		= new HashMap<>();
		// control flag
		private long mKvoFlag;
		// seq
		protected AtomicLong mSeq;
		
		// meta propertys: used to trigger meta kvo event
		public static final String TriggerReason_AddBinding = "AddBinding"; 	// key, target, destination
		public static final String TriggerReason_RemoveBinding = "RemoveBing"; 	// key, target, destination
		public static final String TriggerReason_SetValue = "SetValue";			// key, value, KvoSettingReturns(only for call setvalue)
		
		public static final String Kvo_triggerReason = "triggerReason";
		@KvoAnnotation(name=Kvo_triggerReason)
		public String triggerReason;
		
		// default constructor, init the kvo filed
		public KvoSource() {
			mKvoValues = kvoFieldsContainerFor(this.getClass());
			mKvoFlag = KvoFlagSupportCallTheBindingInit 
					| KvoFlagSupportThreadSafe;
					// | KvoFlagSupportTriggerBatch	// Disable Batch Default

			mSeq = new AtomicLong(1);
		}
		
		public static HashMap<String, KvoField> kvoFieldsContainerFor(Class<?> clazz){
			synchronized (allKvoFields) {
				HashMap<String, KvoField> kvoFields = allKvoFields.get(clazz);	
				if (kvoFields == null) {
					kvoFields = new HashMap<>();
					allKvoFields.put(clazz, kvoFields);
				}
				return kvoFields;	
			}
		}

		/// setting result
		public static class KvoSettingReturns{
			public Object oldValue;
			public Object newValue;
			public String name;
			public Boolean truelyChanged;
			public long ts;
		}
		
		/// collection setting changes
		public static class KvoSettingChanges{
			public HashMap<String, KvoEvent> settings = 
					new HashMap<String, KvoEvent>();
		}

		public void setFlag(long flag){
			mKvoFlag = JFlag.setFlag(mKvoFlag, flag);
		}
		
		public void clearFlag(long flag){
			mKvoFlag = JFlag.clearFlag(mKvoFlag, flag);
		}
		
		public long flag(){
			return mKvoFlag;
		}
		
		public boolean isFlag(long flag){
			return JFlag.isFlag(mKvoFlag, flag);
		}
		
		// easy to use
		// wrap the event and @ setKvoValue
		public void setValue(String name, Object newValue){
			notifyKvoEvent(name, newValue);
		}

		// easy to use
		// wrap the vent and @ setKvoValue
		public void setValueWithReason(String name, Object newValue, String reason) {
			notifyKvoEvent(name, newValue, reason);
		}

		public void notifyKvoEvent(String name, Object newValue) {
			notifyKvoEvent(name, newValue, null);
		}

		public void notifyKvoEvent(String name, Object newValue, String reason){
			KvoSettingReturns settingReturns = null;
			try {
				settingReturns = setKvoValue(name, newValue);
			} catch (SecurityException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (Exception e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			}

			KvoEvent event  = buildKvoEventAbout(settingReturns, reason);
			if (event != null) {
				notifyKvoEvent(event);
			}
		}
		
		public void notifyKvoEvent(String name){
			KvoEvent event = buildKvoEventAbout(name, null);
			if (event != null) {
				notifyKvoEvent(event);
			}
		}

		public void notifyKvoEvent(KvoEvent event){
			// dispatch all connections
			dispatchKvoEvent(event);
			// trigger batch
			triggerBatch(event);
		}

		public void addKvoBinding(String name, Object target, String targetName) throws SecurityException, NoSuchFieldException{
			KvoField kvoField = kvoField(name);
			addKvoBinding(kvoField, name, target, targetName);
		}
		
		public void addKvoBinding(KvoField kvoField, String name, Object target, String targetName) {
			KvoDestination kvoDestination = KvoDestination.buildKvoDestination(target, targetName);
			if (kvoDestination != null) {
				addKvoBinding(kvoField, name, targetName, kvoDestination);
			}
		}
		
		public void addKvoBinding(KvoField kvoField, String name, Object target, KvoDestination kvoDestination) {
			//JLog.info(this, "add kvo binding: " + name + " to target: " + target.getClass().getName());
			addBinding(name, target, kvoDestination);
		}
		
		public void removeKvoBinding(String name, Object target, String targetName){
			KvoDestination kvoDestination = KvoDestination.buildKvoDestination(target, targetName);
			if (kvoDestination != null) {
				removeKvoBinding(name, target, kvoDestination);
			}
		}
		
		public void removeKvoBinding(String name, Object target, KvoDestination kvoDestination){
			removeBinding(name, target, kvoDestination);
		}

		private void dispatchKvoEvent(KvoEvent event){
			DestinationSortedList sourceConnections = mConnections.get(event.name);
			if (sourceConnections != null) {
				notifyEvent(event, sourceConnections);
			}
		}

		private KvoEvent buildKvoEventAbout(String name, String reason) {
			Object oldValue = null;
			Object newValue = null;
			try {
				KvoField kvoField = kvoField(name);

				if (kvoField.field != null) {
					newValue = kvoField.getFieldValue(this);
				}

				KvoEvent kvoEvent = new KvoEvent();
				kvoEvent.event = name;
				kvoEvent.source = this;
				kvoEvent.name = name;
				kvoEvent.from = this;
				kvoEvent.oldValue = oldValue;
				kvoEvent.newValue = newValue;
				kvoEvent.ts = mSeq.addAndGet(1);
				return kvoEvent;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (SecurityException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				JLog.error(this, "notify kvo event failed:" + buildExceptionMsg("", e));
			}
			
			return null;
		}

		private KvoEvent buildKvoEventAbout(KvoSettingReturns setting, String reason) {
			if (setting == null || !setting.truelyChanged) {
				return null;
			}
			KvoEvent kvoEvent = new KvoEvent();
			kvoEvent.event = setting.name;
			kvoEvent.name = setting.name;
			kvoEvent.from = this;
			kvoEvent.source = this;
			kvoEvent.oldValue = setting.oldValue;
			kvoEvent.newValue = setting.newValue;
			kvoEvent.ts = mSeq.addAndGet(1);
			if (reason != null) {
				kvoEvent.putArg(Kvo_triggerReason, reason);
			}

			return kvoEvent;
		}
		
		protected KvoSettingReturns setKvoValue(String name, Object newValue)
				throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
			Object oldValue = null;
			KvoField kvoField = kvoField(name);

			if (kvoField.field == null) {
				return null;
			}

			oldValue = kvoField.getFieldValue(this);

			if (oldValue != newValue && 
					!(oldValue != null ? oldValue.equals(newValue) : newValue.equals(oldValue))) {
				kvoField.setFieldValue(this, newValue);
				KvoSettingReturns settingReturns = new KvoSettingReturns();
				settingReturns.oldValue = oldValue;
				settingReturns.truelyChanged = true;
				settingReturns.newValue = newValue; 
				settingReturns.name = name;
				settingReturns.ts = mSeq.addAndGet(1);
				// trigger meta event for set kvo value
				if (isFlag(KvoFlagSupportTriggerReasonSetValue)) {
					triggerReason(TriggerReason_SetValue, name, newValue, settingReturns);
				}
				return settingReturns;
			}

			return null;
		}
		
		public KvoField kvoField(String name) {
			KvoField kvoField = null;
			synchronized (mKvoValues) {
				kvoField = mKvoValues.get(name);
				if (kvoField == null) {
					kvoField = buildNewKvoField(name);

					mKvoValues.put(name, kvoField);

					verifying(kvoField);
				}
			}
			
			return kvoField;
		}

		protected KvoField buildNewKvoField(String name) {
			KvoField kvoField;

			try {
				Field field = this.getClass().getField(name);

				if(JDatabindingObservable.class.isAssignableFrom(field.getType())) {
					kvoField = new DatabindingKvoField();
				} else {
					kvoField = new DefaultKvoField();
				}

				kvoField.field = field;
				kvoField.annotation = field != null ? field.getAnnotation(KvoAnnotation.class) : null;

				return kvoField;
			} catch (Exception e) {
				e.printStackTrace();

				kvoField = new DefaultKvoField();
				return kvoField;
			}
		}
		
		private void verifying(KvoField kvoField){
			Field field = kvoField.field;
			if (field != null && JConstant.debuggable) {
				if (kvoField.annotation == null) {
					JLog.error(this, "should add annotion to filed: " + field.toString());
				}else {
					if(!kvoField.annotation.name().equals(field.getName())){
						JLog.error(this, "wrong annotion name: " + kvoField.annotation.name() 
								+ " for filed: " + field.getName());
					}
				}
			}
		}
		
		public void triggerReason(String reason, Object ...args){
			if (JFlag.isFlag(mKvoFlag, KvoFlagSupportTriggerReason)) {
				// note reason
				this.triggerReason = reason;
				// build event
				KvoEvent event = buildKvoEventAbout(Kvo_triggerReason, null);
				if (args != null) {
					event.args = new HashMap<String, Object>();
					event.args.put(KvoEvent.Vars, args);
				}
				// notify trigger reason
				notifyEvent(event);
			}
		}
		
		public void addSourceChange(KvoSourceChange change){
			if (mChanges == null) {
				mChanges = new ConcurrentLinkedQueue<Kvo.KvoSourceChange>();
			}
			mChanges.add(change);
		}
		
		public void removeSourceChange(KvoSourceChange change){
			if (mChanges != null) {
				mChanges.remove(change);
			}
		}
		
		public void stopBindingCall(){
			mKvoFlag = JFlag.clearFlag(mKvoFlag, KvoFlagSupportCallTheBindingInit);
		}
		
		public void startBindingCall(){
			mKvoFlag = JFlag.setFlag(mKvoFlag, KvoFlagSupportCallTheBindingInit);
		}
		
		@Override
		protected void onAddBinding(Object key, Object target, FwEventDestination xdestination, 
				final DestinationSortedList sourceConnections){
			if(!JFlag.isFlag(mKvoFlag, KvoFlagSupportCallTheBindingInit)){
				return;
			}
			final String name = (String)key;
			final KvoDestination destination = (KvoDestination)xdestination;

			// init the binding value
            KvoEvent event = buildKvoEventAbout(name, TriggerReason_AddBinding);
            invokeEventTo(event, destination, sourceConnections);

			// changes
			if (JFlag.isFlag(mKvoFlag,  KvoFlagSupportChange) && mChanges != null && mChanges.size() > 0) {
				for (Iterator<KvoSourceChange> iterator = mChanges.iterator(); iterator
						.hasNext();) {
					KvoSourceChange change = iterator.next();
					change.onAddBinding(this, name, target, destination);
				}
			}

			// make a reason
			if (JFlag.isFlag(mKvoFlag, KvoFlagSupportTriggerReasonAddBinding)) {
				triggerReason(TriggerReason_AddBinding, key, target, xdestination);
			}
		}
		
		@Override
		protected void onRemoveBinding(Object key, Object target, FwEventDestination xdestination,
				DestinationSortedList sourceConnections){
			String name = (String)key;
			KvoDestination destination = (KvoDestination)xdestination;
			if (JFlag.isFlag(mKvoFlag,  KvoFlagSupportChange) && mChanges != null && mChanges.size() > 0) {
				for (Iterator<KvoSourceChange> iterator = mChanges.iterator(); iterator
						.hasNext();) {
					KvoSourceChange change = iterator.next();
					change.onRemoveBinding(this, name, target, destination);
				}
			}
			if (JFlag.isFlag(mKvoFlag, KvoFlagSupportTriggerReasonRemoveBinding)) {
				triggerReason(TriggerReason_RemoveBinding, key, target, xdestination);
			}
		}
		
		public static class BatchKvoRunnabe{
			public KvoEvent event;
			public Runnable runnable;
			public Handler handler;
		}
		
		public void triggerBatch(final String group){
			triggerBatch(group, null);
		}
		
		// may be grouped the kvo event
		private void triggerBatch(KvoEvent event){
			if (!JFlag.isFlag(this.mKvoFlag, KvoFlagSupportTriggerBatch)) {
				return;
			}
			try {
				KvoField kvoField = kvoField(event.name);
				if (kvoField != null 
						&& kvoField.annotation != null 
						&& !kvoField.annotation.group().isEmpty()) {
					triggerBatch(kvoField.annotation.group(), event);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		
		private void triggerBatch(final String group, KvoEvent because){
			// do not need
			if(mConnections.get(group) == null){
				return;
			}
			
			BatchKvoRunnabe runnable = null;
			synchronized (this) {
				if(mKvoGroupChanges == null){
					mKvoGroupChanges = new HashMap<String, BatchKvoRunnabe>(); 
				}
				runnable = mKvoGroupChanges.get(group); 
				if (runnable == null) {
					// construct the kvo runnable
					runnable = new BatchKvoRunnabe();
					// collect group kvo event
					final KvoEvent batchEvent = new KvoEvent();
					batchEvent.name = group;
					batchEvent.from = this;
					if (because != null) {
						batchEvent.putArg(because.name, because);
					}
					runnable.handler = new Handler();
					runnable.event = batchEvent;
					runnable.runnable = new Runnable() {
						@Override
						public void run() {
							synchronized (KvoSource.this) {
								mKvoGroupChanges.remove(group);
							}
							dispatchKvoEvent(batchEvent);
						}
					};
					// make remember
					mKvoGroupChanges.put(group, runnable);
					// in current thread post
					runnable.handler.post(runnable.runnable);
				}else{
					// collect the event
					synchronized (runnable.event) {
						if (because != null) {
							runnable.event.putArg(because.name, because);
						}
					}
				}
			}
		}

		private String buildExceptionMsg(String msg, Exception e) {
			StringBuilder sb = new StringBuilder(msg);
			sb.append(" [##Exception##] " + e.toString());
			sb.append(" [##Stacktrace##] " + stackTraceToString(e));
			return sb.toString();
		}

		public String stackTraceToString(Throwable e) {
			StringBuilder sb = new StringBuilder();
			for (StackTraceElement element : e.getStackTrace()) {
				sb.append(element.toString());
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	
	// kvo interface for binding add
	public static void addKvoBinding(KvoSource source, String name, Object target, String targetName){
		try {
			source.addKvoBinding(name, target, targetName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	// kvo interface for binding remove
	public static void removeKvoBinding(KvoSource source, String name, Object target, String targetName){
		source.removeKvoBinding(name, target, targetName);
	}
	
	// kvo auto binding node, we will note all middle info when first binding behavior happend
	public static class KvoMethodNode{
		public Method method; 				// dst method
		public KvoAnnotation annotation;	// dst method annotation
		public KvoField kvoField;			// kvo source field
	}
	
	// cache all annotations
	public static HashMap<Class<?>, HashMap<Class<?>, ArrayList<KvoMethodNode>>> clazz2KvoMethods 
	= new HashMap<Class<?>, HashMap<Class<?>, ArrayList<KvoMethodNode>>>();
	
	// thread safe for classzKvoMethods
	public static ArrayList<KvoMethodNode> getKvoMethods(KvoSource source, Object dst){
		HashMap<Class<?>, ArrayList<KvoMethodNode>> kvoMethods = null;
		ArrayList<KvoMethodNode> methodList = null;
		synchronized (clazz2KvoMethods) {
			kvoMethods = clazz2KvoMethods.get(dst.getClass());
			if (kvoMethods != null) {
				methodList = kvoMethods.get(source.getClass());
			}else {
				kvoMethods = new HashMap<>();
				clazz2KvoMethods.put(dst.getClass(), kvoMethods);
			}	
			if (methodList != null) {
				return methodList;
			}else {
				methodList = new ArrayList<>();
				kvoMethods.put(source.getClass(), methodList);
			}
		}
		
		Method[] methods = dst.getClass().getDeclaredMethods();
		for (Method method : methods) {
			KvoAnnotation annotation = method.getAnnotation(KvoAnnotation.class);
			if (annotation != null
					&& (annotation.targetClass() == KvoSource.class
					|| annotation.targetClass().isAssignableFrom(source.getClass()))) {
				try {
					KvoField kvoField = source.kvoField(annotation.name());
					if (kvoField.annotation != null && kvoField.field != null) {
						KvoMethodNode node = new KvoMethodNode();
						node.method = method;
						node.annotation = annotation;
						node.kvoField = kvoField;
						methodList.add(node);	
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return methodList;
	}
	
	// trigger reason
	public static final String TriggerReasonSingleUnBindFrom = "SingleUnBindFrom";
	public static final String TriggerReasonSingleBindTo = "SingleBindTo";
	
	// kvo auto binding through kvo annotation
	public static void autoBindingTo(KvoSource source, Object dst){
		ArrayList<KvoMethodNode> nodes = getKvoMethods(source, dst);
		for (KvoMethodNode node : nodes) {
			KvoDestination destination = KvoDestination.buildKvoDestination(dst, node.method, node.annotation);
			destination.order = node.annotation.order();
			destination.thread = node.annotation.thread();
			source.addKvoBinding(node.kvoField, node.annotation.name(), dst, destination);
		}
		
		source.triggerReason(TriggerReasonSingleBindTo, dst);
	}
	
	// kvo auto unbinding through kvo annotation
	public static void autoUnbindingFrom(KvoSource source, Object dst){
		source.triggerReason(TriggerReasonSingleUnBindFrom, dst);
		
		ArrayList<KvoMethodNode> nodes = getKvoMethods(source, dst);
		for (KvoMethodNode node : nodes) {
			KvoDestination kvoDestination = KvoDestination.buildKvoDestination(dst, node.method, node.annotation);
			source.removeKvoBinding(node.annotation.name(), dst, kvoDestination);
		}
	}
}
