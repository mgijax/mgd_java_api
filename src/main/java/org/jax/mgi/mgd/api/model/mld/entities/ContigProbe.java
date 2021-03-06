package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "ContigProbe Model Object")
@Table(name="mld_contigprobe")
public class ContigProbe extends BaseEntity {

	@Id
	private Integer _contig_key;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_probe_key")
	private Probe probe;
	
}
