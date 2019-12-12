package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LogicalDbDomain extends BaseDomain {

	private String logicalDBKey;
	private String name;
	private String description;
	private String organismKey;
	private String commonName;
	
	private String createdBy;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	
	private List<ActualDbDomain> actualDBs;

}
