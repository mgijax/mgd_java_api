package org.jax.mgi.mgd.api.model.bib.entities;

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
@ApiModel(value = "Reference Note Model Object")
@Table(name="bib_notes")
public class ReferenceNote extends BaseEntity {
	
	@Id
	private int _refs_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
