package org.jax.mgi.mgd.api.model.mrk.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Marker Utilities Rename Form")
public class MarkerUtilitiesRenameForm extends BaseSearchForm {

	@ApiModelProperty(value="Event key")
	private String eventKey;

	@ApiModelProperty(value="Event Reason key")
	private String eventReasonKey;

	@ApiModelProperty(value="Old Marker key")
	private String oldKey;
	
	@ApiModelProperty(value="Refs key")
	private String refKey;
	
	@ApiModelProperty(value="Add As Synonym (0/1)")
	private String addAsSynonym;
	
	@ApiModelProperty(value="New Name")
	private String newName;
	
	@ApiModelProperty(value="New Symbol")
	private String newSymbols;
	
	@ApiModelProperty(value="New Key")
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
		ret.put("newSymbols", newSymbols);
		ret.put("newKey", newKey);

		return ret;
	}
	
}
