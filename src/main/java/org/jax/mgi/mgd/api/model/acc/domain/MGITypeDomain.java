package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGITypeDomain extends BaseDomain {

	private String MgiTypeKey;
	private String name;
	private String tableName;
	private String primaryKeyName;
	private String identityColumnName;
	private String dbView;
	private Date creation_date;
	private Date modification_date;
	private List<OrganismDomain> organisms;
}
