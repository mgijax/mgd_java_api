package org.jax.mgi.mgd.api.model.gxd.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Genotype Annotation Header View Entity Object")
@Table(name="gxd_genotypeannotheader_view")
public class GenotypeAnnotHeaderView extends BaseEntity {
		
	@Id
	private int headerTermKey;
	private int annotKey;
	private String headerTerm;
	private int termKey;
	private String term;
	private int termSequenceNum;
	private int headerSequenceNum;
}
