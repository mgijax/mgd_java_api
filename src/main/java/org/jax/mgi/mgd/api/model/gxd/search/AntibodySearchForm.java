package org.jax.mgi.mgd.api.model.gxd.search;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Antibody Search Form")
public class AntibodySearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
