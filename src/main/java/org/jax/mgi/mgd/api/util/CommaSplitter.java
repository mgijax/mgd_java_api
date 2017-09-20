package org.jax.mgi.mgd.api.util;

import java.util.ArrayList;
import java.util.List;

/* Is: a class that provides convenience methods for dealing with comma-separated strings
 */
public class CommaSplitter {
	/* take a comma-separated string 's' and chop it into one or more substrings, each of
	 * which has up to 'n' items from the original 's'.  Each of these substrings will
	 * also be comma-delimited.
	 */
	public List<String> split(String s, int n) {
		List<String> sublists = new ArrayList<String>();
		
		if (s == null) return sublists;
		else if (n < 1) {
			sublists.add(s);
		} else {
			StringBuffer sb = new StringBuffer();
			int count = 0;
			
			for (String id : s.replaceAll(" ", "").split(",")) {
				// if more will fit in this chunk, add this one and a preceding comma, if needed
				if (count < n) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(id);
					count++;
				} else {
					if (sb.length() > 0) {
						sublists.add(sb.toString());
						sb = new StringBuffer();
					}
					sb.append(id);
					count = 1;
				}
			}
			
			if (sb.length() > 0) {
				sublists.add(sb.toString());
			}
		}
		return sublists;
	}
}
