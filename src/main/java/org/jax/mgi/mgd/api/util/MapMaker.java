package org.jax.mgi.mgd.api.util;

import java.util.Map;

import org.jax.mgi.mgd.api.exception.APIException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/* Is: an object that knows how to convert a JSON string to a String:Object map
 */
public class MapMaker {
	/***--- instance variables ---***/
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/***--- instance methods ---***/
	
	/* parses the map in the given json string and returns it as a mapping from String to Object
	 */
	public Map<String,Object> toMap(String json) throws APIException {
		try {
			Map<String,Object> map = mapper.readValue(json, new TypeReference<Map<String,Object>>(){});
			return map;
		} catch (Exception e) {
			throw new APIException("MapMaker.toMap failed: " + e.toString());
		}
	}
}
