package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Vocabulary DAG Model Object")
@Table(name="voc_vocabdag")
public class VocabularyDAG extends BaseEntity {

	@Id
	private int _dag_key;	
	private int _vocab_key;
	private Date creation_date;
	private Date modification_date;
}