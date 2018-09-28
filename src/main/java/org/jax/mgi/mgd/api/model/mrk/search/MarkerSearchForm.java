package org.jax.mgi.mgd.api.model.mrk.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Marker Search Form")
public class MarkerSearchForm extends BaseSearchForm {

	@ApiModelProperty(value="Marker symbol to search for")
	private String symbol;

	@ApiModelProperty(value="Marker name to search for")
	private String name;

	@ApiModelProperty(value="Chromosome to search for")
	private String chromosome;

	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();

		if(symbol != null) { ret.put("symbol", symbol); }
		if(name != null) { ret.put("name", name); }
		if(chromosome != null) { ret.put("chromosome", chromosome); }
		return ret;
	}
	
}
