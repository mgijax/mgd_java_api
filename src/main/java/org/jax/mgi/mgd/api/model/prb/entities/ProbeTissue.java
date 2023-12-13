package org.jax.mgi.mgd.api.model.prb.entities;

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
@Schema(description = "Tissue Model Object")
@Table(name="prb_tissue")
public class ProbeTissue extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_tissue_generator")
    @SequenceGenerator(name="prb_tissue_generator", sequenceName = "prb_tissue_seq", allocationSize=1)
    @Schema(name="primary key")

	private int _tissue_key;
	private String tissue;
	@Column(columnDefinition = "int2")
	private int standard;
	private Date creation_date;
	private Date modification_date;
}
