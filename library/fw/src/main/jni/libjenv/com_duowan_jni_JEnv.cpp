#include "com_duowan_jni_JEnv.h"
#include "../include/log.h"
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <sys/utsname.h>

#include <sys/vt.h>
#include <linux/fb.h>

// declares
#define jb_error (-1)
int find_nth_space(char *search_buffer, int   space_ordinality );
int read_env_stat(long *jiffies_per_second, long long *boot_time_since_epoch );
int read_state(const char *proc_buf, char *stat_buf, int stat_buf_len );

long gJiffies_per_second = 0;
long long gBoot_time_since_epoch = 0;
/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getEnvironment
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_duowan_jni_JEnv_getEnvironment
  (JNIEnv *env, jclass obj_class){
	char      stat_buf[2048];
	int		  read_result;

	long jiffies_per_second;
	long long boot_time_since_epoch;
	read_result = read_env_stat(&jiffies_per_second, &boot_time_since_epoch);
	if(read_result != jb_error)
	{
		read_result = sprintf(stat_buf, "env: %s, jiffies_per_second: %ld, boot_time_since_epoch: %llu",
				"duowan-env-jb", jiffies_per_second, boot_time_since_epoch);
		stat_buf[read_result] = 0;
	}
	jstring rtstr = env->NewStringUTF(stat_buf);//(*env)->NewStringUTF(env, stat_buf);
    return rtstr;
}

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getCryptSeed
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_duowan_jni_JEnv_getCryptSeed
  (JNIEnv *env, jclass obj_class){
	jstring rtstr = env->NewStringUTF("duowan-env-jb-cryptkey-2013@copyright");
    return rtstr;
}

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getProcessRawStatString
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_duowan_jni_JEnv_getProcessRawStatString
  (JNIEnv *env, jclass obj_class, jlong jpid){

	char      proc_buf[80];
	char      stat_buf[3048];
	int		  read_result;
	long	  pid = jpid;

	sprintf(proc_buf,"/proc/%ld/stat", pid);
	read_result = read_state(proc_buf, stat_buf, sizeof(stat_buf));
	if(read_result != jb_error)
	{
		stat_buf[read_result] = 0;
		jstring rtstr = env->NewStringUTF(stat_buf);
	    return rtstr;
	}
	jstring rtstr = env->NewStringUTF("");
    return rtstr;
}


/*
 * Class:     com_duowan_jni_JEnv
 * Method:    getProcessStartTimeSinceEpoch
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_duowan_jni_JEnv_getProcessStartTimeSinceEpoch
  (JNIEnv *env, jclass obj_class, jlong jpid){
	int		  read_result;
	int       field_begin;
	char      proc_buf[80];
	char      stat_buf[3048];

    struct tm gm_buf;
    struct tm local_buf;

    long long process_start_time_since_boot;

    long pid = jpid;

    time_t    process_start_time_since_epoch;

	if(gJiffies_per_second == 0){
		read_result = read_env_stat(&gJiffies_per_second, &gBoot_time_since_epoch);
		if(read_result == jb_error)
		{
			return 0;
		}
	}

	sprintf(proc_buf,"/proc/%ld/stat", pid);
	read_result = read_state(proc_buf, stat_buf, sizeof(stat_buf));
	if(read_result == jb_error)
	{
	    return 0;
	}

	stat_buf[read_result] = 0;

	field_begin=find_nth_space(stat_buf,21)+1;
    stat_buf[find_nth_space(stat_buf,22)]=0;

    sscanf(stat_buf+field_begin,"%llu", &process_start_time_since_boot);
    process_start_time_since_epoch =
    gBoot_time_since_epoch+process_start_time_since_boot/gJiffies_per_second;

    localtime_r(&process_start_time_since_epoch,&local_buf);
    gmtime_r   (&process_start_time_since_epoch,&gm_buf   );

    LOGE("process id: %ld", pid);
    LOGE("process_start_time_since_epoch: %ld", process_start_time_since_epoch);

    return (jlong)((long)(process_start_time_since_epoch));
}

/*
 * Class:     com_duowan_jni_JEnv
 * Method:    setupJni
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_duowan_jni_JEnv_setupJni
  (JNIEnv *env, jclass obj_class, jstring apkDir, jstring params, jobject jassetMgr){
	// get the utf-8 buffer
	const char *buffer = env->GetStringUTFChars(apkDir, 0);

	// release the buffer
	env->ReleaseStringUTFChars(apkDir, buffer );
}

int
find_nth_space(char *search_buffer,
               int   space_ordinality
              )
{
  int jndex;
  int space_count;

  space_count=0;

  for(jndex=0;
      search_buffer[jndex];
      jndex++
     )
  {
    if(search_buffer[jndex]==' ')
    {
      space_count++;

      if(space_count>=space_ordinality)
      {
        return jndex;
      }
    }
  }

  fprintf(stderr,"looking for too many spaces\n");

  return 1;
} /* find_nth_space() */


