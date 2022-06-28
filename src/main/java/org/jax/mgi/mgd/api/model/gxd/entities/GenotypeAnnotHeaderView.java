package org.jax.mgi.mgd.api.model.gxd.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Genotype Annotation Header View Entity Object")
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
