LOCAL_PATH := $(call my-dir)

# ---------------------------------------------------------------------------
# lib env
include $(CLEAR_VARS)

LOCAL_MODULE := libjenv 
LOCAL_JNI_SHARED_LIBRARIES := libjenv
LOCAL_REQUIRED_MODULES := libjenv

#-Wno-psabi to remove warning about GCC 4.4 va_list warning
LOCAL_CFLAGS := -DANDROID_NDK -Wno-psabi

LOCAL_DEFAULT_CPP_EXTENSION := cpp 

LOCAL_SRC_FILES := com_duowan_jni_JEnv.cpp \
		screenshot.cpp

LOCAL_LDLIBS := -llog -lz -lm

include $(BUILD_SHARED_LIBRARY)

