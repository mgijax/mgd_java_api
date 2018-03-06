package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Synonym Type Object")
@Table(name="mgi_synonymtype")
public class MGISynonymType extends BaseEntity {
	@Id
	private Integer _synonymType_key;
	private String synonymType;
	private String definition;
	private Integer allowOnlyOne;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	@OneToOne
	@JoinColumn(name="_organism_key")
	private Organism organism;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
