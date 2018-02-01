package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Synonym Object")
@Table(name="mgi_synonym")
public class MGISynonym extends EntityBase {
	@Id
	private Integer _synonym_key;
	private String synonym;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_synonymtype_key")
	private MGISynonymType synonymType;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
