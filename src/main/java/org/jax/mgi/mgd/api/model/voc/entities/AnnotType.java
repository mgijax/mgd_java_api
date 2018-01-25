package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Annotation Type Model Object")
@Table(name="voc_annottype")
public class AnnotType extends EntityBase {

	@Id
	private Integer _annotType_key;
	private String name;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_mgitype_key", referencedColumnName="_mgitype_key")
	private MGIType mgiType;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	private Vocabulary vocab;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_evidencevocab_key", referencedColumnName="_vocab_key")
	private Vocabulary evidenceVocab;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_qualifiervocab_key", referencedColumnName="_vocab_key")
	private Vocabulary qualifierVocab;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_anottype_key", referencedColumnName="_annottype_key")
	private Set<Annotation> annotations;
}
