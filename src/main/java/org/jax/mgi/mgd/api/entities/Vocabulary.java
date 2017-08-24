package org.jax.mgi.mgd.api.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Vocabulary Term Model Object")
@Table(name="voc_vocab")
public class Vocabulary extends Base {

	@Id
	@Column(name="_vocab_key")
	public Long _vocab_key;

	@Column(name="name")
	public String name;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
    public List<Term> terms;
}

