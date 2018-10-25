package org.jax.mgi.mgd.api.model.mgi.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

public class NoteSearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		// no parameters
		Map<String, Object> ret = new HashMap<>();
		return ret;
	}

}
