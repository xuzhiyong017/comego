package com.duowan.fw;

import com.duowan.fw.util.JLog;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * base on weak delegate 
 * 
 * Take care of Event Key: Object
 * There are three way to extend the FwEvent System
 * 1. extend the class EventArg, just the kvo sytem do
 * 2. extend the class FwEventDestionation and make a new Annotation, just like the Net Proto do
 * 3. extend the class FwEventDispatcher, the extremly extend way, just like the module and net usage
 * */
public class FwEvent {
	
	// base event arg
	public static class EventArg{
		public Object event;
		public Object source;
		public Map<String, Object> args;
		public long flag;
		
		// 32 bit
		public static final long MASK_FLAG_DONE = 1;
		public static final long MASK_FLAG_COUNT = 2;
		public static final long MASK_FLAG_TRACE = 3;

		// reason
		public static final String TraceObj = "_trace_obj";
		public static final String TraceReason = "_trace_reason";
		
		// if event do not want this destination 
		public boolean accept(FwEventDestination destination){
			return destination != null;
		}
		
		// the event should tell which destinations we care about: should copy a new list
		public List<FwEventDestination> index(List<FwEventDestination> destinations){
			return destinations;
		}
		
		// convenience for args
		public <T> T arg0(Class<T> clazz){
			return varsOf(this, 0, clazz);
		}
		
		public <T> T arg1(Class<T> clazz){
			return varsOf(this, 1, clazz);
		}
		
		public <T> T arg2(Class<T> clazz){
			return varsOf(this, 2, clazz);
		}
		
		// thread safe arg put 
		public void putArg(String name, Object arg){
			synchronized (this) {
				if (args == null) {
					args = new HashMap<String, Object>();
				}	
				
				args.put(name, arg);
			}
		}
		
		// thread safe arg check 
		public boolean haveArg(String name){
			synchronized (this) {
				if (args == null) {
					return false;
				}
				return args.containsKey(name);
			}
		}
		
		// thread safe arg get
		public Object arg(String name){
			synchronized (this) {
				if (args == null) {
					return null;
				}
				return args.get(name);
			}
		}
		
		// thread safe arg get template
		public <T> T argT(String name, Class<T> clazz) {
			Object obj = arg(name);
			if(obj != null) {
				return clazz.cast(obj);
			}
			return null;
		}
		
		// if do not want this event continue to dispather, just call done
		public void done(){
			flag |= MASK_FLAG_DONE;
		}
		
		public boolean haveDone(){
			return (flag & MASK_FLAG_DONE) != 0;
		}
		
		public boolean shouldTrace(){
			return (flag & MASK_FLAG_TRACE) != 0;
		}
		 // args convenience
		public static final String Vars = "Vars";
		public static Object[] vars(EventArg event){
			Object vars = event.arg(Vars);
			if (vars != null) {
				return Object[].class.cast(vars);
			}
			return null;
		}
		
		public static <T> T varsOf(EventArg event, int index, Class<T> clazz){
			Object[] vars = vars(event);
			if (vars != null && vars.length > index && index >= 0) {
				return clazz.cast(vars[index]);
			}
			return null;
		}
		
		public static EventArg buildEventWithArg(Object source, Object key, Object ...args){
			EventArg event = new EventArg();
			event.source = source;
			event.event = key;
			event.flag = 0;
			HashMap<String, Object> xargs = null;
			if (args != null) {
				xargs = new HashMap<String, Object>();
				xargs.put(EventArg.Vars, args);
			}
			event.args = xargs;
			return event;
		}
	}
	
	// base event destination
	public static abstract class FwEventDestinationBuilder{
		public FwEventDestination buildDestination(Object target, String name) {
			Method method = null;
			try {
				method = target.getClass().getDeclaredMethod(name, FwEventArgs);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			if(method != null){
				return buildDestination(target, method);
			}
			
			JLog.error(JLog.KFw, "build event failed to: " + name);
			return null;
		} 
		
		public FwEventDestination buildDestination(Object target, Method method) {
			Annotation annotation = annotation(method);
			return buildDestination(target, method, annotation);
		}
		
		public abstract FwEventDestination buildDestination(Object target, Method method, Annotation annotation);
		
		public abstract Annotation annotation(Method method);
		
		public abstract Object key(Annotation annotation);
	}
	public static Class<?>[] FwEventArgs = new Class<?>[]{EventArg.class};
	public static class FwEventDestination{
		public EventDelegate delegate;
		public int thread = ThreadBus.Whatever;
		public int order = 0;

