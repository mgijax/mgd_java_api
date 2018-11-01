package org.jax.mgi.mgd.api.model.mrk.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Marker History Search Form")
public class MarkerHistorySearchForm extends BaseSearchForm {

	private Integer markerKey;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		
		if(markerKey != null && markerKey != 0) { ret.put("markerKey", markerKey); }
	
		return ret;
	}
	
}
