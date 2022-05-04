package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Workflow Relevance Model Object")
@Table(name="bib_workflow_relevance")
public class ReferenceWorkflowRelevance extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bib_workflow_relevance_generator")
	@SequenceGenerator(name="bib_workflow_relevance_generator", sequenceName = "bib_workflow_relevance_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _assoc_key;

	private int _refs_key;
	private int isCurrent;
	private Double confidence;
	private String version;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_group_key", referencedColumnName="_term_key")
	private Term groupTerm;
	
	@OneToOne
	@JoinColumn(name="_relevance_key", referencedColumnName="_term_key")
	private Term relevanceTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
