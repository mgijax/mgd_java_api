package org.jax.mgi.mgd.api.model;

import java.util.Map;

import org.jax.mgi.mgd.api.model.voc.search.TermSearchForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel(value="Base Search Form", subTypes= {TermSearchForm.class})
public abstract class BaseSearchForm {

	@ApiModelProperty(hidden=true)
	protected String orderBy;
	@ApiModelProperty(hidden=true)
	protected Integer pageNum;
	@ApiModelProperty(hidden=true)
	protected Integer pageSize;
	
	// <Database Field, Value to Search for>
	@ApiModelProperty(hidden=true)
	public abstract Map<String, Object> getSearchFields();
	
}
