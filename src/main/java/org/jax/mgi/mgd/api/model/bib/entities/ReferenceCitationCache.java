package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mld.entities.MappingNote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Schema(description = "Reference Citation Cache Model Object")
@Table(name = "bib_citation_cache")
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
	private int _relevance_key;
	private String relevanceTerm;
	@Column(columnDefinition = "int2")
	private int isReviewArticle;
	@Column(columnDefinition = "bpchar")
	private String isReviewArticleString;

	// at most one mapping note
	@OneToMany()
	@JoinColumn(name = "_refs_key", insertable = false, updatable = false)
	private List<MappingNote> mappingNote;
}
