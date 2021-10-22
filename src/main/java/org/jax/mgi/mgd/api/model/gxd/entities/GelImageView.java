package org.jax.mgi.mgd.api.model.gxd.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Gel Image View Entity Object")
@Table(name="gxd_gelimage_view")
public class GelImageView extends BaseEntity {
	
	// this view contain a concatenated "figureLabel" + "paneLabel"
	
	@Id
	private int _imagepane_key;
	private int _image_key;
	private String figurepaneLabel;
	private String accID;
	private String pixID;
	private String xDim;
	private String yDim;
	private String x;
	private String y;
	private String width;
	private String height;

}
