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

@Entity
@ApiModel(value = "Reference Workflow Status Model Object")
@Table(name="bib_workflow_status")
public class ReferenceWorkflowStatus extends Base {
	@Id
	@Column(name="_assoc_key")
	public long _assoc_key;

	@Column(name="_refs_key")
	public long _refs_key;

	@Column(name="isCurrent")
	public int isCurrent;

	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;
	
	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_group_key", referencedColumnName="_term_key")
	public Term groupTerm;
	
	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_status_key", referencedColumnName="_term_key")
	public Term statusTerm;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	public User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	public User modifiedByUser;
	
	/***--- transient methods ---***/
	
	@Transient
	public String getGroup() {
		if (this.groupTerm == null) { return null; }
		return this.groupTerm.term;
	}
	
	@Transient
	public String getGroupAbbreviation() {
		if (this.groupTerm == null) { return null; }
		return this.groupTerm.abbreviation;
	}
	
	@Transient
	public String getStatus() {
		if (this.statusTerm == null) { return null; }
		return this.statusTerm.term;
	}
	
	@Transient
	public boolean isForGroup(String groupAbbrev) {
		if ((groupAbbrev == null) || (this.groupTerm == null)) { return false; }
		return groupAbbrev.equals(this.groupTerm.abbreviation);
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
