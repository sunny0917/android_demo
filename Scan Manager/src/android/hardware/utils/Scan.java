package android.hardware.utils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Scan {

	/**
	 * open port 
	 * @param name   "/dev/ttyMT1"
	 * @param baudrate  9600
	 * @return
	 */
	public static native int openPort(String name,int baudrate);
	
	/**
	 * close port
	 * @return
	 */
	public static native int closePort();
	
	/**
	 * enable/disable Honyware N431X some bar code type
	 * @param type  :bar code type
	 * 		  Code 39£º			type = "Code 39"
	 * 		  Code 128£º			type = "Code 128"
	 * 		  Interleaved 2 of 5£º	type = "Interleaved 2 of 5"
	 * 		  GS1-128£º			type = "GS1-128"
	 * 		  EAN-13£º				type = "EAN-13"
	 * 		  PDF417:           type = "PDF417"
	 *        MacroPDF417:      type = "MacroPDF417"
	 *        MicroPDF417:      type = "MicroPDF417"
	 *        QR Code:          type = "QR Code"
	 * @param enable   1£ºenable 0£ºdisable
	 * @return
	 */
    public static native int honywareTypeOnOff(String type,int enable);
    
    /**
     * Reset Honyware N431X to default state
     * @return
     */
    public static native int dohonywareset();
    
    /**
     * Add Prefix
     * @return
     */
    public static native int honywareSetPre();

    /**
     * Add Suffix
     * @return
     */
    public static native int honywareSetSuf();
    
    /**
     * Clear All Pre
     */
    public static native int honywellClearPre();
    
    /**
     * Clear All Suf
     */
    public static native int honywellClearSuf();
    
    /**
     * write buf to port
     * @param buf data for write
     * @return
     */
    public static native int writePort(byte[] buf);
    
    /**
     * symbology config
     * @param obj
     * @return
     */
    public static native int setSymbologyConfig(CodeParms obj);
}
