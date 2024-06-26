package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
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
@Schema(description = "Probe Model Object")
@Table(name="prb_probe")
public class Probe extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_probe_generator")
	@SequenceGenerator(name="prb_probe_generator", sequenceName = "prb_probe_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _probe_key;
	
	private String name;
	private String primer1sequence;
	private String primer2sequence;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String productSize;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="derivedFrom")
	private Probe derivedFrom;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ampPrimer")
	private Probe ampPrimer;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_segmenttype_key", referencedColumnName="_term_key")
	private Term segmentType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term vectorType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_source_key")
	private ProbeSource probeSource;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_probe_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 3 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// markers
	@OneToMany()
	@JoinColumn(name="_probe_key", insertable=false, updatable=false)
	private List<ProbeMarker> markers;
	
	// references
	@OneToMany()
	@JoinColumn(name="_probe_key", insertable=false, updatable=false)
	private List<ProbeReference> references;

	// General note
	@OneToMany()
	@JoinColumn(name="_probe_key", insertable=false, updatable=false)
	private List<ProbeNote> generalNote;

	// Raw Sequence note
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_probe_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 3 and `_notetype_key` = 1037")
	private List<Note> rawsequenceNote;
	
}
