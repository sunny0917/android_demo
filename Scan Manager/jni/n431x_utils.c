/************************************************************
* Copyright (c)  200X- 200X  Olc Corp.ltd.，
* VENDOR_EDIT     (VENDOR_EDIT 须有)
* Description: Source file for Honyware Scanner.
*           To control some scan type on/off.
* Version   : 1.0
* Date      : 2016-8-18
* Author    : hanmeng@cilico.com
* ---------------------------------- Revision History: ----------------------------------
*  	<version>	     <date>		< author >	   <desc>
*
*    Modified to be suitable to the new coding rules in all functions.
*************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/time.h>
#include <sys/stat.h>
#include <termios.h>
#include <sys/types.h>

#include "debug.h"
#include "n431x_utils.h"

#define SERIAL_PATH	"/dev/ttyMT1"

//#include <utils/Log.h>
#if 1
int getBaudrate(int baudrate) {
	switch (baudrate) {
	case 0:
		return B0;
	case 50:
		return B50;
	case 75:
		return B75;
	case 110:
		return B110;
	case 134:
		return B134;
	case 150:
		return B150;
	case 200:
		return B200;
	case 300:
		return B300;
	case 600:
		return B600;
	case 1200:
		return B1200;
	case 1800:
		return B1800;
	case 2400:
		return B2400;
	case 4800:
		return B4800;
	case 9600:
		return B9600;
	case 19200:
		return B19200;
	case 38400:
		return B38400;
	case 57600:
		return B57600;
	case 115200:
		return B115200;
	case 230400:
		return B230400;
	case 460800:
		return B460800;
	case 500000:
		return B500000;
	case 576000:
		return B576000;
	case 921600:
		return B921600;
	case 1000000:
		return B1000000;
	case 1152000:
		return B1152000;
	case 1500000:
		return B1500000;
	case 2000000:
		return B2000000;
	case 2500000:
		return B2500000;
	case 3000000:
		return B3000000;
	case 3500000:
		return B3500000;
	case 4000000:
		return B4000000;
	default:
		return -1;
	}
}

int cilico_scan_open_fd(const char* path, int baudrate) {
	//int fd;
	speed_t speed;
	int flags = 0;
	LOGE("------------------cilico_scan_open_fd------------");

	/* Check arguments */
	{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			/* TODO: throw an exception */
			LOGE("Invalid baudrate");
			return -1;
		}
	}

	/* Opening device */
	{
		//fd = open(path, O_RDWR | O_DIRECT | O_SYNC);
		//fd = open(path, O_RDWR | O_NONBLOCK);
		fd = open(path,O_RDWR|O_NOCTTY);
		LOGI("open(%s) fd = %d", path,fd);
		if (fd == -1) {
			/* Throw an exception */
			LOGE("Cannot open port");
			/* TODO: throw an exception */
			return -1;
		}
	}
	/* Configure device */
	{
		struct termios cfg;
		cfmakeraw(&cfg);

		LOGI("Configuring serial port");
		if (tcgetattr(fd, &cfg)) {
			LOGE("tcgetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return -1;
		}

		LOGE("c_cflag old = 0x%X", cfg.c_cflag);

		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);
#if 1
		cfg.c_cflag |= CLOCAL;
		//cfg.c_cflag |= CREAD;

		//cfg.c_cflag &= ~CLOCAL;
		cfg.c_cflag &= ~CREAD;

		//校验 停止位
		cfg.c_cflag &= ~PARENB;
		cfg.c_cflag &= ~CSTOPB;

		//硬件流控
		cfg.c_cflag &= ~CRTSCTS;
		//cfg.c_cflag |= CRTSCTS;

		//数据位
		cfg.c_cflag &= ~CSIZE;
		cfg.c_cflag |= CS8;

		LOGE("c_cflag new = 0x%X", cfg.c_cflag);

		//软件流控
		cfg.c_iflag &= ~(IXON | IXOFF | IXANY);

		//disable cr lf swape
		cfg.c_iflag &= ~(INLCR | ICRNL | IGNCR);
		//cfg.c_oflag &= ~(ONLCR | OCRNL);
		cfg.c_oflag = 0;
		//直接发送
		cfg.c_lflag &= ~ (ICANON | ECHO | ECHOE | ISIG);
		//cfg.c_lflag = 0;
#endif

		if (tcsetattr(fd, TCSANOW, &cfg)) {
			LOGE("tcsetattr() failed");
			close(fd);
			return -1;
		}
	    ioctl(fd, TIOCMGET, &flags);
	    fprintf(stderr, "Flags are %x.\n", flags);
		flags &= ~TIOCM_RTS;
		ioctl(fd, TIOCMSET, &flags);
		fprintf(stderr, "Setting %x.\n", flags);

	}

	return fd;
}

