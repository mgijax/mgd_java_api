package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Vocabulary Term Model Object")
@Table(name="voc_term")
public class Term extends Base {

	@Id
	@Column(name="_term_key")
	public Long _term_key;

	@Column(name="term")
	public String term;

	@Column(name="abbreviation")
	public String abbreviation;

	@Column(name="sequenceNum")
	public Integer sequenceNum;

	@Column(name="isObsolete")
	public Integer isObsolete;
	
	@Column(name="_vocab_key")
	public Long _vocab_key;
	
	@Column(name="creation_date")
	public Date creation_date;

}
