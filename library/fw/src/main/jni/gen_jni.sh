#!/bin/sh

xclasspath=./../../../build/intermediates/classes/debug
xjnipath=./../java/com/duowan/jni

if [ ! -d ${xclasspath}  ]; then
    mkdir ${xclasspath}
fi

echo "gen native code: com.duowan.jni.JEnv.java"
javac -d ${xclasspath} ${xjnipath}/JEnv.java
javah -classpath ${xclasspath} -d ./libjenv -jni com.duowan.jni.JEnv

echo "gen native code: com.duowan.jni.JBsDiff.java"
javac -d ${xclasspath} ${xjnipath}/JBsDiff.java
javah -classpath ${xclasspath} -d ./libjbsdiff -jni com.duowan.jni.JBsDiff

echo "gen native code: com.duowan.jni.JRingBuffer.java"
javac -d ${xclasspath} ${xjnipath}/JRingBuffer.java
javah -classpath ${xclasspath} -d ./libjringbuffer -jni com.duowan.jni.JRingBuffer

echo "gen native code: com.duowan.jni.JSmartRadix"
javac -d ${xclasspath} ${xjnipath}/JSmartRadix.java
javah -classpath ${xclasspath} -d ./libjsmartradix -jni com.duowan.jni.JSmartRadix
