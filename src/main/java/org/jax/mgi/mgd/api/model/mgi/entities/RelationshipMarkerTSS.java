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
@Schema(description = "Relationship Marker TSS Object")
@Table(name="mgi_relationship_markertss_view")
public class RelationshipMarkerTSS extends BaseEntity {
	
	@Id
	private int _relationship_key;
	private int _object_key_1;
	private int _object_key_2;
	private String marker1;
	private String marker2;
  
}
