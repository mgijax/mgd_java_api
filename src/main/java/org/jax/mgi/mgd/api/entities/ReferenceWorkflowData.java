package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.ejb.Singleton;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;

@Singleton
@Entity
@ApiModel(value = "Reference Workflow Data Model Object")
@Table(name="bib_workflow_data")
public class ReferenceWorkflowData extends Base {
	@Id
	@Column(name="_refs_key")
	public long _refs_key;

	@Column(name="hasPDF")
	public int has_pdf;

	@Column(name="linkSupplemental")
	public String link_supplemental;

	@Column(name="extractedText")
	public String extracted_text;

	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;
	
	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_supplemental_key", referencedColumnName="_term_key")
	public Term supplementalTerm;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	public User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	public User modifiedByUser;
	
	/***--- transient methods ---***/
	
	@Transient
	public String getSupplemental() {
		if (this.supplementalTerm == null) { return null; }
		return this.supplementalTerm.term;
	}
	
	@Transient
	public String getCreatedBy() {
		if (this.createdByUser == null) { return null; }
		return this.createdByUser.login;
	}
	
	@Transient
	public String getModifidBy() {
		if (this.modifiedByUser == null) { return null; }
		return this.modifiedByUser.login;
	}
	
	@Transient
	public String getCreationDate() {
		return this.formatDate(this.creation_date);
	}
	
	@Transient
	public String getModificationDate() {
		return this.formatDate(this.modification_date);
	}
}
