package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Assay Type Model Object")
@Table(name="gxd_assaytype")
public class AssayType extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_assaytype_generator")
	@SequenceGenerator(name="gxd_assaytype_generator", sequenceName = "gxd_assaytype_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _assaytype_key;
	private String assayType;
	@Column(columnDefinition = "int2")
	private Integer isRNAAssay;
	@Column(columnDefinition = "int2")
	private Integer isGelAssay;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
}
