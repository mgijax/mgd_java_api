package org.jax.mgi.mgd.api.model.bib.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Citation Cache Model Object")
@Table(name="bib_citation_cache")
public class ReferenceCitationCache extends BaseEntity {
	@Id
	private int _refs_key;
	private Integer numericPart; // must be Integer or LT will fail
	private String jnumid;
	private String mgiid;
	private String pubmedid;
	private String doiid;
	private String journal;
	private String citation;
	private String short_citation;
	private String referenceType;
	private int isReviewArticle;
	private String isReviewArticleString;
	private int isDiscard;
	
}
