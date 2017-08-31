package org.jax.mgi.mgd.api.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Vocabulary Term Model Object")
@Table(name="voc_vocab")
public class Vocabulary extends EntityBase {

	@Id
	@Column(name="_vocab_key")
	private Integer _vocab_key;

	@Column(name="name")
	private String name;

	@JsonIgnore
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	private List<Term> terms;
}

