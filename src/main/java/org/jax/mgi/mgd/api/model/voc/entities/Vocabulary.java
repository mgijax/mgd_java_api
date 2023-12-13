package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Vocabulary Model Object")
@Table(name="voc_vocab")
public class Vocabulary extends BaseEntity {

	@Id
	private int _vocab_key;
	@Column(columnDefinition = "int2")
	private Integer isSimple;
	@Column(columnDefinition = "int2")
	private Integer isPrivate;
	private String name;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_logicaldb_key")
	private LogicalDB logicalDB;

	@OneToMany()
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	@OrderBy(clause ="sequenceNum")
	private List<Term> terms;
	
	@OneToMany()
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	private List<VocabularyDAG> vocabDAG;	
	
}