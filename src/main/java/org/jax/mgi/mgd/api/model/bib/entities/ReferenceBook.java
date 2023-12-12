package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Reference Book Model Object")
@Table(name="bib_books")
public class ReferenceBook extends BaseEntity {
	
	@Id
	private int _refs_key;
	private String book_au;
	private String book_title;
	private String place;
	private String publisher;
	private String series_ed;	
	private Date creation_date;
	private Date modification_date;

}
