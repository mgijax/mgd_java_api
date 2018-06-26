package org.jax.mgi.mgd.api.model.gxd.search;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Index Search Form")
public class IndexSearchForm extends BaseSearchForm {

	@ApiModelProperty(value="Accession Id to Search for")
	private String accid;
	
	@Override
	public Map<String, Object> getSearchFields() {
		// TODO Auto-generated method stub
				return null;
	}
	
}
