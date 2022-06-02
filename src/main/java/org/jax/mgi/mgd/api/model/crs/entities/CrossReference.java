package org.jax.mgi.mgd.api.model.crs.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Reference Model Object")
@Table(name="crs_references")
public class CrossReference extends BaseEntity {

	@EmbeddedId
	private CrossReferenceKey key;
	private Date creation_date;
	private Date modification_date;
}
