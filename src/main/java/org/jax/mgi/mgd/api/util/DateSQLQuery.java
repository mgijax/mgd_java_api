package org.jax.mgi.mgd.api.util;

import java.util.Map;

public class DateSQLQuery {
			
	public static String[] queryByCreationModification(Map<String, Object> params, String tag) {

		String from = "";
		String where = "";
				
		// construct where for created by/modified by
		if (params.containsKey("createdBy")) {
			where = where + "\nand u1.login ilike '" + params.get("createdBy") + "'";
			where = where + "\nand " + tag + "._CreatedBy_key = u1._User_key";
			from = from + ",MGI_User u1";
		}
		
		if (params.containsKey("modifiedBy")) {
			where = where + "\nand u2.login ilike '" + params.get("modifiedBy") + "'";
			where = where + "\nand " + tag + "._ModifiedBy_key = u2._User_key";
			from = from + ",MGI_User u2";
		}

		// construct where for creation/modification dates
		String[] dateFields = {"creation_date", "modification_date"};
		String value = "";
		String field = "";

		for (int x=0;x<dateFields.length;x++) {
			
			if (params.containsKey(dateFields[x])) {
				
				value = params.get(dateFields[x]).toString();
				field = tag + "." + dateFields[x];

				if (value.startsWith("<=") == true) {
					where = where + "\nand " + field + " <= '" + value.replace("<=","") + "'";
				}
				else if (value.startsWith("<") == true) {
					where = where + "\nand " + field + " < '" + value.replace("<",  "") + "'";
				}
				else if (value.startsWith(">=") == true) {
					where = where + "\nand " + field + " >= '" + value.replace(">=","") + "'";
				}
				else if (value.startsWith(">") == true) {
					where = where + "\nand " + field + " > '" + value.replace(">",  "") + "'";
				}
				else if (value.contains("..") == true) {
					String[] tokens = value.split("\\.\\.");
					where = where + "\nand (" + field + " between '" + tokens[0] 
							+ "' and ('" + tokens[1] + "'::date + '1 day'::interval))";
				}
				else {
					where = where + "\nand (" + field + " between '" + value 
							+ "' and ('" + value + "'::date + '1 day'::interval))";
				}
			}
		}
		
		return new String[] {from, where};
	}
}
