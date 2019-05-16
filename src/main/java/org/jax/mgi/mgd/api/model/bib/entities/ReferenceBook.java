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
public class ReferenceBook extends BaseEntity {

	// due to LTReference, use same String names
	
	@Id
	private int _refs_key;
	@Column(name="book_au")
	private String book_author;
	private String book_title;
	private String place;
	private String publisher;
	@Column(name="series_ed")
	private String series_edition;	private Date creation_date;
	private Date modification_date;

}
