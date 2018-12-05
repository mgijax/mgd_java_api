package org.jax.mgi.mgd.api.util;

import java.util.HashMap;
import java.util.Map;

public class DateSQLQuery {
	
	public static String[] queryByCreationModification(String tag, String createdBy, String modifiedBy, String creation_date, String modification_date) {

		String from = "";
		String where = "";
		
		Map<String, Object> params = new HashMap<>();
		
		if (createdBy != null && !createdBy.isEmpty()) {
			params.put("createdBy", createdBy);
		}
		if (modifiedBy != null && !modifiedBy.isEmpty()) {
			params.put("modifiedBy", modifiedBy);
		}
		if (creation_date != null && !creation_date.isEmpty()) {
			params.put("creation_date", creation_date);
		}
		if (modification_date != null && !modification_date.isEmpty()) {
			params.put("modification_date", modification_date);
		}
		
		// construct where for created by/modified by
		if (params.containsKey("createdBy")) {
			where = where + "\nand u1.login ilike '" + params.get("createdBy") + "'";
			where = where + "\nand " + tag + "._CreatedBy_key = u1._User_key";
			from = from + ",mgi_user u1";
		}
		
		if (params.containsKey("modifiedBy")) {
			where = where + "\nand u2.login ilike '" + params.get("modifiedBy") + "'";
			where = where + "\nand " + tag + "._ModifiedBy_key = u2._User_key";
			from = from + ",mgi_user u2";
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
					where = where + "\nand " + field + "::date <= '" + value.replace("<=","") + "'";
				}
				else if (value.startsWith("<") == true) {
					where = where + "\nand " + field + "::date < '" + value.replace("<",  "") + "'";
				}
				else if (value.startsWith(">=") == true) {
					where = where + "\nand " + field + "::date >= '" + value.replace(">=","") + "'";
				}
				else if (value.startsWith(">") == true) {
					where = where + "\nand " + field + "::date > '" + value.replace(">",  "") + "'";
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
