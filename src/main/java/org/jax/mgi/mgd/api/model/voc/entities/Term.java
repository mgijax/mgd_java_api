package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.dag.entities.DagNode;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="voc_term")
public class Term extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voc_term_generator")
    @SequenceGenerator(name="voc_term_generator", sequenceName = "voc_term_seq", allocationSize=1)
    @Schema(name="primary key")
	private int _term_key;
	private int _vocab_key;
	private String term;
	private String abbreviation;
	private String note;
	private Integer sequenceNum;
	private Integer isObsolete = 0;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key", insertable=false, updatable=false)
	private Vocabulary vocab;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key", insertable=false, updatable=false)
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key", insertable=false, updatable=false)
	private User modifiedBy;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and preferred = 1")
	private List<Accession> accessionIds;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and preferred = 0")
	private List<Accession> accessionSecondaryIds;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_dag_key` in (1,2,3)")
	private List<DagNode> goDagNodes;

	// all synonyms
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause ="`_mgitype_key` = 13")
	@OrderBy(clause ="_synonymtype_key, synonym")
	private List<MGISynonym> synonyms;
	
	// for 'MGI-GORel'
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause ="`_mgitype_key` = 13 and `_synonymtype_key` = 1034")
	@OrderBy(clause ="_synonymtype_key, synonym")
	private List<MGISynonym> goRelSynonyms;

	// 'exact' synonyms
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause ="`_mgitype_key` = 13 and `_synonymtype_key` = 1017")
	@OrderBy(clause ="_synonymtype_key, synonym")
	private List<MGISynonym> exactSynonyms;
}
