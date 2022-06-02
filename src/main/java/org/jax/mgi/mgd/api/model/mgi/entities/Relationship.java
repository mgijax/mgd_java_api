package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
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
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mgi_relationship_generator")
	@SequenceGenerator(name="mgi_relationship_generator", sequenceName = "mgi_relationship_seq", allocationSize=1)	
	private int _relationship_key;
	private int _object_key_1;
	private int _object_key_2;
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
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// relationship/property
	@OneToMany()
	@JoinColumn(name="_relationship_key", insertable=false, updatable=false)
	private List<RelationshipProperty> properties;

	//  1042 | Relationship
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_relationship_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 40 and `_notetype_key` = 1042")
	private List<Note> note;	
}
