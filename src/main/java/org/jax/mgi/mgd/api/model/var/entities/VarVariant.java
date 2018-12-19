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
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Variant  Entity Object")
@Table(name="var_variant")
public class VarVariant extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="var_variant_generator")
	@SequenceGenerator(name="var_variant_generator", sequenceName = "var_variant_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _variant_key;
	private int isReviewed;
	private String description;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key")
	private Allele allele;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sourcevariant_key")
	private VarVariant sourceVariant;
	
	@OneToOne
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
}
