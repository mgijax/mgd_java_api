package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference Note Model Object")
@Table(name="bib_notes")
public class ReferenceNote extends Base {
	@Id
	@Column(name="_refs_key")
	public int _refs_key;

	@Column(name="sequenceNum")
	public int sequenceNum;

	@Column(name="note")
	public String note;
}
