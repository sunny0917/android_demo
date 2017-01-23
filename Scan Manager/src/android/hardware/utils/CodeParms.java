package android.hardware.utils;

public class CodeParms {
	/** Symbology ID
	 * 0x01:Code 39
	 * 0x02:Code 128
	 * 0x03:interleaved 2 of 5
	 * 0x04:gs1-128
	 * 0x05:ean-13
	 * 0x06:pdf417
	 * 0x07:QR code
	 * 0x08:MicroPDF417
	 */
	public int symID;
	/** Flags 
	 * 0x00:No Check Character
	 * 0x01:Validate, but Don¡¯t Transmit
	 * 0x02:Validate and Transmit
	 s*/
	public int Flags;
	/** Minimum length for valid barcode string for this symbology
	 *
	 */
	public int MinLength;
	/** Maximum length for valid barcode string for this symbology 
	 */
	public int MaxLength; 
	/** Add Prefix	 
	 * 0x00:disable
	 * 0x01:enable
	 */
	public int Prefix;
	/** Add Suffix 
	 * 0x00:disable
	 * 0x01:enable
	 */
	public int Suffix;
	
	/**
	 * the String for prefix
	 */
	public String StrPrefix;
	
	/**
	 * the String for suffix
	 */
	public String StrSuffix;
	/**
	 * UPC-A Convert to Ean-13
	 * 0x00:disable
	 * 0x01:enable
	 */
	public int upcToEan;
	
	public CodeParms(int symbologyID){
		this.symID = symbologyID;
	}
}
