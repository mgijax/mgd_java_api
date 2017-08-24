package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Term Model Object")
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
	
	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;

	@OneToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	public User createdBy;
	
	@OneToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	public User modifiedBy;
	
//	@OneToOne(targetEntity=Vocabulary.class, fetch=FetchType.EAGER)
//	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
//	public Vocabulary vocab;
}
