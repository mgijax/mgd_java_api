package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.dag.entities.DagEdge;
import org.jax.mgi.mgd.api.model.dag.entities.DagNode;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="voc_term")
public class Term extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voc_term_generator")
    @SequenceGenerator(name="voc_term_generator", sequenceName = "voc_term_seq", allocationSize=1)
    @ApiModelProperty(value="primary key")
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
	@Where(clause="`_dag_key` in (1,2,3)")
	private List<DagNode> goDagNodes;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause ="`_mgitype_key` = 13 and `_synonymtype_key` = 1034")
	@OrderBy(clause ="_synonymtype_key, synonym")
	private List<MGISynonym> goRelSynonyms;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause ="`_mgitype_key` = 13 and `_synonymtype_key` = 1017")
	@OrderBy(clause ="_synonymtype_key, synonym")
	private List<MGISynonym> celltypeSynonyms;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_dag_key` = 52")
	private List<DagNode> celltypeNodes;	
}
