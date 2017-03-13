LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := jbz

LOCAL_SRC_FILES := \
				blocksort.c \
				bzip2.c \
				bzlib.c \
				compress.c \
				crctable.c \
				decompress.c \
				huffman.c \
				randtable.c 

include $(BUILD_STATIC_LIBRARY)