package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Immutable
@ApiModel(value = "Relationship Allele/Marker FEAR")
@Table(name="all_relationship_fear_view")
public class RelationshipFEAR extends BaseEntity {
	
	@Id
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mgi_relationship_fear_generator")
//	@SequenceGenerator(name="mgi_relationship_fear_generator", sequenceName = "mgi_relationship_seq", allocationSize=1)	
	private int _relationship_key;
	private int _allele_key;
	private int _marker_key;
	private int _organism_key;
	private String symbol;
	private String commonname;
  
}
