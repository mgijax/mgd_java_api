package org.jax.mgi.mgd.api.model.prb.entities;

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
@ApiModel(value = "PRBRef_Notes Model Object")
@Table(name="prb_ref_notes")
public class ProbeReferenceNote extends BaseEntity {

	@Id
	private int _reference_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
