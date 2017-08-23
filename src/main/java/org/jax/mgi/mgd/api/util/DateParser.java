package org.jax.mgi.mgd.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/* Is: an object that knows how to take a date from a search field and extract relevant bits of data
 */
public class DateParser {
	/***--- static variables ---***/
	
	public static String ON = "=";				// occurring on the specified day (operator implied, not entered)
	public static String UP_TO = "<";			// occurring up to (but not including) the specified day
	public static String UP_THROUGH = "<=";		// occurring up to or on the specified day
	public static String AFTER = ">";			// occurring after the specified day (but not on it)
	public static String STARTING_WITH = ">=";	// occurring on or after the specified day
	
	private static String[] prefixOperators = { UP_THROUGH, STARTING_WITH, UP_TO, AFTER };

	/***--- instance variables ---***/
	
	private SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yy");
	private SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy/MM/dd");
	
	/***--- instance methods ---***/
	
	/* parse the user's entered date string and return a list of Strings.  That list will either contain two entries:
	 *		[start date operator, start date]
	 * or four entries:
	 * 		[start date operator, start date, end date operator, end date]
	 * The operators will be from the five constants:  ON, UP_TO, UP_THROUGH, AFTER, STARTING_WITH.
	 * Recognized date formats include: mm-dd-yyyy, mm/dd/yyyy, mm-dd-yy, mm/dd/yy, yyyy-mm-dd, yyyy/mm/dd.
	 * Output dates will always be in mm/dd/yyyy format.
	 */
	public List<String> parse(String date) throws ParseException {
		// eliminate any spaces in the date, and convert hyphens to slashes for the sake of uniformity
		String cleanDate = date.replaceAll(" ", "").replaceAll("-", "/");
		List<String> output = new ArrayList<String>();
		
		// first look for the four relational operators at the start of the date string
		for (String prefixOp : prefixOperators) {
			if (cleanDate.startsWith(prefixOp)) {
				output.add(prefixOp);
				output.add(this.standardizeFormat(cleanDate.substring(prefixOp.length())));
				break;
			}
		}
		
		// no relational operator was found, so look for 'between' operator and then just fall back on
		// a single date
		if (output.size() == 0) {
			int splitPos = cleanDate.indexOf("..");
			if (splitPos >= 0) {
				output.add(STARTING_WITH);
				output.add(this.standardizeFormat(cleanDate.substring(0, splitPos)));
				output.add(UP_THROUGH);
				output.add(this.standardizeFormat(cleanDate.substring(splitPos + 2)));
			}
		
			if (output.size() == 0) {
				output.add(ON);
				output.add(this.standardizeFormat(cleanDate));
			}
		}
		return output;
	}
	
	/* convert the given date in mm/dd/yyyy, mm/dd/yy, or yyyy/mm/dd to a standard mm/dd/yyyy format
	 */
	private String standardizeFormat(String datePart) throws ParseException{
		try {
			return dateFormat1.format(dateFormat1.parse(datePart));
		} catch (Throwable t) {}

		try {
			return dateFormat1.format(dateFormat2.parse(datePart));
		} catch (Throwable t) {}

		return dateFormat1.format(dateFormat3.parse(datePart));
	}
}
