package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Evidence Model Object")
@Table(name="voc_evidence")
public class Evidence extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voc_evidence_generator")
	@SequenceGenerator(name="voc_evidence_generator", sequenceName = "voc_evidence_seq", allocationSize=1)
	@Schema(name="primary key")
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
	private ReferenceCitationCache reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToMany()
	@JoinColumn(name="_annotevidence_key", referencedColumnName="_annotevidence_key", insertable=false, updatable=false)
	private List<EvidenceProperty> properties;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_annotevidence_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 25")
	private List<Note> allNotes;
	
}
