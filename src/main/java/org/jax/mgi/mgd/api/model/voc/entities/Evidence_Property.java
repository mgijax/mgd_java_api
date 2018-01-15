package org.jax.mgi.mgd.api.model.voc.entities;

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
@ApiModel(value = "Evidence Property Model Object")
@Table(name="voc_evidence_property")
public class Evidence_Property extends EntityBase {

	@Id
	private Integer _evidenceProperty_key;
	private Integer stanza;
	private Integer sequenceNum;
	private String value;
	private Date creation_date;
	private Date modification_date;
	
}
