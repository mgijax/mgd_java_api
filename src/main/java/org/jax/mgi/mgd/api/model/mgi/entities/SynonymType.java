package org.jax.mgi.mgd.api.model.mgi.entities;

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
@ApiModel(value = "Synonym Type Object")
@Table(name="mgi_synonymtype")
public class SynonymType extends EntityBase {
	@Id
	private Integer _synonymType_key;
	private String synonymType;
	private String definition;
	private Integer allowOnlyOne;
	private Date creation_date;
	private Date modification_date;

}
