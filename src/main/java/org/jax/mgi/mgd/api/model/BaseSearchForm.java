package org.jax.mgi.mgd.api.model;

import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Schema(description="Base Search Form")
public abstract class BaseSearchForm {

	@Schema(hidden=true)
	protected String orderBy;
	@Schema(hidden=true)
	protected Integer pageNum;
	@Schema(hidden=true)
	protected Integer pageSize;
	@Schema(hidden=true)
	protected Integer searchDepth = 1;
	
	// <Database Field, Value to Search for>
	@Schema(hidden=true)
	public abstract Map<String, Object> getSearchFields();
	
}
