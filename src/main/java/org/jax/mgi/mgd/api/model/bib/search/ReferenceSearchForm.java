package org.jax.mgi.mgd.api.model.bib.search;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Reference Search Form")
public class ReferenceSearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		return null;
	}
	
}
