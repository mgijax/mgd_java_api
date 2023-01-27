package org.jax.mgi.mgd.api.model.seq.entities;

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
@ApiModel(value = "Sequence Entity Object")
@Table(name="seq_sequence")
public class Sequence extends BaseEntity {

	@Id
	private int _sequence_key;
	private int _sequencetype_key;
	private int _sequencequaliy_key;
	private int _sequencestatus_key;
	private int _sequenceprovider_key;
	private int _organism_key;
	private int length;
	private String description;
	private String version;
	private String division;
	private int virtual;
	private int numerOfOrganisms;
	private Date seqrecord_date;
	private Date sequence_date;
	private int _createdby_key;
	private int _modifiedby_key;
	private Date creation_date;
	private Date modification_date;  
}
