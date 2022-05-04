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
@ApiModel(value = "Reference Workflow Data Model Object")
@Table(name="bib_workflow_data")
public class ReferenceWorkflowData extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bib_workflow_data_generator")
	@SequenceGenerator(name="bib_workflow_data_generator", sequenceName = "bib_workflow_data_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _assoc_key;

	private int _refs_key;
	private int haspdf;
	private String linksupplemental; // never used in schema/obsolete/should be removed from schema 05/2022
	private String extractedtext;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_supplemental_key", referencedColumnName="_term_key")
	private Term supplementalTerm;	

	@OneToOne
	@JoinColumn(name="_extractedtext_key", referencedColumnName="_term_key")
	private Term extractedTextTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
