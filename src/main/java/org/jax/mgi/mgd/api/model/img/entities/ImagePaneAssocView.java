package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Image Pane Assoc View Entity Object")
@Table(name="img_imagepane_assoc_view")
public class ImagePaneAssocView extends BaseEntity {

	// 06/14/2010
	// used by alleles & genotypes to display image pane associations
	// if the allele/UI, genotype/UI and image/UI want to edit image pane associations
	// then this may need to change
	
	@Id
	private int _assoc_key;

	private Integer _object_key;
	private Integer isPrimary;
	private String figureLabel;
	private String term;
	private String mgiID;
	private String pixID;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagepane_key")
	private ImagePane imagePane;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imageclass_key", referencedColumnName="_term_key")
	private Term imageClass;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
}
