package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.DomainBase;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGITypeDomain extends DomainBase {

	private Integer _mgitype_key;
	private String name;
	private String tableName;
	private String primaryKeyName;
	private String identityColumnName;
	private String dbView;
	private Date creation_date;
	private Date modification_date;
	private List<OrganismDomain> organisms;
}
