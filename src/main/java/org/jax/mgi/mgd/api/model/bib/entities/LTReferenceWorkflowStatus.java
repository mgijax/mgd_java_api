package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Workflow Status Model Object")
@Table(name="bib_workflow_status")
public class LTReferenceWorkflowStatus extends BaseEntity {
	
	@Id
	private int _assoc_key;
	private int _refs_key;
	private int isCurrent;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_group_key", referencedColumnName="_term_key")
	private Term groupTerm;
	
	@OneToOne
	@JoinColumn(name="_status_key", referencedColumnName="_term_key")
	private Term statusTerm;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;
	
	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
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
		if (this.createdBy == null) { return null; }
		return this.createdBy.getLogin();
	}
	
	@Transient
	public String getModifiedBy() {
		if (this.modifiedBy == null) { return null; }
		return this.modifiedBy.getLogin();
	}
	
	@Transient
	public String getCreationDate() {
		return formatter.format(this.creation_date);
	}
	
	@Transient
	public String getModificationDate() {
		return formatter.format(this.modification_date);
	}

	public String getRefsKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
