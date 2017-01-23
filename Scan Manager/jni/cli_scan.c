#include <jni.h>
//#include "n431x_utils.h"
#include "debug.h"

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_openPort(JNIEnv *env, jclass jc,jstring path,jint baudrate)
{
	jboolean iscopy;
	const char* path_sv = (*env)->GetStringUTFChars(env,path, &iscopy);
	LOGI("scan open fd %s",path_sv);
	jint ret = cilico_scan_open_fd(path_sv, baudrate);
	//LOGI("scan open fd = %d",ret);
	return ret;
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_closePort(JNIEnv *env, jclass jc)
{
	return cilico_scan_close_fd();
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_honywareTypeOnOff(JNIEnv *env, jclass jc,jstring bar_type,jint enable)
{
	jboolean iscopy;
	const char* type = (*env)->GetStringUTFChars(env,bar_type, &iscopy);

	jint ret = cilico_scan_open_fd("/dev/ttyMT1",9600);
	if(ret < 0) {
		LOGE("open serial error:ret = %d",ret);
		return -1;
	}
	LOGI("Scan honywareTypeOnOff type = %s,enable = %d",type,enable);
	honywareTypeOnOff(type,enable);
	cilico_scan_close_fd();
	return 0;
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_dohonywareset(JNIEnv *env, jclass jc)
{
	LOGI("Scan honywarereset");
	return dohonywareset();
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_honywareSetPre(JNIEnv *env, jclass jc)
{
	LOGI("Scan honywareSetPre");
	return honywareSetPre();
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_honywareSetSuf(JNIEnv *env, jclass jc)
{
	LOGI("Scan honywareSetSuf");
	return honywareSetSuf();
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_honywellClearPre(JNIEnv *env, jclass jc)
{
	LOGI("Scan honywareSetPre");
	return honywellClearPre();
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_honywellClearSuf(JNIEnv *env, jclass jc)
{
	LOGI("Scan honywareSetSuf");
	return honywellClearSuf();
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_writePort(JNIEnv *env, jclass jc,jbyteArray array)
{
	LOGI("Scan writePort");
	jbyte * buf_array = (jbyte*)(*env)->GetByteArrayElements(env,array, 0);
    jsize  size = (*env)->GetArrayLength(env,array);
    char * buf = (char*)buf_array;
    int len = (int)size;
	return writePort((char *)buf,len);
}

JNIEXPORT jint JNICALL Java_android_hardware_utils_Scan_setSymbologyConfig(JNIEnv *env, jclass jc,jobject obj)
{
	jfieldID symId,flag,minLen,maxLen,prefix,suffix,upcToEan,strPre,strSuf;
	jclass cls = (*env)->GetObjectClass(env,obj);
	jint ret = cilico_scan_open_fd("/dev/ttyMT1",9600);
	if(ret < 0) {
		LOGE("open serial error:ret = %d",ret);
		return -1;
	}
	symId = (*env)->GetFieldID(env,cls,"symID","I");
	flag = (*env)->GetFieldID(env,cls,"Flags","I");
	minLen = (*env)->GetFieldID(env,cls,"MinLength","I");
	maxLen = (*env)->GetFieldID(env,cls,"MaxLength","I");
	prefix = (*env)->GetFieldID(env,cls,"Prefix","I");
	suffix = (*env)->GetFieldID(env,cls,"Suffix","I");
	upcToEan = (*env)->GetFieldID(env,cls,"upcToEan","I");
	strPre = (*env)->GetFieldID(env,cls,"StrPrefix","Ljava/lang/String;");
	strSuf = (*env)->GetFieldID(env,cls,"StrSuffix","Ljava/lang/String;");

	jint jid = (jint)(*env)->GetIntField(env,obj,symId);
	jint jflag = (jint)(*env)->GetIntField(env,obj,flag);
	jint jminLen = (jint)(*env)->GetIntField(env,obj,minLen);
	jint jmaxLen = (jint)(*env)->GetIntField(env,obj,maxLen);
	jint jprefix = (jint)(*env)->GetIntField(env,obj,prefix);
	jint jsuffix = (jint)(*env)->GetIntField(env,obj,suffix);
	jint jupcToEan = (jint)(*env)->GetIntField(env,obj,upcToEan);
	jstring jstrPre = (jstring)(*env)->GetObjectField(env,obj, strPre);
	jstring jstrSuf = (jstring)(*env)->GetObjectField(env,obj, strSuf);

	char* str_pre=(char*) (*env)->GetStringUTFChars(env,jstrPre,JNI_FALSE);
	char* str_suf=(char*) (*env)->GetStringUTFChars(env,jstrSuf,JNI_FALSE);
	//cilico_scan_open_fd("/dev/ttyMT1",9600);
	LOGI("symID=%02x,flag=%02x,min=%02x,max=%02x,pre=%02x,suf=%02x,jupcToEan=%02x,strpre=%s,strsuf=%s",jid,jflag,jminLen,jmaxLen,jprefix,jsuffix,jupcToEan,str_pre,str_suf);
	setSymbologyConfig(jid,jflag,jminLen,jmaxLen,jprefix,jsuffix,jupcToEan,str_pre,str_suf);
	//setSymbologyConfig(jid,jflag,jminLen,jmaxLen,jprefix,jsuffix,jupcToEan);
	(*env)->ReleaseStringUTFChars(env,jstrPre,str_pre);
	(*env)->ReleaseStringUTFChars(env,jstrSuf,str_suf);
	cilico_scan_close_fd();
	return 0;

}
