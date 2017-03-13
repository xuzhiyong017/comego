LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# This is the target being built.
LOCAL_MODULE:= jbsdiff


# All of the source files that we will compile.
LOCAL_SRC_FILES:=  diff_patch.c


# No static libraries.
LOCAL_STATIC_LIBRARIES := \
     libjbz
     
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)