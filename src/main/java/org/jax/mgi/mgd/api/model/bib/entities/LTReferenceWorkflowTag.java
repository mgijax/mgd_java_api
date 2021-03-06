package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Column;
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
@ApiModel(value = "Reference Workflow Tag Model Object")
@Table(name="bib_workflow_tag")
public class LTReferenceWorkflowTag extends BaseEntity {
	@Id
	@Column(name="_assoc_key")
	private int _assoc_key;

	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="creation_date")
	private Date creation_date;
	
	@Column(name="modification_date")
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_tag_key", referencedColumnName="_term_key")
	private Term tag;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByUser;
	
	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByUser;
	
	/***--- transient methods ---***/
	
	@Transient
	public String getTagTerm() {
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
