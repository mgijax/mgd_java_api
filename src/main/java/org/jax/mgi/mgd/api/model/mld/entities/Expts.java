package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Expts Object")
@Table(name="mld_expts")
public class Expts extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mld_expts_generator")
	@SequenceGenerator(name="mld_expts_generator", sequenceName = "mld_expts_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _expt_key;
	private String exptType;
	private Integer tag;
	private String chromosome;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private Reference reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_expt_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 4 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;
	
//	@OneToMany
//	@JoinColumn(name="_expt_key", insertable=false, updatable=false)
//	private List<ExptMarker> exptMarkers;
//	
//	@OneToMany
//	@JoinColumn(name="_marker_key", insertable=false, updatable=false)
//	private List<ExptMarker> expt;
}
