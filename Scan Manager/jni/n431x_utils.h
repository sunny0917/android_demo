#ifndef __N431X_UTILS_H
#define __N431X_UTILS_H

static int fd = -1;

char buf1[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'A', '2', '.' };
char buf2[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '9', '9','0', '2', '5', 'C', '8', '0', '.' };  //Add Code ID
char buf3[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'A', '2', '.' };
char buf4[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '9', '9','0', '3','.' };

char buf11[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'A', '2', '.' };
char buf12[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '2','0', '2', '.' };  //Add Code ID
char buf13[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'A', '2', '.' };
char buf14[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '2','0', '3','5', 'C', '8', '0','.' };

//Add Prefix
char Add_prefix[] = {0x16, 'M', 0x0D,'P','R','E','B','K','2','.'};
//Clear One Prefix
char Clear_One_prefix[] = {0x16, 'M', 0x0D,'P','R','E','C','L','2','.'};
//Clear All Prefix
char Clear_All_prefix[] = {0x16, 'M', 0x0D,'P','R','E','C','A','2','.'};
char buf5[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2','.' };

//Add Suffix
char Add_suffix[] = {0x16, 'M', 0x0D,'S','U','F','B','K','2','.'};
//Clear One Suffix
char Clear_One_suffix[] = {0x16, 'M', 0x0D,'S','U','F','C','L','2','.'};
//Clear All Suffix
char Clear_All_suffix[] = {0x16, 'M', 0x0D,'S','U','F','C','A','2','.'};
char buf6[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2','.' };


//Code 39
char Code_39On[] = {0x16, 'M', 0x0D,'C','3','9','E','N','A','1','.'};
char Code_39Off[] = {0x16, 'M', 0x0D,'C','3','9','E','N','A','0','.'};
//Code 39 Check Character
char Code_39NCC[] = {0x16, 'M', 0x0D,'C','3','9','C','K','2','0','.'};   //No Check Character
char Code_39VDT[] = {0x16, 'M', 0x0D,'C','3','9','C','K','2','1','.'};  //Validate, but Don¡¯t Transmit
char Code_39VT[] = {0x16, 'M', 0x0D,'C','3','9','C','K','2','2','.'};   //Validate and Transmit
//Code 39 Message Length
char Code_39MinLen[] = {0x16, 'M', 0x0D,'C','3','9','M','I','N','0','0','.'};
char Code_39MaxLen[] = {0x16, 'M', 0x0D,'C','3','9','M','A','X','4','8','.'};
#if 0
//Code 39 Prefix
char Code_39AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '2','0', '2', '5', 'C', '8', '0', '.' };
char Code_39ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', '2', '.' };
char Code_39AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '2','0', '2', '5', 'C', '8', '0', '.' };
char Code_39ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', '2', '.' };
#else
char Code_39AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '2'};
char Code_39ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', '2', '.' };
char Code_39AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '2' };
char Code_39ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', '2', '.' };
#endif
//Code 128
char Code_128On[] = {0x16, 'M', 0x0D,'1','2','8','E','N','A','1','.'};
char Code_128Off[] = {0x16, 'M', 0x0D,'1','2','8','E','N','A','0','.'};
//Code 128 Message Length
char Code_128MinLen[] = {0x16, 'M', 0x0D,'1','2','8','M','I','N','0','0','.'};
char Code_128MaxLen[] = {0x16, 'M', 0x0D,'1','2','8','M','A','X','8','0','.'};
//Code 128 Prefix
#if 0
char Code_128AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', 'A','0', '2', '5', 'C', '8', '0', '.' };
char Code_128ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', 'A', '.' };
char Code_128AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', 'A','0', '2', '5', 'C', '8', '0', '.' };
char Code_128ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', 'A', '.' };
#else
char Code_128AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', 'A'};
char Code_128ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', 'A', '.' };
char Code_128AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', 'A'};
char Code_128ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', 'A', '.' };
#endif
//Interleaved 2 of 5
char Interleaved_2of5On[] = {0x16, 'M', 0x0D,'I','2','5','E','N','A','1','.'};
char Interleaved_2of5Off[] = {0x16, 'M', 0x0D,'I','2','5','E','N','A','0','.'};
//Interleaved 2 of 5 Check Character
char Interleaved_2of5NCC[] = {0x16, 'M', 0x0D,'I','2','5','C','K','2','0','.'};   //No Check Character
char Interleaved_2of5VDT[] = {0x16, 'M', 0x0D,'I','2','5','C','K','2','1','.'};  //Validate, but Don¡¯t Transmit
char Interleaved_2of5VT[] = {0x16, 'M', 0x0D,'I','2','5','C','K','2','2','.'};   //Validate and Transmit
//Interleaved 2 of 5 Message Length
char Interleaved_2of5MinLen[] = {0x16, 'M', 0x0D,'I','2','5','M','I','N','0','4','.'};
char Interleaved_2of5MaxLen[] = {0x16, 'M', 0x0D,'I','2','5','M','A','X','8','0','.'};
//Interleaved 2 of 5 Prefix
#if 0
char Interleaved_2of5AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '5','0', '2', '5', 'C', '8', '0', '.' };
char Interleaved_2of5ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', '5', '.' };
char Interleaved_2of5AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '5','0', '2', '5', 'C', '8', '0', '.' };
char Interleaved_2of5ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', '5', '.' };
#else
char Interleaved_2of5AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '5'};
char Interleaved_2of5ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', '5', '.' };
char Interleaved_2of5AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '5'};
char Interleaved_2of5ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', '5', '.' };
#endif
//GS1-128
char GS1_128On[] = {0x16, 'M', 0x0D,'G','S','1','E','N','A','1','.'};
char GS1_128Off[] = {0x16, 'M', 0x0D,'G','S','1','E','N','A','0','.'};
//GS1-128 Message Length
char GS1_128MinLen[] = {0x16, 'M', 0x0D,'G','S','1','M','I','N','0','1','.'};
char GS1_128MaxLen[] = {0x16, 'M', 0x0D,'G','S','1','M','A','X','8','0','.'};
//GS1-128 Prefix
#if 0
char GS1_128AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '4', '9','0', '2', '5', 'C', '8', '0', '.' };
char GS1_128ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '4', '9', '.' };
char GS1_128AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '4', '9','0', '2', '5', 'C', '8', '0', '.' };
char GS1_128ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '4', '9', '.' };
#else
char GS1_128AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '4', '9'};
char GS1_128ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '4', '9', '.' };
char GS1_128AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '4', '9'};
char GS1_128ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '4', '9', '.' };
#endif
//EAN-13
char EAN_13On[] = {0x16, 'M', 0x0D,'E','1','3','E','N','A','1','.'};
char EAN_13Off[] = {0x16, 'M', 0x0D,'E','1','3','E','N','A','0','.'};
//Convert UPC-A to EAN-13
char UPCConToEAN13[] = {0x16, 'M', 0x0D,'U','P','A','E','N','A','0','.'};
char NUPCConToEAN13[] = {0x16, 'M', 0x0D,'U','P','A','E','N','A','1','.'};
//EAN-13 Prefix
#if 0
char EAN_13AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '4','0', '2', '5', 'C', '8', '0', '.' };
char EAN_13ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', '4', '.' };
char EAN_13AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '4','0', '2', '5', 'C', '8', '0', '.' };
char EAN_13ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', '4', '.' };
#else
char EAN_13AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '6', '4'};
char EAN_13ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '6', '4', '.' };
char EAN_13AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '6', '4'};
char EAN_13ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '6', '4', '.' };
#endif
//PDF417
char PDF417_On[] = {0x16, 'M', 0x0D,'P','D','F','E','N','A','1','.'};
char PDF417_Off[] = {0x16, 'M', 0x0D,'P','D','F','E','N','A','0','.'};
//PDF417 Message Length
char PDF417_MinLen[] = {0x16, 'M', 0x0D,'P','D','F','M','I','N','0','0','0','1','.'};
char PDF417_MaxLen[] = {0x16, 'M', 0x0D,'P','D','F','M','A','X','2','7','5','0','.'};

