package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Vocabulary Model Object")
@Table(name="voc_vocab")
public class Vocabulary extends BaseEntity {

	@Id
	private int _vocab_key;
	private Integer isSimple;
	private Integer isPrivate;
	private String name;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_logicaldb_key")
	private LogicalDB logicalDB;

	@OneToMany
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	private List<Term> terms;
	
}