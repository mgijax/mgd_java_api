package org.jax.mgi.mgd.api.model.voc.entities;

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
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Evidence Model Object")
@Table(name="voc_evidence")
public class Evidence extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voc_evidence_generator")
	@SequenceGenerator(name="voc_evidence_generator", sequenceName = "voc_evidence_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _annotevidence_key;
	private int _annot_key;
	private String inferredFrom;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_evidenceterm_key", referencedColumnName="_term_key")
	private Term evidenceTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// MP-Sex-Specificity
	@OneToMany()
	@JoinColumn(name="_annotevidence_key", referencedColumnName="_annotevidence_key", insertable=false, updatable=false)
	@Where(clause="`_propertyterm_key` = 8836535")
	private List<EvidenceProperty> mpSexSpecificity;
		
	// General
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_annotevidence_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 25")
	private List<Note> allNotes;

	
}
