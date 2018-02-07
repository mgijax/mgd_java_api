package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "CellLine Derivation Model Object")
@Table(name="all_cellline_derivation")
public class AlleleCellLineDerivation extends EntityBase {

	@Id
	private Integer _derivation_key;
	private String name;
	private String description;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term cellLineVector;
	
	@OneToOne
	@JoinColumn(name="_parentcellline_key", referencedColumnName="_cellline_key")
	private AlleleCellLine parentCellLine;
	
	@OneToOne
	@JoinColumn(name="_derivationtype_key", referencedColumnName="_term_key")
	private Term derivationType;
	
	@OneToOne
	@JoinColumn(name="_creator_key", referencedColumnName="_term_key")
	private Term cellLineCreator;
	
	@OneToOne
	@JoinColumn(name="_refs_key" )
	private Reference reference;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
