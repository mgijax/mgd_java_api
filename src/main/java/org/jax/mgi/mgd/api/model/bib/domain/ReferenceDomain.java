package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceDomain extends BaseDomain {
	private Integer _refs_key;
	private String authors;
	private String title;
	private String journal;
	private String volume;
	private String issue;
	private String date;
	private Integer year;
	private String pages;
	private Integer isReviewArticle;
	private Integer isDiscard;
	private String primaryAuthor;
	private String referenceType;
	private String jnumID;
	private Date creation_date;
	private Date modification_date;
}
