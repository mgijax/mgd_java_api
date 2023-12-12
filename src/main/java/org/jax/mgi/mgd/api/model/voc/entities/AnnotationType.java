package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Annotation Type Model Object")
@Table(name="voc_annottype")
public class AnnotationType extends BaseEntity {

	@Id
	private int _annotType_key;
	private String name;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	//turn this off 
	// because it is causing the entity to return the same annotation more than once
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_vocab_key")
	//private Vocabulary vocab;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_evidencevocab_key", referencedColumnName="_vocab_key")
	private Vocabulary evidenceVocab;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_qualifiervocab_key", referencedColumnName="_vocab_key")
	private Vocabulary qualifierVocab;
	
}
