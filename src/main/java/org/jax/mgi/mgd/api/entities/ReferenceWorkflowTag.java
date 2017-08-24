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
@ApiModel(value = "Reference Workflow Tag Model Object")
@Table(name="bib_workflow_tag")
public class ReferenceWorkflowTag extends Base {
	@Id
	@Column(name="_assoc_key")
	public long _assoc_key;

	@Column(name="_refs_key")
	public long _refs_key;

	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;
	
	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_tag_key", referencedColumnName="_term_key")
	public Term tag;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	public User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	public User modifiedByUser;
	
	/***--- transient methods ---***/
	
	@Transient
	public String getTag() {
		if (this.tag == null) { return null; }
		return this.tag.getTerm();
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
