package org.jax.mgi.mgd.api.model.ri.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "RI_Summary Model Object")
@Table(name="ri_summary")
public class RISummary extends BaseEntity {

	@Id
	private int _risummary_key;
	private String summary;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_riset_key")
	private RISet riSet;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
	@OneToMany
	@JoinColumn(name="_risummary_key")
	private Set<RISummaryExptRef> riSummaryExperiments;
}
