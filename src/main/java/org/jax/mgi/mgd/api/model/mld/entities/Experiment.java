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
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Expts Model Object")
@Table(name="mld_expts")
public class Experiment extends EntityBase {

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
	@JoinColumn(name="_expts_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 4 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="_expt_key")
	private Set<Hit> hits;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="_expt_key")
	private Set<ExptMarker> markers;
}
