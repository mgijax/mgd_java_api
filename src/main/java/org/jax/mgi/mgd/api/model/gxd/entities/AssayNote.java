package org.jax.mgi.mgd.api.model.gxd.entities;

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
@ApiModel(value = "Assay Note Model Object")
@Table(name="gxd_assaynote")
public class AssayNote extends BaseEntity {

	@Id
	private Integer _assay_key;
	private String assayNote;
	private Date creation_date;
	private Date modification_date;
}
