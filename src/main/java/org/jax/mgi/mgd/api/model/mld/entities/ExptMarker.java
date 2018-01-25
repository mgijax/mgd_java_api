package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Expt_Marker Model Object")
@Table(name="mld_expt_marker")
public class ExptMarker extends EntityBase {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private String gene;
	private String description;
	private Integer matrixData;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Marker marker;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_allele_key", referencedColumnName="_allele_key")
	private Allele allele;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_assay_key", referencedColumnName="_assay_key")
	private Assay assay;
	
}
