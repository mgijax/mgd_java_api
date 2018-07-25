package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonymType;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Synonym Model Object")
public class MGISynonymDomain extends BaseDomain {

	private Integer synonymKey;
	private Integer _object_key;
	private String synonym;
	private Date creation_date;
	private Date modification_date;
	private String mgiType;
	private String synonymType;

	// the synonym reference can be null, so this isn't working
	//private String JNum = "";
	
	private String createdBy;
	private String modifiedBy;
}   	