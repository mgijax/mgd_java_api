package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Immutable
@ApiModel(value = "Relationship Allele/Driver Gene")
@Table(name="all_allele_driver_view")
public class RelationshipAlleleDriverGene extends BaseEntity {
	
	@Id
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mgi_relationship_drivergene_generator")
//	@SequenceGenerator(name="mgi_relationship_drivergene_generator", sequenceName = "mgi_relationship_seq", allocationSize=1)	
	private int _relationship_key;
	private int _allele_key;
	private int _marker_key;
	private int _organism_key;
	private String markerSymbol;
	private String commonName;
  
}
