package org.jax.mgi.mgd.api.model.mrk.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Marker Utilities Form")
public class MarkerUtilitiesForm extends BaseSearchForm {

	private String eventKey;
	private String eventReasonKey;
	private String oldKey;
	private String refKey;
	private String addAsSynonym;
	private String newName;
	private String newSymbol;
	private String newKey;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();

		ret.put("eventKey", eventKey);
		ret.put("eventReasonKey", eventReasonKey);
		ret.put("oldKey", oldKey);
		ret.put("refKey", refKey);
		ret.put("addAsSynonym", addAsSynonym);
		ret.put("newName", newName);
		ret.put("newSymbol", newSymbol);
		ret.put("newKey", newKey);

		return ret;
	}
	
}
