package org.jax.mgi.mgd.api.model.all.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Allele Search Form")
public class AlleleSearchForm extends BaseSearchForm {

	@ApiModelProperty(value="Allele symbol to search for")
	private String symbol;

	@ApiModelProperty(value="Allele name to search for")
	private String name;

	@ApiModelProperty(value="Allele Type to search for")
	private String alleletype;
	
	@ApiModelProperty(value="Allele Status to search for")
	private String allelestatus;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		
		if(symbol != null) { ret.put("symbol", symbol); }
		if(name != null) { ret.put("name", name); }
		if(alleletype != null) {ret.put("alleletype", alleletype);}
		if(allelestatus != null) {ret.put("allelestatus", allelestatus);}
		return ret;
	}
	
}