int cilico_scan_close_fd() {
	close(fd);
	fd = -1;
	LOGE("cilico_scan_close_fd---------------");
	return 0;
}
#endif

int openPort()
{
	int ret=0;
	fd=open("/dev/ttyMT1",O_RDWR|O_NOCTTY);
	struct termios term;

	tcgetattr(fd,&term);
	cfsetispeed(&term,B9600);
	cfsetospeed(&term,B9600);
	term.c_cc[VTIME]    = 1;   /* inter-character timer unused */
	term.c_cc[VMIN]     = 0;   /* blocking read until 1 chars received */

	if(tcsetattr(fd,TCSANOW,&term)!=0)
	{
		  if(fd>0)
		  close(fd);
		 ret=-1;
		 goto End;
	}
	tcflush(fd,TCIOFLUSH);
 End:
	return ret;
}

int writePort(char *buf,int len)
{
	int ret = -1;
	int i = 0;
#if 0
	ret = cilico_scan_open_fd(SERIAL_PATH,9600);
	if(ret < 0) {
		LOGE("open serial error:ret = %d",ret);
		return -1;
	}

	int fp;
	fp=open("/dev/ttyMT1",O_RDWR|O_NOCTTY);
	struct termios term;

	tcgetattr(fp,&term);
	cfsetispeed(&term,B9600);
	cfsetospeed(&term,B9600);
	term.c_cc[VTIME]    = 1;   /* inter-character timer unused */
	term.c_cc[VMIN]     = 0;   /* blocking read until 1 chars received */

	if(tcsetattr(fp,TCSANOW,&term)!=0)
	{
		  if(fp>0)
		  close(fp);
		 ret=-1;
		 return ret;
	}
	tcflush(fp,TCIOFLUSH);
#endif

	for(;i<len;i++)
		LOGE("writePort %02x",buf[i]);
	ret = write(fd, buf, len);
	if (ret != len) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	usleep(50*1000);
	LOGE("writePort write success ret = %d",ret);
	//cilico_scan_close_fd();
	//close(fp);
	return 0;
}

int honywareSetPre()
{
	int ret = -1;

	ret = writePort(buf1, sizeof(buf1));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf2, sizeof(buf2));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf3, sizeof(buf3));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf4, sizeof(buf4));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}

	return 0;
}

int honywareSetSuf()
{
	int ret = -1;

	ret = writePort(buf11, sizeof(buf11));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf12, sizeof(buf12));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf13, sizeof(buf13));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf14, sizeof(buf14));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return 0;
}

int honywellClearPre()
{
	int ret = -1;

	ret = writePort(Clear_All_prefix, sizeof(Clear_All_prefix));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return 0;
}

int honywellClearSuf()
{
	int ret = -1;
	ret = writePort(Clear_All_suffix, sizeof(Clear_All_suffix));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return 0;
}

