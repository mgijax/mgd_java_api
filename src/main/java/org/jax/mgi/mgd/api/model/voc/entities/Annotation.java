package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;

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

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and `_logicaldb_key` = 146 and preferred = 1")
	private List<Accession> markerFeatureTypeIds;
	
}
