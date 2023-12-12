package org.jax.mgi.mgd.api.model.gxd.entities;

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
@Schema(description = "TheilerStage Model Object")
@Table(name="gxd_theilerstage")
public class TheilerStage extends BaseEntity {

	@Id
	private Integer _stage_key;
	private Integer stage;
	private String description;
	private Integer dpcMin;
	private Integer dpcMax;
	private Date creation_date;
	private Date modification_date;
}
