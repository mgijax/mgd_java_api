package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;

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
@Schema(description = "Expts Object")
@Table(name="mld_expts")
public class Expts extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mld_expts_generator")
	@SequenceGenerator(name="mld_expts_generator", sequenceName = "mld_expts_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _expt_key;
	private String exptType;
	private Integer tag;
	private String chromosome;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;
	
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_expt_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 4 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// markers
	@OneToMany
	@JoinColumn(name="_expt_key", insertable=false, updatable=false)
	private List<ExptMarker> markers;
	
	// note
	@OneToMany()
	@JoinColumn(name="_expt_key", insertable=false, updatable=false)
	private List<ExptNote> exptNote;
	
}
