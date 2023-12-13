package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Expt Marker Object")
@Table(name="mld_expt_marker")
public class ExptMarker extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mld_expt_marker_generator")
	@SequenceGenerator(name="mld_expt_marker_generator", sequenceName = "mld_expt_marker_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _assoc_key;
	private int _expt_key;
	private Integer sequenceNum;
	private String description;
	@Column(columnDefinition = "int2")
	private Integer matrixData;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_assay_type_key")
	private MappingAssayType assayType;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "_marker_key")
	private Marker marker;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key")
	private Allele allele;
	
}
