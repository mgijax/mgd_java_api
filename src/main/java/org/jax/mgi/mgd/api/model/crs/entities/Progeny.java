package org.jax.mgi.mgd.api.model.crs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Progeny Model Object")
@Table(name="crs_progeny")
public class Progeny extends EntityBase {

	@EmbeddedId
	private ProgenyKey key;
	private String name;
	private String sex;
	private String notes;
	private Date creation_date;
	private Date modification_date;
	
	@Getter @Setter
	@Embeddable
	public class ProgenyKey implements Serializable {
		private Integer _cross_key;
		private Integer sequenceNum;
	}
}
