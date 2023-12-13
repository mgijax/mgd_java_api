package org.jax.mgi.mgd.api.model.img.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Image Summary View Entity Object")
@Table(name="img_image_summary2_view")
public class ImageSummaryView extends BaseEntity {
	
	// this view contain a concatenated "figureLabel" + "paneLabel"
	
	@Id
	private int _imagepane_key;
	private int _image_key;
	private String figurepaneLabel;
	private String accID;
	private Integer pixID;
	private Integer xDim;
	private Integer yDim;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;

}