int honywareTypeOnOff(const char* type,int enable)
{
	int ret = 0;
	int i = 0;
	char code_cmd[15];
	LOGE("before open the fd = %d",fd);
	//ret = cilico_scan_open_fd(SERIAL_PATH,9600);
	/*ret = openPort();
	if(ret < 0) {
		LOGE("open serial error:ret = %d",ret);
		return -1;
	}*/
#if 0
	int fd;
	fd=open("/dev/ttyMT1",O_RDWR|O_NOCTTY);
	struct termios term;

	tcgetattr(fd,&term);
	cfsetispeed(&term,B9600);
	cfsetospeed(&term,B9600);
	term.c_cc[VTIME]    = 1;   /* inter-character timer unused */
	term.c_cc[VMIN]     = 0;   /* blocking read until 1 chars received */

	if(tcsetattr(fd,TCSANOW,&term)!=0)
	{
		  if(fd>0)
		  close(fd);
		 ret=-1;
		 return ret;
	}
	tcflush(fd,TCIOFLUSH);
#endif

	memset(code_cmd,0,sizeof(code_cmd));
	LOGE("honywareTypeOnOff	start");
	//fd = open("/dev/ttyMT1", O_RDWR | O_SYNC);
	//LOGE("honywareTypeOnOff open serial success fd = %d",fd);
	if(type == NULL)
	{
		printf("Code type error is null\n");
		close(fd);
		return -1;
	}

	if(!strncmp(type,"Code 39",strlen("Code 39")))
	{
		if(enable)
		{
			strncpy(code_cmd,Code_39On,sizeof(Code_39On));
		}else {
			strncpy(code_cmd,Code_39Off,sizeof(Code_39Off));
		}
	}else if(!strncmp(type,"Code 128",strlen("Code 128"))) {
		if(enable)
		{
			strncpy(code_cmd,Code_128On,sizeof(Code_128On));
		}else {
			strncpy(code_cmd,Code_128Off,sizeof(Code_128Off));
		}
	}else if(!strncmp(type,"Interleaved 2 of 5",strlen("Interleaved 2 of 5"))) {
		if(enable)
		{
			strncpy(code_cmd,Interleaved_2of5On,sizeof(Interleaved_2of5On));
		}else {
			strncpy(code_cmd,Interleaved_2of5Off,sizeof(Interleaved_2of5Off));
		}
	}else if(!strncmp(type,"GS1-128",strlen("GS1-128"))) {
		if(enable)
		{
			strncpy(code_cmd,GS1_128On,sizeof(GS1_128On));
		}else {
			strncpy(code_cmd,GS1_128Off,sizeof(GS1_128Off));
		}
	}else if(!strncmp(type,"EAN-13",strlen("EAN-13"))) {
		if(enable)
		{
			strncpy(code_cmd,EAN_13On,sizeof(EAN_13On));
		}else {
			strncpy(code_cmd,EAN_13Off,sizeof(EAN_13Off));
		}
	}else if(!strncmp(type,"PDF417",strlen("PDF417"))) {
		if(enable)
		{
			strncpy(code_cmd,PDF417_On,sizeof(PDF417_On));
		}else {
			strncpy(code_cmd,PDF417_Off,sizeof(PDF417_Off));
		}
	}else if(!strncmp(type,"MacroPDF417",strlen("MacroPDF417"))){
		if(enable) {
			strncpy(code_cmd,MacroPDF417_On,sizeof(MacroPDF417_On));
		}else{
			strncpy(code_cmd,MacroPDF417_Off,sizeof(MacroPDF417_Off));
		}
	}else if(!strncmp(type,"MicroPDF417",strlen("MicroPDF417"))){
		if(enable) {
			strncpy(code_cmd,MicroPDF417_On,sizeof(MicroPDF417_On));
		}else{
			strncpy(code_cmd,MicroPDF417_Off,sizeof(MicroPDF417_Off));
		}
	}else if(!strncmp(type,"QR Code",strlen("QR Code"))){
		if(enable) {
			strncpy(code_cmd,QR_CODE_On,sizeof(QR_CODE_On));
		}else{
			strncpy(code_cmd,QR_CODE_Off,sizeof(QR_CODE_Off));
		}
	}else {
		printf("bar code type is not support\n");
		return -1;
	}
	//for(;i<strlen(code_cmd);i++)
		//LOGE("%02x",code_cmd[i]);
	if(strlen(code_cmd))
	{
		//ret = write(fd, code_cmd, strlen(code_cmd));
		ret = writePort(code_cmd,strlen(code_cmd));
		if (ret != 0) {
			printf("write uart error %d\n", errno);
			//close(fd);
			return -1;
		}
		LOGE("honywareTypeOnOff write success ret = %d",ret);
		//close(fd);
		return 0;
	}else {
		LOGE("honywareTypeOnOff code_cmd is null");
		//close(fd);
		return -1;
	}
}


char valueToHexCh(const int value)
{
  char result = '\0';
  if(value >= 0 && value <= 9){
    result = (char)(value + 48);
  }
  else if(value >= 10 && value <= 15){
    result = (char)(value - 10 + 65);
  }
  else{
    ;
  }

  return result;
}
int strToHex(char *ch, char *hex)
{
  int high,low;
  int tmp = 0;
  if(ch == NULL || hex == NULL){
    return -1;
  }

  if(strlen(ch) == 0){
	  *hex = '\0';
    return -2;
  }

  while(*ch){
    tmp = (int)*ch;
    high = tmp >> 4;
    low = tmp & 15;
    *hex++ = valueToHexCh(high);
    *hex++ = valueToHexCh(low);
    ch++;
  }

  *hex = '\0';
  return 0;
}
int strToAscii(char *result,char *src)
{
	//char *result;
	int i;
	//int len = strlen(src);
	//result = malloc(len*2);

	strToHex(src,result);

	LOGE("src  %s %d",src,strlen(src));
	LOGE("result %s %d",result,strlen(result));

	return 0;//result;
}