		@Override
		public boolean equals(Object obj){
			if (obj == null || !(obj instanceof FwEventDestination)) {
				return false;
			}
			FwEventDestination other = (FwEventDestination)obj;
			if (other.delegate.mTarget.get() == delegate.mTarget.get() 
					&& other.delegate.mEntry.equals(delegate.mEntry)) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {

			return delegate.toString() + " in thread: " + thread + " with order " + order;
		}
		
		public boolean invoke(final EventArg event){
			final EventDelegate realDelegate = delegate;
			final Object realTarget = delegate.mTarget.get(); 
			if (realTarget != null) {
				if (thread != ThreadBus.Whatever) {
					ThreadBus.bus().callThreadSafe(thread, new Runnable() {
						@Override
						public void run() {
							invokeReal(realTarget, realDelegate, event);
						}
					});
				}else{
					invokeReal(realTarget, realDelegate, event);
				}
				return true;
			}else {
				return false;
			}
		}
		
		public boolean invalid(){
			return delegate == null || delegate.mTarget.get() == null;
		}
		
		protected void invokeReal(final Object realTarget, 
				final EventDelegate realDelegate, 
				final EventArg event){
			realDelegate.invoke(new Object[]{event});
		}
		
		public static FwEventDestination buildDestination(Object target, Method method, Integer thread, Integer order){
			FwEventDestination destination = new FwEventDestination();
			destination.delegate = new EventDelegate();
            destination.delegate.mTarget = new WeakReference<Object>(target);
			destination.delegate.mEntry = method;
			
			if (thread != null) {
				destination.thread = thread;
			}
			if (order != null) {
				destination.order = order;
			}
			return destination;
		}
		
		public static FwEventDestinationBuilder BUILDER = new FwEventDestinationBuilder() {
			
			@Override
			public Annotation annotation(Method method){
				return method.getAnnotation(FwEventAnnotation.class);
			}
			
			@Override
			public Object key(Annotation xannotation) {
				if (xannotation instanceof FwEventAnnotation) {
					FwEventAnnotation annotation = FwEventAnnotation.class.cast(xannotation);
					return annotation.event();
				}
				return null;
			}
			
			@Override
			public FwEventDestination buildDestination(
					Object target, Method method, Annotation xannotation) {
				FwEventAnnotation annotation = FwEventAnnotation.class.cast(xannotation);
				if (annotation != null) {
					return FwEventDestination.buildDestination(target, method, annotation.thread(), annotation.order());
				}else{
					return FwEventDestination.buildDestination(target, method, null, null);
				}
			}
		};
	}
	
	/***
	 * NB! ONLY CAN CALL all(), remove(), size() ON DestinationSortedList
	 * TODO: SHOULD USE A SAFE ORDER LINKED LIST
	 * */
	public static class DestinationSortedList extends ArrayList<FwEventDestination>{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -434035072239870723L;
		
		public DestinationSortedList(Collection<? extends FwEventDestination> collection) {
			super(collection);
	    }
		
		public DestinationSortedList(){
		}

		@Override
		public synchronized boolean add(FwEventDestination object ){
			int i=super.size();
			for(;i>0; --i){
				FwEventDestination right = this.get(i-1);
				if(right.order >= object.order){
					break;
				}
			}
			super.add(i, object);
			return true;
		}
		
		@Override
		public synchronized boolean remove(Object object){
			return super.remove(object);
		}
		
		@Override
		public synchronized int size(){
			return super.size();
		}
		
		public synchronized DestinationSortedList copy(){
			return new DestinationSortedList(this);
		}
	}
	
	// event dispatcher
	public static class FwEventDispatcher{
		protected HashMap<Object, DestinationSortedList> mConnections = new HashMap<Object, DestinationSortedList>();;
		protected final int MaxRunShrinkConnections = 30;
		protected FwEventDestinationBuilder mBuilder = FwEventDestination.BUILDER;
		
