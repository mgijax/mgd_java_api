package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference Book Model Object")
@Table(name="bib_books")
public class ReferenceBook extends Base {
	@Id
	@Column(name="_refs_key")
	public int _refs_key;

	@Column(name="book_au")
	public String book_author;

	@Column(name="book_title")
	public String book_title;

	@Column(name="place")
	public String place;

	@Column(name="publisher")
	public String publisher;

	@Column(name="series_ed")
	public String series_edition;

	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;
}
