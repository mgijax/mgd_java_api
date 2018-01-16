package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe Model Object")
@Table(name="prb_probe")
public class Probe extends EntityBase {

	@Id
	private Integer _probe_key;
	private String name;
	private Integer derivedFrom;
	private String primer1sequence;
	private String primer2sequence;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String productSize;
	private Date creation_date;
	private Date modification_date;


	// Complex Many to Many
	@OneToMany(targetEntity=ProbeMarker.class, fetch=FetchType.EAGER)
	private List<ProbeMarker> probeMarkers;
}