		public FwEventDispatcher(){
		}

		public FwEventDispatcher(FwEventDestinationBuilder builder) {
			mBuilder = builder;
		}
		
		public void addBinding(Object key, Object target, String targetName) {
			FwEventDestination destination = mBuilder.buildDestination(target, targetName);
			if (destination != null) {
				addBinding(key, target, destination);
			}
		}
		
		public void addBinding(Object key, Object target, Method method) {
			FwEventDestination destination = mBuilder.buildDestination(target, method);
			if (destination != null) {
				addBinding(key, target, destination);
			}
		}
		
		public void addBinding(Object key, Object target, FwEventDestination destination) {
			//JLog.info(JLog.KFw, "add binding: " + key + " to target: " + target.getClass().getName());
			DestinationSortedList  sourceConnections=null;
			synchronized (mConnections) {
				sourceConnections = mConnections.get(key);
				if (sourceConnections == null) {
					sourceConnections = new DestinationSortedList();
					mConnections.put(key, sourceConnections);
				}	
			}

			sourceConnections.add(destination);

			// call back
			onAddBinding(key, target, destination, sourceConnections);

			// control the connection size
			shrinkConnectionQueue(key, sourceConnections, MaxRunShrinkConnections);
		}
		
		public void removeBinding(Object key, Object target, String targetName){
			FwEventDestination destination = mBuilder.buildDestination(target, targetName);
			if (destination != null) {
				removeBinding(key, target, destination);
			}
		}
		
		public void removeBinding(Object key, Object target, Method method){
			FwEventDestination destination = mBuilder.buildDestination(target, method);
			if (destination != null) {
				removeBinding(key, target, destination);
			}
		}
		
		public void removeBinding(Object key, Object target, FwEventDestination destination){
			DestinationSortedList sourceConnections = null;
			synchronized (mConnections) {
				sourceConnections = mConnections.get(key);
			}
			if (sourceConnections != null) {
				// remove it
				sourceConnections.remove(destination);
				synchronized (mConnections) {
					if (sourceConnections.size() == 0) {
						mConnections.remove(key);
					}	
				}
				// call back
				onRemoveBinding(key, target, destination, sourceConnections);
			}
		}
		
		public void notifyEvent(EventArg event){
			DestinationSortedList sourceConnections = null;
			synchronized (mConnections) {
				sourceConnections = mConnections.get(event.event);
			}
			if (sourceConnections != null && sourceConnections.size() > 0) {
				notifyEvent(event, sourceConnections);
			}
		}
		
		protected void notifyEvent(EventArg event, DestinationSortedList sourceConnections){
			// copy connections
			List<FwEventDestination> connections = event.index(sourceConnections.copy());
			// invoke all connections
			for (FwEventDestination destination : connections) {
				// call event
				if (event.accept(destination)) {
					invokeEventTo(event, destination, sourceConnections);
				}
				// do not need to dispatching anymore
				if(event.haveDone()){
					break;	
				}
				// should trace the event dispatch target for reasons
				if (event.shouldTrace()) {
					event.putArg(EventArg.TraceObj, destination);
				}
			}
		}
		
		protected void invokeEventTo(EventArg event, 
				FwEventDestination destination, 
				DestinationSortedList sourceConnections){
			if(!destination.invoke(event)){
				sourceConnections.remove(destination);
			}
		}
		
		private void shrinkConnectionQueue(Object key, DestinationSortedList sourceConnections, int maxSize){
			if (sourceConnections.size() > maxSize) {
				// must be sure the source connetions is thread safe
				synchronized (sourceConnections) {
					for (Iterator<FwEventDestination> iterator = sourceConnections.iterator(); iterator
							.hasNext();) {
						FwEventDestination destination = (FwEventDestination) iterator
								.next();
						if (destination != null && destination.invalid()) {
							iterator.remove();
						}
					}	
				}
				
				if (sourceConnections.size() > maxSize) {
					JLog.warn(JLog.KFw, "too many connections: " + sourceConnections.size()
							+ " add to: " + key 
							+ " in " + this.toString());
				}
			}
		}
		
