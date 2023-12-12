package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Marker Type Entity Object")
@Table(name="mrk_types")
public class MarkerType extends BaseEntity {

	@Id
	private Integer _marker_type_key;
	private String name;
	private Date creation_date;
	private Date modification_date;
}
