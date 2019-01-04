package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Variant Sequence Entity Object")
@Table(name="all_variant_sequence")
public class VariantSequence extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_variantsequence_seq_generator")
	@SequenceGenerator(name="all_variantsequence_seq_generator", sequenceName = "all_variantsequence_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _variantsequence_key;
    private String startCoordinate;
    private String endCoordinate;
	private String referenceSequence;
	private String variantSequence;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_variant_key", insertable=false, updatable=false)
	private AlleleVariant variant;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sequence_type_key", referencedColumnName="_term_key")
	private Term sequenceType;
		
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
}
