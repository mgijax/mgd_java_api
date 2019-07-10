package org.jax.mgi.mgd.api.util;

import java.io.UnsupportedEncodingException;

/*
 * Is a class that contains methods dealing with ISO-8859-15 (postgres/latin9) and UTF8   
 */
public class DecodeString {
		
	public static String getDecodeToUTF8(String s) {
		// get the ISO-8859-15/Latin9 string from the database
		// if the decoding returns a question mark, then return the original string
		// if the decoding does not return a question mark, then return the decoded/UTR8 string

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
		// set any string to UTF-8
		// also set the single quote to double quote for postgres

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
