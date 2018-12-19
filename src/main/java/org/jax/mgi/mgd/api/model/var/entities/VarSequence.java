package org.jax.mgi.mgd.api.model.var.entities;

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
@Table(name="var_sequence")
public class VarSequence extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="var_sequence_generator")
	@SequenceGenerator(name="var_sequence_generator", sequenceName = "var_sequence_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _variantsequence_key;
	private String referenceSequence;
	private String variantSequence;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_variant_key", insertable=false, updatable=false)
	private VarVariant variant;
	
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
