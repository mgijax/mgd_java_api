package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

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
