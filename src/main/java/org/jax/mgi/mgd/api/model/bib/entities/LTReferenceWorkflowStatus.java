package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
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
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bib_workflow_status_generator")
	@SequenceGenerator(name="bib_workflow_status_generator", sequenceName = "bib_workflow_status_seq", allocationSize=1)	
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
	
	@OneToOne
	@JoinColumn(name="_group_key", referencedColumnName="_term_key")
	private Term groupTerm;
	
	@OneToOne
	@JoinColumn(name="_status_key", referencedColumnName="_term_key")
	private Term statusTerm;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByUser;
	
	@OneToOne
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

	public String getRefsKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
