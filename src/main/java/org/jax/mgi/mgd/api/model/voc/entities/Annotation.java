package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.prb.entities.Strain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Annotation Model Object")
@Table(name="voc_annot")
public class Annotation extends EntityBase {

	@Id
	private Integer _annot_key;
	private Integer _object_key;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_annottype_key")
	private AnnotType annotType;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_term_key")
	private Term term;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_qualifier_key", referencedColumnName="_term_key")
	private Term qualifier;
	
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
    @Where(clause="annotType._mgitype_key = 11")
    private Allele allele;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
    @Where(clause="annotType._mgitype_key = 12")
    private Genotype genotype;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
    @Where(clause="annotType._mgitype_key = 2")
    private Marker marker;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
    @Where(clause="annotType._mgitype_key = 10")
    private Strain strain;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
    @Where(clause="annotType._mgitype_key = 13")
    private Term annotatedTerm;
}
