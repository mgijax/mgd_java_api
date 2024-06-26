package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

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
@Schema(description = "CellLine Derivation Model Object")
@Table(name="all_cellline_derivation")
public class AlleleCellLineDerivation extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_cellline_derivation_generator")
	@SequenceGenerator(name="all_cellline_derivation_generator", sequenceName = "all_cellline_derivation_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _derivation_key;
	private String name;
	private String description;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term vector;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vectortype_key", referencedColumnName="_term_key")
	private Term vectorType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_parentcellline_key", referencedColumnName="_cellline_key")
	private CellLine parentCellLine;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_derivationtype_key", referencedColumnName="_term_key")
	private Term derivationType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_creator_key", referencedColumnName="_term_key")
	private Term creator;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key" )
	private ReferenceCitationCache reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	//  1033 | General
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_derivation_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 36 and `_notetype_key` = 1033")
	private List<Note> generalNote;	
}
