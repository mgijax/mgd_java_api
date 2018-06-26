package org.jax.mgi.mgd.api.model.seq.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Sequence Model Object")
public class SequenceDomain extends BaseDomain {

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
