package com.duowan.fw;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by jerry on 16/6/24.
 */
public class FwEventListener {

    // save all Mids
    public static HashMap<Class<?>, Object> Mids = new HashMap<Class<?>, Object>();

    // register the interface
    public static void register(final Class<?> clazz) {

        ClassLoader loader = clazz.getClassLoader();

        Object mid = java.lang.reflect.Proxy.newProxyInstance(
                loader,
                new Class[] { clazz },
                new java.lang.reflect.InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        FwEvent.EventArg arg = FwEvent.EventArg.buildEventWithArg(
                                clazz,                                  // event-source
                                eventKey(clazz, method),                // event-key
                                args);                                  // event-args

                        FwEvent.dispatcher.notifyEvent(arg);

                        return null;
                    }
                });

        Mids.put(clazz, mid);
    }

    // the interface dispatcher
    public static <T> T event(Class<T> clazz) {
        return clazz.cast(Mids.get(clazz));
    }

    // golbal event key append with method hash-code
    public static String eventKey(Class<?> clazz, Method method) {
        return "Fw:" + clazz.getClass().getName() +":" + method.hashCode();
    }

    // do the real destination invoke with args
    public static class FwEventListenerDestination extends FwEvent.FwEventDestination {

        @Override
        protected void invokeReal(final Object realTarget,
                                  final EventDelegate realDelegate,
                                  final FwEvent.EventArg event){
            realDelegate.invoke(FwEvent.EventArg.vars(event));
        }

        public static FwEvent.FwEventDestination buildDestination(Object target, Method method, Integer thread, Integer order){
            FwEventListenerDestination destination = new FwEventListenerDestination();
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

        public static FwEvent.FwEventDestinationBuilder BUILDER = new FwEvent.FwEventDestinationBuilder() {

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
            public FwEvent.FwEventDestination buildDestination(
                    Object target, Method method, Annotation xannotation) {
                FwEventAnnotation annotation = FwEventAnnotation.class.cast(xannotation);
                if (annotation != null) {
                    return FwEventListenerDestination.buildDestination(target, method, annotation.thread(), annotation.order());
                }else{
                    return FwEventListenerDestination.buildDestination(target, method, null, null);
                }
            }
        };
    }

    // Build the destination by interface class and method
    public static FwEvent.FwEventDestination buildDestination(Class<?> clazz, Method method, Object target) {
        try {
            Method targetMethod = target.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            Annotation annotation = FwEventListenerDestination.BUILDER.annotation(targetMethod);

            if (annotation == null) {
                annotation = FwEventListenerDestination.BUILDER.annotation(method);
            }

            FwEvent.FwEventDestination destination = null;
            if (annotation != null) {
                destination = FwEventListenerDestination.BUILDER.buildDestination(target, targetMethod, annotation);
            } else {
                destination = FwEventListenerDestination.BUILDER.buildDestination(target, targetMethod);
            }

            return destination;

        } catch(Exception e) {

        }

        return null;
    }

    public static void subscrible(Class<?> clazz, Object target) {
        // TODO: Check the interface is implemented by target
        // Object clazzTarget = clazz.cast(target);

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {

            FwEvent.FwEventDestination destination = buildDestination(clazz, method, target);

            if (destination != null) {
                FwEvent.dispatcher.addBinding(eventKey(clazz, method), target, destination);
            }
        }
    }

    public static  void unsubscrible(Class<?> clazz, Object target) {
        // TODO: Check the interface is implemented by target
        // Object clazzTarget = clazz.cast(target);

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            FwEvent.FwEventDestination destination = buildDestination(clazz, method, target);
            if (destination != null) {
                FwEvent.dispatcher.removeBinding(eventKey(clazz, method), target, destination);
            }
        }
    }
}


