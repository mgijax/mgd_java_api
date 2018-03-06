package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "EventReason Model Object")
@Table(name="mrk_eventreason")
public class EventReason extends BaseEntity {

	@Id
	private Integer _marker_eventreason_key;
	private String eventReason;
	private Date creation_date;
	private Date modification_date;
}
