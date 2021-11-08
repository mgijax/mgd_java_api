package org.jax.mgi.mgd.api.model.dag.domain;

import java.util.Date;

import javax.persistence.Id;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Dag Domain")
public class DagDomain extends BaseDomain{

	@Id	
	private String dagKey;
	private String name;
	private String abbreviation;
	private Date creation_date;
	private Date modification_date;

	private Reference reference;
	
	private MGIType mgiType;
}	
