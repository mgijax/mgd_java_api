package org.jax.mgi.mgd.api.model.voc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Evidence Model Object")
@Table(name="voc_evidence")
public class Evidence extends EntityBase {

	@Id
	private Integer _annotevidence_key;

}
