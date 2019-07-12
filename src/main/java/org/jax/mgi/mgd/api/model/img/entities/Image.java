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

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Entity Object")
@Table(name="img_image")
public class Image extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="img_image_generator")
	@SequenceGenerator(name="img_image_generator", sequenceName = "img_image_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _image_key;

	private Integer xDim;
	private Integer yDim;
	private String figureLabel;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imageclass_key", referencedColumnName="_term_key")
	private Term imageClass;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagetype_key", referencedColumnName="_term_key")
	private Term imageType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_thumbnailimage_key", referencedColumnName="_image_key")
	private Image thumbnailImage;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// Caption
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 9 and `_notetype_key` = 1024")
	private List<Note> captionNote;
	
	// Copyright
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 9 and `_notetype_key` = 1023")
	private List<Note> copyrightNote;
	
	// Private Curatorial
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 9 and `_notetype_key` = 1025")
	private List<Note> privateCuratorialNote;
	
	// External Link
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 9 and `_notetype_key` = 1039")
	private List<Note> externalLinkNote;
		
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 9 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;

	// editable only accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 9 and `_logicaldb_key` in (19)")
	@OrderBy(clause ="accid")
	private List<Accession> editAccessionIds;
	
	// non-editable accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_image_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` in (9) and `_logicaldb_key` not in (1,19)")
	@OrderBy(clause ="accid")
	private List<Accession> nonEditAccessionIds;
	
	// image panes
	// sort descending to push any null pane labels to the top if imagePane list
	// translator checks the first row of imagePane list for null before calling sort
	@OneToMany()
	@JoinColumn(name="_image_key", insertable=false, updatable=false)
	@OrderBy(clause="paneLabel desc")
	private List<ImagePane> imagePanes;	
}
