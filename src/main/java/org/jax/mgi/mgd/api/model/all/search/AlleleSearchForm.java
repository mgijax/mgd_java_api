package org.jax.mgi.mgd.api.model.all.search;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Allele Search Form")
public class AlleleSearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		return null;
	}
	
}
