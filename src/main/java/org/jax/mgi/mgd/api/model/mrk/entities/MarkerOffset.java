package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Offset Model Object")
@Table(name="mrk_offset")
public class MarkerOffset extends EntityBase {

	@EmbeddedId
	private MarkerOffsetKey key;
	private Integer cmOffset;
	private Date creation_date;
	private Date modification_date;

}
