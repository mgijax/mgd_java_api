package org.jax.mgi.mgd.api.model.prb.entities;

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
@ApiModel(value = "PRBReference Model Object")
@Table(name="prb_reference")
public class Reference extends EntityBase {

	@Id
	private Integer _reference_key;
	private Integer hasRmap;
	private Integer hasSequence;
	private Date creation_date;
	private Date modification_date;
}
