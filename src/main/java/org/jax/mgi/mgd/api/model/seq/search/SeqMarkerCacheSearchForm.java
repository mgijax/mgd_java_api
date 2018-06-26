package org.jax.mgi.mgd.api.model.seq.search;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("SeqMarkerCache Search Form")
public class SeqMarkerCacheSearchForm extends BaseSearchForm {

	@Override
	public Map<String, Object> getSearchFields() {
		return null;
	}
	
}
