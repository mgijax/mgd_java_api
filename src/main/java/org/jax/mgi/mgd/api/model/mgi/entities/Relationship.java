package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Relationship Object")
@Table(name="mgi_relationship")
public class Relationship extends BaseEntity {
	
	@Id
	private Integer _relationship_key;
	private Integer _object_key_1;
	private Integer _object_key_2;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_category_key")
	private RelationshipCategory category;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_relationshipterm_key", referencedColumnName="_term_key")
	private Term relationshipTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_qualifier_key", referencedColumnName="_term_key")
	private Term qualifierTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_evidence_key", referencedColumnName="_term_key")
	private Term evidenceTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;
	  
	//@OneToMany
    //@JoinColumn(name="_object_key_1", referencedColumnName="_marker_key", insertable=false, updatable=false)
	//@Where(clause="`_category_key` = 1008")
	//private List<Marker> tssToMarker;
	
	//@OneToMany
    //@JoinColumn(name="_object_key_2", referencedColumnName="_marker_key", insertable=false, updatable=false)
	//@Where(clause="`_category_key` = 1008")
	//private List<Marker> geneToMarker;
	
}