int setPrefixSuffix(char *result,char *begin,int len,char *str)
{

	LOGE("len=%d",strlen(result));
	memcpy(result,begin,len);
	LOGE("len=%d  str=%d",strlen(result),strlen(str));
	memcpy(result+len,str,strlen(str));
	LOGE("len=%d",strlen(result));
	memcpy(result+len+strlen(str),"02.",strlen("02."));
	LOGE("len=%d",strlen(result));
	return 0;
}

int setSymbologyConfig(int symId,int flag,int minLen,int maxLen,int prefix,int suffix,int upcToEan,char *strPre,char *strSuf)
{
	int ret = -1;
	//cilico_scan_open_fd(SERIAL_PATH,9600);
	LOGE("pre =%s,suf=%s",strPre,strSuf);
	char *m_strPre=NULL,*m_strSuf=NULL,*m_addpre=NULL,*m_addsuf=NULL;

	//if(strlen(strPre)) {
		m_strPre = (char *)malloc(strlen(strPre)*2+1);
		memset(m_strPre,'\0',strlen(strPre)*2+1);
		strToAscii(m_strPre,strPre);
		LOGE("m_strPre=%s,len = %d",m_strPre,strlen(m_strPre));
	//}
	//if(strlen(strSuf)) {
		m_strSuf = (char *)malloc(strlen(strSuf)*2+1);
		memset(m_strSuf,'\0',strlen(strSuf)*2+1);
		strToAscii(m_strSuf,strSuf);
		LOGE("m_strSuf=%s,len = %d,pre len = %d",m_strSuf,strlen(m_strSuf),strlen(m_strPre));
	//}
	switch(symId){
	case 0x01://code 39
		switch(flag){
		case 0x00://no check
			ret = writePort(Code_39NCC, sizeof(Code_39NCC));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
			break;
		case 0x01://validate,don't transmit
			ret = writePort(Code_39VDT, sizeof(Code_39VDT));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
			break;
		case 0x02://validate and transmit
			break;
			ret = writePort(Code_39VT, sizeof(Code_39VT));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		default:
			break;
		}
		//if(minLen == 0x01) {
			Code_39MinLen[9] = minLen/10+48;
			Code_39MinLen[10] = minLen%10+48;
			ret = writePort(Code_39MinLen, sizeof(Code_39MinLen));
			LOGE("set min");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
		//if(maxLen == 0x01) {
			Code_39MaxLen[9] = maxLen/10+48;
			Code_39MaxLen[10] = maxLen%10+48;
			ret = writePort(Code_39MaxLen, sizeof(Code_39MaxLen));
			LOGE("set max");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}

		if(prefix == 0x01) {
			m_addpre = (char*)malloc(sizeof(Code_39AddPre)+strlen(m_strPre)+3);
			memset(m_addpre,'\0',sizeof(Code_39AddPre)+strlen(m_strPre)+3);
			LOGE("m_addpre len = %d,m_strPre len = %d",strlen(m_addpre),strlen(m_strPre));
			setPrefixSuffix(m_addpre,Code_39AddPre,sizeof(Code_39AddPre),m_strPre);
			ret = writePort(m_addpre,strlen(m_addpre));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(Code_39ClearPre,sizeof(Code_39ClearPre));
			LOGE("clear pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}

		if(suffix == 0x01) {
			m_addsuf = (char*)malloc(sizeof(Code_39AddSuf)+strlen(m_strSuf)+3);
			memset(m_addsuf,'\0',sizeof(Code_39AddSuf)+strlen(m_strSuf)+3);
			setPrefixSuffix(m_addsuf,Code_39AddSuf,sizeof(Code_39AddSuf),m_strSuf);
			ret = writePort(m_addsuf,strlen(m_addsuf));
			LOGE("set suf");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(Code_39ClearSuf,sizeof(Code_39ClearSuf));
			LOGE("clear suf");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}
		break;
	case 0x02: //code 128
		//if(minLen == 0x01) {
			Code_128MinLen[9] = minLen/10+48;
			Code_128MinLen[10] = minLen%10+48;
			ret = writePort(Code_128MinLen, sizeof(Code_128MinLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
		//if(maxLen == 0x01) {
			Code_128MaxLen[9] = maxLen/10+48;
			Code_128MaxLen[10] = maxLen%10+48;
			ret = writePort(Code_128MaxLen, sizeof(Code_128MaxLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}

		if(prefix == 0x01) {
			m_addpre = (char*)malloc(sizeof(Code_128AddPre)+strlen(m_strPre)+3);
			memset(m_addpre,'\0',sizeof(Code_128AddPre)+strlen(m_strPre)+3);
			LOGE("m_addpre len = %d,m_strPre len = %d",strlen(m_addpre),strlen(m_strPre));
			setPrefixSuffix(m_addpre,Code_128AddPre,sizeof(Code_128AddPre),m_strPre);
			ret = writePort(m_addpre,strlen(m_addpre));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(Code_128ClearPre,sizeof(Code_128ClearPre));
			LOGE("clear pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}

		if(suffix == 0x01) {
			m_addsuf = (char*)malloc(sizeof(Code_128AddSuf)+strlen(m_strSuf)+3);
			memset(m_addsuf,'\0',sizeof(Code_128AddSuf)+strlen(m_strSuf)+3);
			setPrefixSuffix(m_addsuf,Code_128AddSuf,sizeof(Code_128AddSuf),m_strSuf);
			ret = writePort(m_addsuf,strlen(m_addsuf));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(Code_128ClearSuf,sizeof(Code_128ClearSuf));
			LOGE("set suf");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}
		break;
	case 0x03: //interleaved 2 of 5
		switch(flag){
		case 0x00://no check
			ret = writePort(Interleaved_2of5NCC, sizeof(Interleaved_2of5NCC));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
			break;
		case 0x01://validate,don't transmit
			ret = writePort(Interleaved_2of5VDT, sizeof(Interleaved_2of5VDT));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
			break;
		case 0x02://validate and transmit
			break;
			ret = writePort(Interleaved_2of5VT, sizeof(Interleaved_2of5VT));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		default:
			break;
		}
		//if(minLen == 0x01) {
			Interleaved_2of5MinLen[9] = minLen/10+48;
			Interleaved_2of5MinLen[10] = minLen%10+48;
			ret = writePort(Interleaved_2of5MinLen, sizeof(Interleaved_2of5MinLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
		//if(maxLen == 0x01) {
			Interleaved_2of5MaxLen[9] = maxLen/10+48;
			Interleaved_2of5MaxLen[10] = maxLen%10+48;
			ret = writePort(Interleaved_2of5MaxLen, sizeof(Interleaved_2of5MaxLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}

		if(prefix == 0x01) {
			m_addpre = (char*)malloc(sizeof(Interleaved_2of5AddPre)+strlen(m_strPre)+3);
			memset(m_addpre,'\0',sizeof(Interleaved_2of5AddPre)+strlen(m_strPre)+3);
			LOGE("m_addpre len = %d,m_strPre len = %d",strlen(m_addpre),strlen(m_strPre));
			setPrefixSuffix(m_addpre,Interleaved_2of5AddPre,sizeof(Interleaved_2of5AddPre),m_strPre);
			ret = writePort(m_addpre,strlen(m_addpre));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(Interleaved_2of5ClearPre,sizeof(Interleaved_2of5ClearPre));
			LOGE("clear pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}

		if(suffix == 0x01) {
			m_addsuf = (char*)malloc(sizeof(Interleaved_2of5AddSuf)+strlen(m_strSuf)+3);
			memset(m_addsuf,'\0',sizeof(Interleaved_2of5AddSuf)+strlen(m_strSuf)+3);
			setPrefixSuffix(m_addsuf,Interleaved_2of5AddSuf,sizeof(Interleaved_2of5AddSuf),m_strSuf);
			ret = writePort(m_addsuf,strlen(m_addsuf));
			LOGE("set suf");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(Interleaved_2of5ClearSuf,sizeof(Interleaved_2of5ClearSuf));
			LOGE("clear suf");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}
		break;
	case 0x04: //gs1-128
		//if(minLen == 0x01) {
			GS1_128MinLen[9] = minLen/10+48;
			GS1_128MinLen[10] = minLen%10+48;
			ret = writePort(GS1_128MinLen, sizeof(GS1_128MinLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
		//if(maxLen == 0x01) {
			GS1_128MaxLen[9] = maxLen/10+48;
			GS1_128MaxLen[10] = maxLen%10+48;
			ret = writePort(GS1_128MaxLen, sizeof(GS1_128MaxLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}

		if(prefix == 0x01) {
			m_addpre = (char*)malloc(sizeof(GS1_128AddPre)+strlen(m_strPre)+3);
			memset(m_addpre,'\0',sizeof(GS1_128AddPre)+strlen(m_strPre)+3);
			setPrefixSuffix(m_addpre,GS1_128AddPre,sizeof(GS1_128AddPre),m_strPre);
			ret = writePort(m_addpre,strlen(m_addpre));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(GS1_128ClearPre,sizeof(GS1_128ClearPre));
			LOGE("clear pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}

		if(suffix == 0x01) {
			m_addsuf = (char*)malloc(sizeof(GS1_128AddSuf)+strlen(m_strSuf)+3);
			memset(m_addsuf,'\0',sizeof(GS1_128AddSuf)+strlen(m_strSuf)+3);
			setPrefixSuffix(m_addsuf,GS1_128AddSuf,sizeof(GS1_128AddSuf),m_strSuf);
			ret = writePort(m_addsuf,strlen(m_addsuf));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(GS1_128ClearSuf,sizeof(GS1_128ClearSuf));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}
		break;
	case 0x05://ean-13
		if(upcToEan == 0x01) {
			ret = writePort(UPCConToEAN13, sizeof(UPCConToEAN13));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(NUPCConToEAN13, sizeof(NUPCConToEAN13));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}
		if(minLen == 0x01) {

		}
		if(maxLen == 0x01) {

		}

		if(prefix == 0x01) {
			m_addpre = (char*)malloc(sizeof(EAN_13AddPre)+strlen(m_strPre)+3);
			memset(m_addpre,'\0',sizeof(EAN_13AddPre)+strlen(m_strPre)+3);
			setPrefixSuffix(m_addpre,EAN_13AddPre,sizeof(EAN_13AddPre),m_strPre);
			ret = writePort(m_addpre,strlen(m_addpre));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(EAN_13ClearPre,sizeof(EAN_13ClearPre));
			LOGE("clear pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}

		if(suffix == 0x01) {
			m_addsuf = (char*)malloc(sizeof(EAN_13AddSuf)+strlen(m_strSuf)+3);
			memset(m_addsuf,'\0',sizeof(EAN_13AddSuf)+strlen(m_strSuf)+3);
			setPrefixSuffix(m_addsuf,EAN_13AddSuf,sizeof(EAN_13AddSuf),m_strSuf);
			ret = writePort(m_addsuf,strlen(m_addsuf));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}else {
			ret = writePort(EAN_13ClearSuf,sizeof(EAN_13ClearSuf));
			LOGE("set pre");
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		}
		break;
	case 0x06://pdf417
		//if(minLen == 0x01) {

			PDF417_MinLen[9] = minLen/1000+48;
			PDF417_MinLen[10] = (minLen%1000)/100+48;
			PDF417_MinLen[11] = (minLen%100)/10+48;
			PDF417_MinLen[12] = minLen%10+48;
			ret = writePort(PDF417_MinLen, sizeof(PDF417_MinLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
		//if(maxLen == 0x01) {
			PDF417_MaxLen[9] = maxLen/1000+48;
			PDF417_MaxLen[10] = (maxLen%1000)/100+48;
			PDF417_MaxLen[11] = (maxLen%100)/10+48;
			PDF417_MaxLen[12] = maxLen%10+48;
			ret = writePort(PDF417_MaxLen, sizeof(PDF417_MaxLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
			if(prefix == 0x01) {
				m_addpre = (char*)malloc(sizeof(PDF417_AddPre)+strlen(m_strPre)+3);
				memset(m_addpre,'\0',sizeof(PDF417_AddPre)+strlen(m_strPre)+3);
				setPrefixSuffix(m_addpre,PDF417_AddPre,sizeof(PDF417_AddPre),m_strPre);
				ret = writePort(m_addpre,strlen(m_addpre));
				LOGE("set pre");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}else {
				ret = writePort(PDF417_ClearPre,sizeof(PDF417_ClearPre));
				LOGE("clear pre");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}

			if(suffix == 0x01) {
				m_addsuf = (char*)malloc(sizeof(PDF417_AddSuf)+strlen(m_strSuf)+3);
				memset(m_addsuf,'\0',sizeof(PDF417_AddSuf)+strlen(m_strSuf)+3);
				setPrefixSuffix(m_addsuf,PDF417_AddSuf,sizeof(PDF417_AddSuf),m_strSuf);
				ret = writePort(m_addsuf,strlen(m_addsuf));
				LOGE("set suf");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}else {
				ret = writePort(PDF417_ClearSuf,sizeof(PDF417_ClearSuf));
				LOGE("clear suf");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}
		break;
	case 0x07://qr code
		//if(minLen == 0x01) {
			QR_CODE_MinLen[9] = minLen/1000+48;
			QR_CODE_MinLen[10] = (minLen%1000)/100+48;
			QR_CODE_MinLen[11] = (minLen%100)/10+48;
			QR_CODE_MinLen[12] = minLen%10+48;
			ret = writePort(QR_CODE_MinLen, sizeof(QR_CODE_MinLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
		//if(maxLen == 0x01) {
			QR_CODE_MaxLen[9] = maxLen/1000+48;
			QR_CODE_MaxLen[10] = (maxLen%1000)/100+48;
			QR_CODE_MaxLen[11] = (maxLen%100)/10+48;
			QR_CODE_MaxLen[12] = maxLen%10+48;
			ret = writePort(QR_CODE_MaxLen, sizeof(QR_CODE_MaxLen));
			if (ret != 0) {
				printf("write uart error %d\n", errno);
				return -1;
			}
		//}
			if(prefix == 0x01) {
				m_addpre = (char*)malloc(sizeof(QR_CODE_AddPre)+strlen(m_strPre)+3);
				memset(m_addpre,'\0',sizeof(QR_CODE_AddPre)+strlen(m_strPre)+3);
				setPrefixSuffix(m_addpre,QR_CODE_AddPre,sizeof(QR_CODE_AddPre),m_strPre);
				ret = writePort(m_addpre,strlen(m_addpre));
				LOGE("set pre");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}else {
				ret = writePort(QR_CODE_ClearPre,sizeof(QR_CODE_ClearPre));
				LOGE("clear pre");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}

			if(suffix == 0x01) {
				m_addsuf = (char*)malloc(sizeof(QR_CODE_AddSuf)+strlen(m_strSuf)+3);
				memset(m_addsuf,'\0',sizeof(QR_CODE_AddSuf)+strlen(m_strSuf)+3);
				setPrefixSuffix(m_addsuf,QR_CODE_AddSuf,sizeof(QR_CODE_AddSuf),m_strSuf);
				ret = writePort(m_addsuf,strlen(m_addsuf));
				LOGE("set suf");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}else {
				ret = writePort(QR_CODE_ClearSuf,sizeof(QR_CODE_ClearSuf));
				LOGE("clear suf");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}
		break;
	case 0x08://MicroPDF417
			//if(minLen == 0x01) {
			MicroPDF417_MinLen[9] = minLen/100+48;
			MicroPDF417_MinLen[10] = (minLen%100)/10+48;
			MicroPDF417_MinLen[11] = minLen%10+48;
				ret = writePort(MicroPDF417_MinLen, sizeof(MicroPDF417_MinLen));
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			//}
			//if(maxLen == 0x01) {
				MicroPDF417_MaxLen[9] = maxLen/100+48;
				MicroPDF417_MaxLen[10] = (maxLen%100)/10+48;
				MicroPDF417_MaxLen[11] = maxLen%10+48;
				ret = writePort(MicroPDF417_MaxLen, sizeof(MicroPDF417_MaxLen));
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			//}
			if(prefix == 0x01) {
				m_addpre = (char*)malloc(sizeof(MicroPDF417_AddPre)+strlen(m_strPre)+3);
				memset(m_addpre,'\0',sizeof(MicroPDF417_AddPre)+strlen(m_strPre)+3);
				setPrefixSuffix(m_addpre,MicroPDF417_AddPre,sizeof(MicroPDF417_AddPre),m_strPre);
				ret = writePort(m_addpre,strlen(m_addpre));
				LOGE("set pre");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}else {
				ret = writePort(MicroPDF417_ClearPre,sizeof(MicroPDF417_ClearPre));
				LOGE("clear pre");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}

			if(suffix == 0x01) {
				m_addsuf = (char*)malloc(sizeof(MicroPDF417_AddSuf)+strlen(m_strSuf)+3);
				memset(m_addsuf,'\0',sizeof(MicroPDF417_AddSuf)+strlen(m_strSuf)+3);
				setPrefixSuffix(m_addsuf,MicroPDF417_AddSuf,sizeof(MicroPDF417_AddSuf),m_strSuf);
				ret = writePort(m_addsuf,strlen(m_addsuf));
				LOGE("set suf");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}else {
				ret = writePort(MicroPDF417_ClearSuf,sizeof(MicroPDF417_ClearSuf));
				LOGE("clear suf");
				if (ret != 0) {
					printf("write uart error %d\n", errno);
					return -1;
				}
			}
			break;
	default:
		break;
	}

	if(m_strPre != NULL) {
		free(m_strPre);
		m_strPre = NULL;
	}
	if(m_strSuf != NULL) {
		free(m_strSuf);
		m_strSuf = NULL;
	}
	if(m_addpre != NULL) {
		free(m_addpre);
		m_addpre = NULL;
	}
	if(m_addsuf != NULL) {
		free(m_addsuf);
		m_addsuf = NULL;
	}
	//cilico_scan_close_fd();
	return 0;
}

int dohonywareset() {
	int ret;

    //char rmCustomDF[] = { 0x16, 'M', 0x0D, 'D', 'E', 'F', 'O', 'V', 'R', '.' };
    char facDefault[] = { 0x16, 'M', 0x0D, 'D', 'E', 'F', 'A', 'L', 'T', '.' };
    //config data fat
	char buf1[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'A', '2', '.' };   //Clear All Prefixes
	char buf2[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '9', '9',
			'0', '2', '5', 'C', '8', '0', '.' };
	char buf3[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'A', '2', '.' };   //Clear All Suffixes
	char buf4[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '9', '9',
			'0', '3', '.' };

    ret = writePort(facDefault, sizeof(facDefault));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
	    close(fd);
		return -1;
	}
	usleep(50*1000);
	ret = writePort(buf1, sizeof(buf1));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf2, sizeof(buf2));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf3, sizeof(buf3));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	ret = writePort(buf4, sizeof(buf4));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}

    LOGE("dohonywareset ==========default=");
	return 0;

}

int Code39NCC()
{
	int ret = -1;
	ret = writePort(Code_39NCC,sizeof(Code_39NCC));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Code39VDT()
{
	int ret = -1;
	ret = writePort(Code_39VDT,sizeof(Code_39VDT));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Code39VT()
{
	int ret = -1;
	ret = writePort(Code_39VT,sizeof(Code_39VT));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Code39MinLen()
{
	int ret = -1;
	ret = writePort(Code_39MinLen,sizeof(Code_39MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Code39MaxLen()
{
	int ret = -1;
	ret = writePort(Code_39MaxLen,sizeof(Code_39MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Code128MinLen()
{
	int ret = -1;
	ret = writePort(Code_128MinLen,sizeof(Code_128MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Code128MaxLen()
{
	int ret = -1;
	ret = writePort(Code_128MaxLen,sizeof(Code_128MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Interleaved2of5NCC()
{
	int ret = -1;
	ret = writePort(Interleaved_2of5NCC,sizeof(Interleaved_2of5NCC));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Interleaved2of5VDT()
{
	int ret = -1;
	ret = writePort(Interleaved_2of5VDT,sizeof(Interleaved_2of5VDT));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Interleaved2of5VT()
{
	int ret = -1;
	ret = writePort(Interleaved_2of5VT,sizeof(Interleaved_2of5VT));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Interleaved2of5MinLen()
{
	int ret = -1;
	ret = writePort(Interleaved_2of5MinLen,sizeof(Interleaved_2of5MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int Interleaved2of5MaxLen()
{
	int ret = -1;
	ret = writePort(Interleaved_2of5MaxLen,sizeof(Interleaved_2of5MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int GS1128MinLen()
{
	int ret = -1;
	ret = writePort(GS1_128MinLen,sizeof(GS1_128MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int GS1128MaxLen()
{
	int ret = -1;
	ret = writePort(GS1_128MaxLen,sizeof(GS1_128MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int UPCA_ConToEAN13()
{
	int ret = -1;
	ret = writePort(UPCConToEAN13,sizeof(UPCConToEAN13));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int NUPCA_ConToEAN13()
{
	int ret = -1;
	ret = writePort(NUPCConToEAN13,sizeof(NUPCConToEAN13));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int PDF417MinLen()
{
	int ret = -1;
	ret = writePort(PDF417_MinLen,sizeof(PDF417_MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int PDF417MaxLen()
{
	int ret = -1;
	ret = writePort(PDF417_MaxLen,sizeof(PDF417_MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int MacroPDF417On()
{
	int ret = -1;
	ret = writePort(MacroPDF417_On,sizeof(MacroPDF417_On));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int MacroPDF417Off()
{
	int ret = -1;
	ret = writePort(MacroPDF417_Off,sizeof(MacroPDF417_Off));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int MicroPDF417On()
{
	int ret = -1;
	ret = writePort(MicroPDF417_On,sizeof(MicroPDF417_On));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int MicroPDF417Off()
{
	int ret = -1;
	ret = writePort(MicroPDF417_Off,sizeof(MicroPDF417_Off));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int MicroPDF417MinLen()
{
	int ret = -1;
	ret = writePort(MicroPDF417_MinLen,sizeof(MicroPDF417_MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int MicroPDF417MaxLen()
{
	int ret = -1;
	ret = writePort(MicroPDF417_MaxLen,sizeof(MicroPDF417_MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int QRCODEMinLen()
{
	int ret = -1;
	ret = writePort(QR_CODE_MinLen,sizeof(QR_CODE_MinLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
int QRCODEMaxLen()
{
	int ret = -1;
	ret = writePort(QR_CODE_MaxLen,sizeof(QR_CODE_MaxLen));
	if (ret != 0) {
		printf("write uart error %d\n", errno);
		close(fd);
		return -1;
	}
	return ret;
}
