/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_duowan_jni_JEnv */

#ifndef _Included_com_duowan_jni_JEnv
#define _Included_com_duowan_jni_JEnv
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getEnvironment
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_duowan_jni_JEnv_getEnvironment
  (JNIEnv *, jclass);

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getCryptSeed
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_duowan_jni_JEnv_getCryptSeed
  (JNIEnv *, jclass);

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getProcessRawStatString
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_duowan_jni_JEnv_getProcessRawStatString
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getProcessStartTimeSinceEpoch
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_duowan_jni_JEnv_getProcessStartTimeSinceEpoch
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    setupJni
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_duowan_jni_JEnv_setupJni
  (JNIEnv *, jclass, jstring, jstring, jobject);

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    taskScreenShoot
 * Signature: (Lcom/duowan/jni/JEnv/ScreenShoot;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_duowan_jni_JEnv_taskScreenShoot
  (JNIEnv *, jclass, jobject);

#ifdef __cplusplus
}
#endif
#endif