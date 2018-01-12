package org.jax.mgi.mgd.api.model.mrk.entities;

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
@ApiModel(value = "BiotypeMapping Model Object")
@Table(name="mrk_biotypemapping")
public class BiotypeMapping extends EntityBase {

	@Id
	private Integer _biotypemapping_key;
	private Integer useMCVchildren;
	private Date creation_date;
	private Date modification_date;
}
