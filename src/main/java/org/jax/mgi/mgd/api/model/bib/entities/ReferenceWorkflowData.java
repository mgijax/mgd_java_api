package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Reference Workflow Data Model Object")
@Table(name="bib_workflow_data")
public class ReferenceWorkflowData extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bib_workflow_data_generator")
	@SequenceGenerator(name="bib_workflow_data_generator", sequenceName = "bib_workflow_data_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _assoc_key;

	private int _refs_key;
	private int haspdf;
	private String linksupplemental; // never used in schema/obsolete/should be removed from schema 05/2022
	private String extractedtext;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_supplemental_key", referencedColumnName="_term_key")
	private Term supplementalTerm;	

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_extractedtext_key", referencedColumnName="_term_key")
	private Term extractedTextTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
