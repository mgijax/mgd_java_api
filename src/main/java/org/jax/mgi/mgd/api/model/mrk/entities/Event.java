package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Event Model Object")
@Table(name="mrk_event")
public class Event extends EntityBase {

	@Id
	private Integer _marker_event_key;
	private String event;
	private Date creation_date;
	private Date modification_date;
}
