package org.jax.mgi.mgd.api.model.voc.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Term Search Form")
public class TermSearchForm extends BaseSearchForm {
	
	@ApiModelProperty(value="Vocab Name to Search for")
	private String vocabName;
	@ApiModelProperty(value="Search for Term by term")
	private String term;
	@ApiModelProperty(value="Accession Id to Search for")
	private String accid;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();

		if(vocabName != null) { ret.put("vocab.name", vocabName); }
		if(term != null) { ret.put("term", term); }
		if(accid != null) { ret.put("mgiTermAccessionId.accID", accid); }
		
		return ret;
	}

}
