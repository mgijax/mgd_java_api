package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
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
@Schema(description = "Probe Source Model Object")
@Table(name="prb_source")
public class ProbeSource extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_source_generator")
    @SequenceGenerator(name="prb_source_generator", sequenceName = "prb_source_seq", allocationSize=1)
    @Schema(name="primary key")

	private int _source_key;
	private String name;
	private String description;
	private String age;
	private int ageMin;
	private int ageMax;
	private int isCuratorEdited;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_segmenttype_key", referencedColumnName="_term_key")
	private Term segmentType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term vector;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_tissue_key")
	private ProbeTissue tissue;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_cellline_key", referencedColumnName="_term_key")
	private Term cellLine;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gender_key", referencedColumnName="_term_key")
	private Term gender;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_source_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 5")
	private List<Accession> accessionIds;
	
}
