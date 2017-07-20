package org.jax.mgi.mgd.api.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	public int _assoc_key;

	@Column(name="_refs_key")
	public int _refs_key;

	@Column(name="isCurrent")
	public int isCurrent;

	@Column(name="creation_date")
	private Date creation_date;
	
	@Column(name="modification_date")
	private Date modification_date;
	
	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_group_key", referencedColumnName="_term_key")
	private Term groupTerm;
	
	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_status_key", referencedColumnName="_term_key")
	private Term statusTerm;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByTerm;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByTerm;
	
	/***--- transient methods ---***/
	
	@Transient
	public String getGroup() {
		return this.groupTerm.term;
	}
	
	@Transient
	public String getGroupAbbreviation() {
		return this.groupTerm.abbreviation;
	}
	
	@Transient
	public String getStatus() {
		return this.statusTerm.term;
	}
	
	@Transient
	public boolean isForGroup(String groupAbbrev) {
		if (groupAbbrev == null) { return false; }
		
		return groupAbbrev.equals(this.groupTerm.abbreviation);
	}
	
	@Transient
	public String getCreatedBy() {
		return this.createdByTerm.login;
	}
	
	@Transient
	public String getModifidBy() {
		return this.modifiedByTerm.login;
	}
	
	@Transient
	private String formatDate(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		return formatter.format(d); 
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
