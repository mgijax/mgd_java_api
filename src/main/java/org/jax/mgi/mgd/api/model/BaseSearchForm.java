package org.jax.mgi.mgd.api.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class BaseSearchForm {

	protected String orderBy;
	protected Integer pageNum;
	protected Integer pageSize;
	
	// <Database Field, Value to Search for>
	public abstract Map<String, Object> getSearchFields();
	
}
