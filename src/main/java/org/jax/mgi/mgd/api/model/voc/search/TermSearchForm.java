package org.jax.mgi.mgd.api.model.voc.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.SearchForm;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TermSearchForm extends SearchForm {

	private String vocabName;
	private String term;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();

		if(vocabName != null) { ret.put("vocab.name", vocabName); }
		if(term != null) { ret.put("term", term); }
		
		return ret;
	}

}
