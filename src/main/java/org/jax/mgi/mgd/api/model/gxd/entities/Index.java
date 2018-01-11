package org.jax.mgi.mgd.api.model.gxd.entities;

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
@ApiModel(value = "Index Model Object")
@Table(name="gxd_index")
public class Index extends EntityBase {

	@Id
	private Integer _index_key;
	private String comments;
	private Date creation_date;
	private Date modification_date;
}
