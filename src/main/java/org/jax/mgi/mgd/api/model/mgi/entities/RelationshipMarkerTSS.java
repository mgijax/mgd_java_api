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
@ApiModel(value = "Relationship Marker TSS Object")
@Table(name="mgi_relationship_markertss_view")
public class RelationshipMarkerTSS extends BaseEntity {
	
	@Id
	private int _relationship_key;
	private int _object_key_1;
	private int _object_key_2;
	private String marker1;
	private String marker2;
  
}
