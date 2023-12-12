package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

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
@Schema(description = "Mapping Assay Types Object")
@Table(name="mld_assay_types")
public class MappingAssayType extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mld_assay_types_generator")
	@SequenceGenerator(name="mld_assay_types_generator", sequenceName = "mld_assay_types_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _assay_type_key;
	private String description;
	private Date creation_date;
	private Date modification_date;

}