		// for subclass to override
		protected void onAddBinding(Object key, Object target, FwEventDestination destination, 
				DestinationSortedList sourceConnections){
		}
		
		// for subclass to override
		protected void onRemoveBinding(Object key, Object target, FwEventDestination destination,
				DestinationSortedList sourceConnections){
		}
	}
	
	public static FwEventDispatcher dispatcher = new FwEventDispatcher();
	
	// auto binding all annotation events
	public static final void autoBindingEvent(Object target){
		autoBindingEvent(dispatcher, FwEventDestination.BUILDER, target);
	}
	
	// auto remove all annotation events
	public static final void autoRemoveEvent(Object target){
		autoRemoveEvent(dispatcher, FwEventDestination.BUILDER, target);
	}
	
	// auto binding node, we will note all middle info when first binding behavior happend
	public static class MethodNode{
		public Method method; 				// dst method
		public Annotation annotation;		// dst method annotation
		public Object key;					// event key
	}

	// cache all annotations
	public static HashMap<FwEventDestinationBuilder, 
		HashMap<Class<?>, HashMap<Class<?>, ArrayList<MethodNode>>>> builder2Clazz2Methods 
	= new HashMap<FwEventDestinationBuilder,
		HashMap<Class<?>, HashMap<Class<?>, ArrayList<MethodNode>>>>();

	// thread safe for builder2Clazz2Methods
	public static ArrayList<MethodNode> getMethods(FwEventDestinationBuilder builder, 
			FwEventDispatcher source, 
			Object dst){
		HashMap<Class<?>, HashMap<Class<?>, ArrayList<MethodNode>>> builderClazz = null;
		synchronized (builder2Clazz2Methods) {
			builderClazz = builder2Clazz2Methods.get(builder);
			if (builderClazz == null) {
				builderClazz = new HashMap<Class<?>, HashMap<Class<?>, ArrayList<MethodNode>>>(); 
				builder2Clazz2Methods.put(builder, builderClazz);	
			}
		}
		
		HashMap<Class<?>, ArrayList<MethodNode>> clazz2Method = null;
		synchronized (builderClazz) {
			clazz2Method = builderClazz.get(dst.getClass());
			if (clazz2Method == null) {
				clazz2Method = new HashMap<Class<?>, ArrayList<MethodNode>>(); 
				builderClazz.put(dst.getClass(), clazz2Method);
			}
		}
		
		ArrayList<MethodNode> methodList = null;
		synchronized (clazz2Method) {
			methodList = clazz2Method.get(source.getClass());
			if (methodList != null) {
				return methodList;
			}else{
				methodList = new ArrayList<MethodNode>();
				clazz2Method.put(source.getClass(), methodList);
				
				Method[] methods = dst.getClass().getDeclaredMethods();
				for (Method method : methods) {
					Annotation annotation = builder.annotation(method);
					if (annotation != null) {
						FwEventDestination destination = builder.buildDestination(dst, method, annotation);
						if (destination != null) {
							MethodNode node = new MethodNode();
							node.annotation = annotation;
							node.key = builder.key(annotation);
							node.method = method;
							methodList.add(node);
						}
					}
				}
				return methodList;
			}
		}
	}

	// auto binding all annotation events
	public static final void autoBindingEvent(FwEventDispatcher dispatcher, 
			FwEventDestinationBuilder builder, Object target){
		Method[] methods = target.getClass().getDeclaredMethods();
		for (Method method : methods) {
			Annotation annotation = builder.annotation(method);
			if (annotation != null) {
				FwEventDestination destination = builder.buildDestination(target, method, annotation);
				if (destination != null) {
					dispatcher.addBinding(builder.key(annotation), target, destination);
				}
			}
		}
	}

	// auto remove all annotation events
	public static final void autoRemoveEvent(FwEventDispatcher dispatcher, 
			FwEventDestinationBuilder builder, Object target){
		Method[] methods = target.getClass().getDeclaredMethods();
		for (Method method : methods) {
			Annotation annotation = builder.annotation(method);
			if (annotation != null) {
				FwEventDestination destination = builder.buildDestination(target, method, annotation);
				if (destination != null) {
					dispatcher.removeBinding(builder.key(annotation), target, destination);
				}
			}
		}
	}
}
