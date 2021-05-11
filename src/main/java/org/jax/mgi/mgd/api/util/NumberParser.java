package org.jax.mgi.mgd.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jax.mgi.mgd.api.exception.FatalAPIException;

/* Is: an object that knows how to take a number from a search field and extract relevant bits of data
 */
public class NumberParser {
	/***--- static variables ---***/
	
	public static String EXACTLY = "=";			// only looking at that exact number (operator implied, not entered)
	public static String UP_TO = "<";			// values up to but not including the given number
	public static String UP_THROUGH = "<=";		// values up to and including the given number
	public static String AFTER = ">";			// values higher than the given number
	public static String STARTING_WITH = ">=";	// values at or higher than the given number
	
	private static String[] prefixOperators = { UP_THROUGH, STARTING_WITH, UP_TO, AFTER };

	/***--- instance variables ---***/
	
	// floating point number (with our without leading + or - sign); decimal point and fractional part are optional,
	// so this will pick up integers too.
	Pattern numberPattern = Pattern.compile("([+-]?[0-9]+)([.]?[0-9]*)");

	/***--- instance methods ---***/
	
	/* parse the user's entered numeric query string and return a list of Strings.  That list will either contain
	 * two entries:
	 *		[start number operator, start number]
	 * or four entries:
	 * 		[start number operator, start number, end number operator, end number]
	 * The operators will be from the five constants:  EXACTLY, UP_TO, UP_THROUGH, AFTER, STARTING_WITH.
	 * Recognized number formats include three integer formats:
	 * 		1234  -1234  +1234
	 * and six floating point formats:
	 * 		1234.  -1234.  +1234.  1234.567  -1234.567  +1234.567
	 * Output numbers will always:
	 * 		1. omit a leading plus sign
	 * 		2. omit a trailing decimal point (if no decimal digits follow it)
	 * 		3. omit trailing zeroes after a decimal point
	 */
	public List<String> parse(String number) throws FatalAPIException {
		// strip any commas (assuming they're due to place value notation)
		String cleanNumber = number.replaceAll(",", "").trim();
		
		List<String> output = new ArrayList<String>();
		
		// first look for the four relational operators at the start of the String
		for (String prefixOp : prefixOperators) {
			if (cleanNumber.startsWith(prefixOp)) {
				output.add(prefixOp);
				output.add(this.standardizeFormat(cleanNumber.substring(prefixOp.length()).trim()));
				break;
			}
		}
		
		// no relational operator was found, so look for 'between' operator and then just fall back on
		// a single date
		if (output.size() == 0) {
			int splitPos = cleanNumber.indexOf("..");
			if (splitPos >= 0) {
				output.add(STARTING_WITH);
				output.add(this.standardizeFormat(cleanNumber.substring(0, splitPos).trim()));
				output.add(UP_THROUGH);
				output.add(this.standardizeFormat(cleanNumber.substring(splitPos + 2).trim()));
			}
		
			if (output.size() == 0) {
				output.add(EXACTLY);
				output.add(this.standardizeFormat(cleanNumber));
			}
		}
		return output;
	}
	
	/* convert the given number (see comments on parse method for recognized formats) to a standard format
	 */
	private String standardizeFormat(String number) throws FatalAPIException{
		if (numberPattern.matcher(number.trim()).matches()) {
			// omit a leading plus sign (the only one allowed by the pattern)
			number = number.replaceAll("[+]", "");
			
			if (number.indexOf(".") >= 0) {
				// Omit trailing zeroes after a decimal point.
				number = number.replaceAll("[0]+$", "");
				
				// Also omit a trailing decimal point.
				number = number.replaceAll("[.]+$", "");
			}

			return number;
		} else {
			throw new FatalAPIException("Cannot parse number: " + number);
		}
	}
}
