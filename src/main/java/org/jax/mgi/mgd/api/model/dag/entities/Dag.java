package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "DAG Model Object")
@Table(name="dag_dag")
public class Dag extends EntityBase {

	@Id
	private Integer _dag_key;
	private String name;
	private String abbreviation;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
	@OneToOne
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	// as of 01/19/2018 each dag only exists in one vocabulary
	// vocabularies should only have one entry
	@ManyToMany
	@JoinTable(name = "voc_vocabdag",
		inverseJoinColumns = @JoinColumn(name = "_vocab_key"),
		joinColumns = @JoinColumn(name = "_dag_key")
	)
	private Set<Vocabulary> vocabularies;
	
}
