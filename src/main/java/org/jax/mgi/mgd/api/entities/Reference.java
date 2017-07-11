package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends Base {

	@Id
	@Column(name="_Refs_key")
	public Long _refs_key;
	
	@Column(name="authors")
	public String authors;

	@Column(name="_primary")
	public String primaryAuthor;

	@Column(name="title")
	public String title;

	@Column(name="journal")
	public String journal;
	
	@Column(name="vol")
	public String volume;

	@Column(name="issue")
	public String issue;
	
	@Column(name="date")
	public String date;
	
	@Column(name="year")
	public String year;

	@Column(name="pgs")
	public String pages;
	
	@Column(name="abstract")
	public String refAbstract;		// just "abstract" is a Java reserved word
	
	@Column(name="isReviewArticle")
	public int isReviewArticle;
}
