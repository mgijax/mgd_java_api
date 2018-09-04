package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Expression Cache Model Object")
@Table(name="gxd_expression")
public class ExpressionCache extends BaseEntity {

	@Id
	private Integer _expression_key;
	private Integer _marker_key;
	private String expressed;
	//private String age;
	//private Integer agemin;
	//private Integer agemax;
	//private Integer isrecombinase;
	//private Integer isforgxd;
	//private Integer hasimage;
	private Date creation_date;
	private Date modification_date;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_assay_key")
	//private Assay assay;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_refs_key")
	//private Reference reference;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_assaytype_key")
	//private AssayType assayType;

	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_genotype_key")
	//private Genotype genotype;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_marker_key")
	//private Marker marker;

	//need to add:
	//_emaps_term_key
	//_stage_key
	//_specimen_key
	//_genelane_key
	
}
