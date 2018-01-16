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
@ApiModel(value = "Reference Association Type Object")
@Table(name="mgi_refassoctype")
public class RefAssocType extends EntityBase {
	@Id
	private Integer _refAssocType_key;
	private String assocType;
	private Integer allowOnlyOne;
	private Date creation_date;
	private Date modification_date;

}
