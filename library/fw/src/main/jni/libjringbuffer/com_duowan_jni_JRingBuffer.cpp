#include "com_duowan_jni_JRingBuffer.h"
#include "com_duowan_jni_JRingBuffer_JRBFlag.h"

#include <jni.h>
#include "ringbuffer.h"
#include "../include/log.h"

typedef union {
    iringbuffer rb; // char *
    jlong handle;
}RBH;

#define __RBH RBH rbh = { .handle=handle }

/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_alloc
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_com_duowan_jni_JRingBuffer_irb_1alloc
  (JNIEnv *, jclass, jlong capacity, jint flag) {
      RBH rbh = { irb_alloc(capacity, flag) };
      return rbh.handle;
  }

/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_duowan_jni_JRingBuffer_irb_1free
  (JNIEnv *, jclass, jlong handle) {
      __RBH;
      irb_free(rbh.rb);
      return ;
  }


/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_duowan_jni_JRingBuffer_irb_1close
  (JNIEnv *, jclass, jlong handle) {
      __RBH;
      irb_close(rbh.rb);
  }



/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_shutdown
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_duowan_jni_JRingBuffer_irb_1shutdown
  (JNIEnv *, jclass, jlong handle, jint flag) {
      __RBH;
      irb_shutdown(rbh.rb, flag);
  }


/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_write
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_com_duowan_jni_JRingBuffer_irb_1write
  (JNIEnv *env, jclass, jlong handle, jbyteArray bytes) {
      __RBH;
      jint wlen = 0;
      jbyte *b = (jbyte *)env->GetByteArrayElements(bytes, 0);
      jsize s = env->GetArrayLength(bytes);

      wlen = irb_write(rbh.rb, (const char*)b, (int)s);

      env->ReleaseByteArrayElements(bytes, b, 0);

      return wlen;
  }

/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_read
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_com_duowan_jni_JRingBuffer_irb_1read
  (JNIEnv *env, jclass, jlong handle, jbyteArray bytes) {
      __RBH;
      jint rlen = 0;
      jbyte *b = (jbyte *)env->GetByteArrayElements(bytes, 0);
      jsize s = env->GetArrayLength(bytes);

      if (s) {
          rlen = irb_read(rbh.rb, (char*)b, (int)s);
      }

      env->ReleaseByteArrayElements(bytes, b, 0);

      return rlen;
  }


/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_ready
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_duowan_jni_JRingBuffer_irb_1ready
  (JNIEnv *, jclass, jlong handle) {
      __RBH;
      return irb_ready(rbh.rb);
  }

/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_getmicros
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_duowan_jni_JRingBuffer_irb_1getmicros
  (JNIEnv *, jclass) {
      int64_t micros = irb_getmicros();
      jlong jmicros = micros; 
      LOGE("[Java_com_duowan_jni_JRingBuffer_irb_1getmicros]""%lld(%d) - %lld(%d)\n", 
              micros, sizeof(int64_t), 
              jmicros, sizeof(jlong));
      return jmicros;
  }

/*
 * Class:     com_duowan_jni_JRingBuffer
 * Method:    irb_getmillis
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_duowan_jni_JRingBuffer_irb_1getmillis
  (JNIEnv *, jclass) {
      int64_t micros = irb_getmillis();
      jlong jmicros = micros; 
      LOGE("[Java_com_duowan_jni_JRingBuffer_irb_1getmillis]""%lld - %lld\n", micros, jmicros);

      return irb_getmillis();
  }


