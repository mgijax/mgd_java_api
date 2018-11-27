package org.jax.mgi.mgd.api.model.mgi.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("MGI Synonym Type Search Form")
public class MGISynonymTypeSearchForm extends BaseSearchForm {

	private String mgiTypeKey;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		
		if(mgiTypeKey != null && mgiTypeKey != "") { ret.put("mgiTypeKey", mgiTypeKey); }

		return ret;
	}

}
