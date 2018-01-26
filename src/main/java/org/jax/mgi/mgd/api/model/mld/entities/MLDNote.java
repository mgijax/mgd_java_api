package org.jax.mgi.mgd.api.model.mld.entities;

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
@ApiModel(value = "MLD Notes Model Object")
@Table(name="mld_notes")
public class Notes extends EntityBase {

	@Id
	private Integer _refs_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
