package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Workflow Status Model Object")
//@SequenceGenerator(name = "bib_workflow_status_serial", sequenceName = "bib_workflow_status_serial")
@Table(name="bib_workflow_status")
public class ReferenceWorkflowStatus extends EntityBase {
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bib_workflow_status_serial")
	@Column(name="_assoc_key")
	private int _assoc_key;

	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="isCurrent")
	private int isCurrent;

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
	private User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByUser;
	
	/***--- transient methods ---***/
	
	@Transient
	public String getGroup() {
		if (this.groupTerm == null) { return null; }
		return this.groupTerm.getTerm();
	}
	
	@Transient
	public String getGroupAbbreviation() {
		if (this.groupTerm == null) { return null; }
		return this.groupTerm.getAbbreviation();
	}
	
	@Transient
	public String getStatus() {
		if (this.statusTerm == null) { return null; }
		return this.statusTerm.getTerm();
	}
	
	@Transient
	public boolean isForGroup(String groupAbbrev) {
		if ((groupAbbrev == null) || (this.groupTerm == null)) { return false; }
		return groupAbbrev.equals(this.groupTerm.getAbbreviation());
	}
	
	@Transient
	public String getCreatedBy() {
		if (this.createdByUser == null) { return null; }
		return this.createdByUser.getLogin();
	}
	
	@Transient
	public String getModifidBy() {
		if (this.modifiedByUser == null) { return null; }
		return this.modifiedByUser.getLogin();
	}
	
	@Transient
	public String getCreationDate() {
		return formatter.format(this.creation_date);
	}
	
	@Transient
	public String getModificationDate() {
		return formatter.format(this.modification_date);
	}
}
