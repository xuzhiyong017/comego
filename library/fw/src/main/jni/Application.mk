STLPORT_FORCE_REBUILD := false
APP_STL := stlport_static
APP_CPPFLAGS += -fno-exceptions
APP_CPPFLAGS += -fno-rtti
GLOBAL_CFLAGS =   -fvisibility=hidden
APP_ABI := armeabi armeabi-v7a x86 
APP_MODULES      := libjenv 
APP_MODULES      += libt9search
APP_MODULES		 += libjbsdiff
APP_MODULES		 += libjringbuffer
APP_MODULES		 += libjsmartradix
