package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Image Pane Entity Object")
@Table(name="img_imagepane")
public class ImagePane extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="img_imagepane_generator")
	@SequenceGenerator(name="img_imagepane_generator", sequenceName = "img_imagepane_seq", allocationSize=1)
	@Schema(name="primary key")
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
	@OneToMany()
	@JoinColumn(name="_imagepane_key", insertable=false, updatable=false)	
	private List<ImageSummaryView> imageSummary;
	
	// image pane associations
	@OneToMany()
	@JoinColumn(name="_imagepane_key", insertable=false, updatable=false)
	//@Where(clause="`_mgitype_key` in (11, 12)")
	@OrderBy(clause="isPrimary desc")
	private List<ImagePaneAssoc> paneAssocs;

}
