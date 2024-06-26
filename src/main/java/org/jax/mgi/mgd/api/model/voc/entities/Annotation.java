package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.gxd.entities.GenotypeAnnotHeaderView;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Annotation Model Object")
@Table(name="voc_annot")
public class Annotation extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voc_annot_generator")
	@SequenceGenerator(name="voc_annot_generator", sequenceName = "voc_annot_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _annot_key;
	private int _object_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_annottype_key")
	private AnnotationType annotType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_term_key")
	private Term term;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_qualifier_key")
	private Term qualifier;

	@OneToMany()
	@JoinColumn(name="_annot_key", insertable=false, updatable=false)
	private List<Evidence> evidences;
	
	// marker feature type:  from _annottype_key = 1011
	// _term_key is the feature type, _object_key is the marker
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and `_logicaldb_key` = 146 and preferred = 1")
	private List<Accession> markerFeatureTypeIds;
	
	// sequence ontology : from _annottype_key 1026, 1027
	// _term_key is the SO term, _object_key is the allele variant
    @OneToMany()
    @JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
    @Where(clause="`_mgitype_key` = 13 and `_logicaldb_key` = 145 and preferred = 1")
    private List<Accession> alleleVariantSOIds;

	// mammalian phenotype ids:  from _annottype_key = 1002
	// _term_key is the MP term, _object_key is the genotype
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and `_logicaldb_key` = 34 and preferred = 1")
	private List<Accession> mpIds;
	
	// disease ontology ids:  from _annottype_key = 1020
	// _term_key is the DO term, _object_key is the genotype
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and `_logicaldb_key` = 191 and preferred = 1")
	private List<Accession> doIds;

	// go ids:  from _annottype_key = 1000
	// _term_key is the GO term, _object_key is the marker
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 13 and `_logicaldb_key` = 31 and preferred = 1")
	private List<Accession> goIds;
	
	// MP Header by Annotation
	@OneToMany()
    @JoinColumn(name="annotKey", referencedColumnName="_annot_key", insertable=false, updatable=false)
    private List<GenotypeAnnotHeaderView> mpHeadersByAnnot;	
}
