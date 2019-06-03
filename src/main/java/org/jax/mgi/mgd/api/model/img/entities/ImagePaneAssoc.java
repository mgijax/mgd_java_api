package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Pane Assoc Entity Object")
@Table(name="img_imagepane_assoc")
public class ImagePaneAssoc extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="img_imagepane_assoc_generator")
	@SequenceGenerator(name="img_imagepane_assoc_generator", sequenceName = "img_imagepane_assoc_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _assoc_key;

	private Integer _object_key;
	private Integer isPrimary;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagepane_key")
	private ImagePane imagePane;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// does this pane association contain an allele or a genotype?
	// see ImagePaneAssocTranslator; translator will decide
	
	@OneToMany()
	@JoinColumn(name="_allele_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	private List<Allele> alleles;	

	@OneToMany()
	@JoinColumn(name="_genotype_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	private List<Genotype> genotypes;	
	
}
