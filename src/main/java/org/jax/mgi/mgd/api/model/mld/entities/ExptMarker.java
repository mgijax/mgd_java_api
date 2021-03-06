package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Expt_Marker Model Object")
@Table(name="mld_expt_marker")
public class ExptMarker extends BaseEntity {

	@EmbeddedId
	private ExptMarkerKey key;
	private String gene;
	private String description;
	private Integer matrixData;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_assay_type_key")
	private MLDAssayType assayType;
	
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "_marker_key", insertable = false, updatable = false)
	private Marker marker;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key", insertable = false, updatable = false)
	private Allele allele;
	
}
