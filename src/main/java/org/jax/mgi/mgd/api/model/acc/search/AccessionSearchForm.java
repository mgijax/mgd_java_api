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
	private Integer _logicaldb_key;
	private Integer _mgitype_key;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		if(accid != null) { ret.put("accID", accid); }
		if(_logicaldb_key != null) { ret.put("_logicaldb_key", _logicaldb_key); }
		if(_mgitype_key != null) { ret.put("_mgitype_key", _mgitype_key); }
		return ret;
	}
	
}
