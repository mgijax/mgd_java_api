package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.annotation.concurrent.Immutable;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Immutable
@Schema(description = "Relationship Allele/Driver Gene")
@Table(name="all_allele_driver_view")
public class RelationshipAlleleDriverGene extends BaseEntity {
	
	@Id	
	private int _relationship_key;
	private int _allele_key;
	private int _marker_key;
	private int _organism_key;
	private String symbol;
	private String commonname;
  
}
