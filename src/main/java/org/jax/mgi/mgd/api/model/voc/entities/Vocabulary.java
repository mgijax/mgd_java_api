package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

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
	private Integer _vocab_key;
	Integer isSimple;
	Integer isPrivate;
	private String name;
	private Date creation_date;
	private Date modification_date;

	@JsonIgnore
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	private List<Term> terms;
}