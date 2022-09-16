package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Vocabulary DAG Model Object")
@Table(name="voc_vocabdag")
public class VocabularyDAG extends BaseEntity {

	@Id
	private int _dag_key;	
	private int _vocab_key;
	private Date creation_date;
	private Date modification_date;
}