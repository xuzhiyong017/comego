LOCAL_PATH := $(call my-dir)  
  
include $(CLEAR_VARS)  
  
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog -lEGL
LOCAL_MODULE    := inject_so 
LOCAL_SRC_FILES := inject_so.c  
include $(BUILD_SHARED_LIBRARY)  