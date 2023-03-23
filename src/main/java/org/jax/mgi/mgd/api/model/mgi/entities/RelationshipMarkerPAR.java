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
@ApiModel(value = "Relationship Marker PAR Object")
@Table(name="mgi_relationship_markerpar_view")
public class RelationshipMarkerPAR extends BaseEntity {
	
	@Id
	private int _relationship_key;
	private int _object_key_1;
	private String marker;
  
}
