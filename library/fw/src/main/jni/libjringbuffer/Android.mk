LOCAL_PATH := $(call my-dir)

# ---------------------------------------------------------------------------
# lib jringbuffer
include $(CLEAR_VARS)

LOCAL_MODULE := libjringbuffer 
LOCAL_JNI_SHARED_LIBRARIES := libjringbuffer
LOCAL_REQUIRED_MODULES := libjringbuffer

#-Wno-psabi to remove warning about GCC 4.4 va_list warning
LOCAL_CFLAGS := -DANDROID_NDK -Wno-psabi

LOCAL_DEFAULT_CPP_EXTENSION := cpp 

LOCAL_SRC_FILES := com_duowan_jni_JRingBuffer.cpp \
		ringbuffer.c

LOCAL_LDLIBS := -llog -lz -lm

include $(BUILD_SHARED_LIBRARY)

