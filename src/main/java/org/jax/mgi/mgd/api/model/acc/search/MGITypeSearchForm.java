package org.jax.mgi.mgd.api.model.acc.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("MGI Type Search Form")
public class MGITypeSearchForm extends BaseSearchForm {

	@ApiParam(value="MGI Type Name to Search for")
	private String mgiTypeName;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		if(mgiTypeName != null) {ret.put("name", mgiTypeName); }

		return ret;
	}
}
