package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Book Model Object")
@Table(name="bib_books")
public class LTReferenceBook extends BaseEntity {
	@Id
	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="book_au")
	private String book_author;

	@Column(name="book_title")
	private String book_title;

	@Column(name="place")
	private String place;

	@Column(name="publisher")
	private String publisher;

	@Column(name="series_ed")
	private String series_edition;

	@Column(name="creation_date")
	private Date creation_date;
	
	@Column(name="modification_date")
	private Date modification_date;
}
