package org.jax.mgi.mgd.api.model.mgi.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Organism Search Form")
public class OrganismSearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		// no parameters
		Map<String, Object> ret = new HashMap<>();
		return ret;
	}
	
}
