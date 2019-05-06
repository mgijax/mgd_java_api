package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends BaseEntity {

	@Id
	private int _refs_key;
	private String authors;
	private String title;
	private String journal;
	private String vol;
	private String issue;
	private String date;
	private int year;
	private String pgs;
	private int isReviewArticle;
	private int isDiscard;
	private Date creation_date;
	private Date modification_date;

	@Column(name="abstract")
	private String referenceAbstract;

	@Column(name="_primary")
	private String primaryAuthor;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache referenceCitationCache;
	
}
