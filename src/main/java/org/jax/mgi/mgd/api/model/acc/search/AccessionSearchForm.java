package org.jax.mgi.mgd.api.model.acc.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Accession Search Form")
public class AccessionSearchForm extends BaseSearchForm {

	@ApiModelProperty(value="Accession Id to Search for")
	private String accid;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		if(accid != null) { ret.put("accID", accid); }
		return ret;
	}
	
}
