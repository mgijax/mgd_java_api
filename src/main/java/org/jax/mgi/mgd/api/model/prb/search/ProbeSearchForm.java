package org.jax.mgi.mgd.api.model.prb.search;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Probe Search Form")
public class ProbeSearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		return null;
	}
	
}
