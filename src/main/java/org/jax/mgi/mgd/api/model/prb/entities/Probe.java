package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe Model Object")
@Table(name="prb_probe")
public class Probe extends BaseEntity {

	@Id
	private int _probe_key;
	private String name;
	private int derivedFrom;
	private String primer1sequence;
	private String primer2sequence;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String productSize;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_source_key")
	private ProbeSource source;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term vector;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_segmenttype_key", referencedColumnName="_term_key")
	private Term segmentType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_probe_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 3 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany()
	@JoinColumn(name="_probe_key")
	private List<ProbeMarker> probeMarkers;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_probe_key")
	@Where(clause="`_mgitype_key` = 3 AND preferred = 1")
	private List<Accession> allAccessionIds;
	
}
