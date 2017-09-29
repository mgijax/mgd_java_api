package org.jax.mgi.mgd.api.util;

import java.util.regex.Pattern;

/* Is: a class that provides convenience methods for various parameter checks
 */
public class Checks {
	private static Pattern intPattern = Pattern.compile("^[0-9]{1,9}$");

	public static boolean isInteger(String s) {
		return intPattern.matcher(s).matches();
	}
}