char PDF417_AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '7', '2'};
char PDF417_ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '7', '2', '.' };
char PDF417_AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '7', '2'};
char PDF417_ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '7', '2', '.' };
//MacroPDF417
char MacroPDF417_On[] = {0x16, 'M', 0x0D,'P','D','F','M','A','C','1','.'};
char MacroPDF417_Off[] = {0x16, 'M', 0x0D,'P','D','F','M','A','C','0','.'};
//MicroPDF417
char MicroPDF417_On[] = {0x16, 'M', 0x0D,'M','P','D','E','N','A','1','.'};
char MicroPDF417_Off[] = {0x16, 'M', 0x0D,'M','P','D','E','N','A','0','.'};
//MicroPDF417 Message Length
char MicroPDF417_MinLen[] = {0x16, 'M', 0x0D,'M','P','D','M','I','N','0','0','1','.'};
char MicroPDF417_MaxLen[] = {0x16, 'M', 0x0D,'M','P','D','M','A','X','3','6','6','.'};

char MicroPDF417_AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '5', '2'};
char MicroPDF417_ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '5', '2', '.' };
char MicroPDF417_AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '5', '2'};
char MicroPDF417_ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '5', '2', '.' };
//QR CODE
char QR_CODE_On[] = {0x16, 'M', 0x0D,'Q','R','C','E','N','A','1','.'};
char QR_CODE_Off[] = {0x16, 'M', 0x0D,'Q','R','C','E','N','A','0','.'};
//QR Code Message Length
char QR_CODE_MinLen[] = {0x16, 'M', 0x0D,'Q','R','C','M','I','N','0','0','0','1','.'};
char QR_CODE_MaxLen[] = {0x16, 'M', 0x0D,'Q','R','C','M','A','X','7','0','8','9','.'};

char QR_CODE_AddPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'B', 'K', '2', '7', '3'};
char QR_CODE_ClearPre[] = { 0x16, 'M', 0x0D, 'P', 'R', 'E', 'C', 'L', '2', '7', '3', '.' };
char QR_CODE_AddSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'B', 'K', '2', '7', '3'};
char QR_CODE_ClearSuf[] = { 0x16, 'M', 0x0D, 'S', 'U', 'F', 'C', 'L', '2', '7', '3', '.' };
//int getBaudrate(int baudrate);
int cilico_scan_open_fd(const char* path, int baudrate);
int cilico_scan_close_fd();
int honywareTypeOnOff(const char* type,int enable);
int dohonywareset();
int honywareSetPre();
int honywareSetSuf();
int honywellClearPre();
int honywellClearSuf();
int writePort(char *buf,int len);
#endif
