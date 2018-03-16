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
@ApiModel(value = "MRKNotes Model Object")
@Table(name="mrk_notes")
public class MarkerNote extends BaseEntity {

	@Id
	private Integer _marker_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
