package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Annotation Model Object")
@Table(name="voc_annot")
public class Annotation extends BaseEntity {

	@Id
	private int _annot_key;
	private int _object_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_annottype_key")
	private AnnotationType annotType;

	@OneToOne
	@JoinColumn(name="_term_key")
	private Term term;

	@OneToOne
	@JoinColumn(name="_qualifier_key", referencedColumnName="_term_key")
	private Term qualifier;
	   
}
