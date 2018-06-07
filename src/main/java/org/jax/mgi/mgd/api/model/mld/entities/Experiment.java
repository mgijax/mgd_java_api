package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Expts Model Object")
@Table(name="mld_expts")
public class Experiment extends BaseEntity {

	@Id
	private Integer _expt_key;
	private String exptType;
	private Integer tag;
	private String chromosome;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_expt_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 4 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany
	@JoinColumn(name="_expt_key")
	private Set<Hit> hits;
	
	@OneToMany
	@JoinColumn(name="_expt_key")
	private Set<ExptMarker> exptMarkers;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<ExptMarker> expt;
}
