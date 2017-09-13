package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Note Model Object")
@Table(name="bib_notes")
public class ReferenceNote extends EntityBase {
	@Id
	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="sequenceNum")
	private int sequenceNum;

	@Column(name="note")
	private String note;

	@Column(name="creation_date")
	private Date creation_date;
	
	@Column(name="modification_date")
	private Date modification_date;
}
