package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Summary Entity Object")
@Table(name="img_image_summary_view")
public class ImageSummary extends BaseEntity {

	@Id
	private int _object_key;
	private Integer _accession_key;
	private String accID;
	private String prefixPart;
	private String numericPart;
	private Integer _logicaldb_key;
	private Integer _mgitype_key;
	@Column(name="private")		// just "private" is a Java reserved word
	private Integer isPrivate;	
	private Integer preferred;
	private String mgiID;
	private String subtype;
	private String description;
	private String short_description;
	private Date creation_date;
	private Date modification_date;

}
