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
import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Pane Entity Object")
@Table(name="img_imagepane")
public class ImagePane extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="img_imagepane_generator")
	@SequenceGenerator(name="img_imagepane_generator", sequenceName = "img_imagepane_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _imagepane_key;	
	private Integer _image_key;
	private String paneLabel;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
	private Date creation_date;
	private Date modification_date;
	
	// image summary
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_image_key", insertable=false, updatable=false)
	private ImageSummary imageSummary;
	
	// image pane associations
	@OneToMany()
	@JoinColumn(name="_imagepane_key", insertable=false, updatable=false)
	//@Where(clause="`_mgitype_key` in (11, 12)")
	@OrderBy(clause="isPrimary desc")
	private List<ImagePaneAssoc> paneAssocs;


}