int
read_state(const char *proc_buf,
		char *stat_buf,
		int stat_buf_len
		)
{
	int stat_fd;
	ssize_t read_result;

	stat_fd=open(proc_buf,O_RDONLY);
	if(stat_fd<0)
    {
      fprintf(stderr,"open() fail\n");
      LOGE("0. open() fail");

      goto err;
    }

	read_result=read(stat_fd, stat_buf, stat_buf_len);

    if(read_result<0)
    {
      fprintf(stderr,"read() fail\n");
      LOGE("1. read() fail");

      goto err;
    }

    if(read_result>=stat_buf_len)
    {
      fprintf(stderr,"stat_buf is too small\n");

      LOGE("2. stat_buf is too small: result(%d), stat_buf(%d)", read_result, stat_buf_len);
      goto err;
    }

    close(stat_fd);
    return read_result;

err:
	close(stat_fd);
	return jb_error;
}

int
read_env_stat(long *jiffies_per_second,
		long long *boot_time_since_epoch
		)
{
	int       field_begin;
	char      proc_buf[80];
    char      stat_buf[4048];

    *jiffies_per_second=sysconf(_SC_CLK_TCK);

    int read_result = read_state("/proc/stat", stat_buf, sizeof(stat_buf));
    if(read_result != jb_error)
    {
    	field_begin=strstr(stat_buf,"btime ")-stat_buf+6;

	    sscanf(stat_buf+field_begin,"%llu", boot_time_since_epoch);

	    return 0;
    }

    return jb_error;
}

/**
 * screen shot
 * */
#include "screenshot.h"
/*
 * Class:     com_duowan_jni_JEnv
 * Method:    taskScreenShoot
 * Signature: (Lcom/duowan/jni/JEnv/ScreenShoot;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_duowan_jni_JEnv_taskScreenShoot
  (JNIEnv *env, jclass targetClass, jobject obj){

	LOGE("in takescreen 0");
	jclass screenShootClass = env->GetObjectClass(obj);
	LOGE("in takescreen 1");
	jfieldID xres = env->GetFieldID(screenShootClass, "xres", "I");
	LOGE("in takescreen 2");
	jfieldID yres = env->GetFieldID(screenShootClass, "yres", "I");
	LOGE("in takescreen 3");
	jfieldID bps = env->GetFieldID(screenShootClass, "bps", "I");
	LOGE("in takescreen 4");
	jfieldID gray = env->GetFieldID(screenShootClass, "gray", "I");
	LOGE("in takescreen 5");
	jfieldID bytes = env->GetFieldID(screenShootClass, "bytes", "[B");
	LOGE("in takescreen 6");
	struct picture pic = {0};
	LOGE("in takescreen 7");
	TakeScreenshot(_display_device, &pic);
	LOGE("in takescreen 8");
	if(xres){
		env->SetIntField(obj, xres, pic.xres);
	}
	LOGE("in takescreen 9");
	if(yres){
		env->SetIntField(obj, yres, pic.yres);
	}
	LOGE("in takescreen 10");
	if(bps){
		env->SetIntField(obj, bps, pic.bps);
	}
	LOGE("in takescreen 11");
	if(gray){
		env->SetIntField(obj, gray, pic.gray);
	}
	LOGE("in takescreen 12");
	if(bytes){
		int len = pic.xres * pic.yres * pic.bps/8;
		LOGE("in takescreen 12-0");
		jbyteArray byteArray = env->NewByteArray(len);
		LOGE("in takescreen 12-1");
		env->SetByteArrayRegion(byteArray, 0, len, (const jbyte*)(pic.buffer));
		LOGE("in takescreen 12-2");
		env->SetObjectField(obj, bytes, byteArray);
		LOGE("in takescreen 12-3");
		env->DeleteLocalRef(byteArray);
		LOGE("in takescreen 12-4");
	}
	LOGE("in takescreen 13");

	pic.free();
	LOGE("in takescreen 14");
	return true;
}
