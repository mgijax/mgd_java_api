package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.dag.entities.Dag;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

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
@Schema(description = "Relationship Category Object")
@Table(name="mgi_relationship_category")
public class RelationshipCategory extends BaseEntity {
	@Id
	private Integer _category_key;
	private String name;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_relationshipvocab_key", referencedColumnName="_vocab_key")
	private Vocabulary relationshipVocab;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_relationshipdag_key", referencedColumnName="_dag_key")
	private Dag relationshipDag ;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key_1", referencedColumnName="_mgitype_key")
	private MGIType mgitype1;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key_2", referencedColumnName="_mgitype_key")
	private MGIType mgitype2;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_qualifiervocab_key", referencedColumnName="_vocab_key")
	private Vocabulary qualifierVocab;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_evidencevocab_key", referencedColumnName="_vocab_key")
	private Vocabulary evidenceVocab;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
