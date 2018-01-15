package org.jax.mgi.mgd.api.model.ri.entities;

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
@ApiModel(value = "RI_RISet Model Object")
@Table(name="ri_riset")
public class RISet extends EntityBase {

	@Id
	private Integer _riset_key;
	private String designation;
	private String abbrev1;
	private String abbrev2;
	private String RI_IdList;
	private Date creation_date;
	private Date modification_date;
}
