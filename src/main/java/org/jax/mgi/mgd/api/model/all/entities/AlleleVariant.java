package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

import jakarta.persistence.Column;
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
@Schema(description = "Variant  Entity Object")
@Table(name="all_variant")
public class AlleleVariant extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_variant_seq_generator")
	@SequenceGenerator(name="all_variant_seq_generator", sequenceName="all_variant_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _variant_key;
	@Column(columnDefinition="int2")
	private int isReviewed;
	private String description;
	private Date creation_date;
	private Date modification_date;

	// if _sourcevariant_key == null, then curated variant
	// if _sourcevariant_key != null, then source variant
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sourcevariant_key")	
	private AlleleVariant sourceVariant;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key")
	private Allele allele;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
    @OneToMany()
    @JoinColumn(name="_object_key", referencedColumnName="_variant_key", insertable=false, updatable=false)
    @Where(clause="`_annottype_key` = 1026")
    private List<Annotation> variantTypes;

    @OneToMany()
    @JoinColumn(name="_object_key", referencedColumnName="_variant_key", insertable=false, updatable=false)
    @Where(clause="`_annottype_key` = 1027")
    private List<Annotation> variantEffects;

    @OneToMany()
    @JoinColumn(name="_variant_key", insertable=false, updatable=false)
    private List<VariantSequence> variantSequences;

	// Curator
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_variant_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 45 and `_notetype_key` = 1050")
	private List<Note> curatorNote;

	// Public
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_variant_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 45 and `_notetype_key` = 1051")
	private List<Note> publicNote;
	
	// References
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_variant_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 45")
	private List<MGIReferenceAssoc> refAssocs;
	
}
