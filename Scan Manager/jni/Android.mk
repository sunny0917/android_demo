LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := n431xutils
LOCAL_SRC_FILES := n431x_utils.c\
				   cli_scan.c\
	
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
#LOCAL_LDFLAGS += -fuse-ld=bfd
  
LOCAL_MODULE_TAGS := optional
include $(BUILD_SHARED_LIBRARY)