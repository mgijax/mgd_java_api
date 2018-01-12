package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Relationship Property Object")
@Table(name="mgi_relationship_property")
public class Relationship_Property extends EntityBase {
	@Id
	private Integer _relationship_Property_key;
	private String value;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;

}
