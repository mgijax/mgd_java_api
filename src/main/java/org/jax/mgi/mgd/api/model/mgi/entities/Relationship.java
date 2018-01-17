package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Relationship Property Object")
@Table(name="mgi_relationship_property")
public class Relationship extends EntityBase {
	@Id
	private Integer _relationship_key;
	
}
