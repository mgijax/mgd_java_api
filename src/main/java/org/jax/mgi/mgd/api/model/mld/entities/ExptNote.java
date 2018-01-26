package org.jax.mgi.mgd.api.model.mld.entities;

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
@ApiModel(value = "MLD Expt_Notes Model Object")
@Table(name="mld_expt_notes")
public class ExptNote extends EntityBase {

	@Id
	private Integer _expt_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
