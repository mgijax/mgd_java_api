package org.jax.mgi.mgd.api.util;

import java.io.UnsupportedEncodingException;

/*
 * Is a class that contains a method that returns a decoded 
 * string from ISO-8859-15 (postgres/latin9) to UTF8   
 */
public class DecodeString {
		
	public static String getDecodeToUTF8(String s) {

		String sDecoded = "";
		try {
			Boolean executeDecode = true;
			
			// decode postgres/latin9 to UTF8
			sDecoded = new String(s.getBytes("ISO-8859-15"), "UTF-8");
			
			// if postgres contains characters that cannot be converted to UTF8
			// then use existing postgres note
			// else, use decoded UTF8 encoding
			for (int i = 0; i < sDecoded.length(); i++){
				if (sDecoded.codePointAt(i) == 65533) {
					executeDecode = false;
				}
			}
						
			if (executeDecode) {
				return(sDecoded);				
			}
			else {
				return(s);				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return(s);
	}
	
	public static String setDecodeToLatin9(String s) {

		String sDecoded = "";
		try {
			sDecoded = new String(s.replace("'", "''").getBytes("UTF-8"), "ISO-8859-15");
			return(sDecoded);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return(s);
	}	
}
