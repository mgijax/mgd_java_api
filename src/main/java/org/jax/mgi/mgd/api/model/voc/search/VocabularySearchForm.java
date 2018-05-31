package org.jax.mgi.mgd.api.model.voc.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Vocabulary Search Form")
public class VocabularySearchForm extends BaseSearchForm {

	@ApiModelProperty(value="Vocab Name to Search for")
	private String vocabName;
	@ApiModelProperty(value="Search for Private Vocabularies only")
	private String isPrivate;
	@ApiModelProperty(value="Search for Vocabularies by a term")
	private String term;
	
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();
		
		if(vocabName != null) { ret.put("name", vocabName); }
		if(isPrivate != null) { ret.put("isprivate", isPrivate); }
		if(term != null) { ret.put("terms.term", term); }
		
		return ret;
	}
}
