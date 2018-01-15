package org.jax.mgi.mgd.api.model.seq.entities;

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
@ApiModel(value = "Sequence Model Object")
@Table(name="seq_sequence")
public class Sequence extends EntityBase {

	@Id
	private Integer _sequence_key;
	private Integer length;
	private String description;
	private String version;
	private String division;
	private Integer virtual;
	private Integer numberOfOrganisms;
	private Date seqrecord_date;
	private Date sequence_date;
	private Date creation_date;
	private Date modification_date;
}
