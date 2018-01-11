package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Model Object")
@Table(name="img_image")
public class Image extends EntityBase {

	@Id
	private Integer _image_key;
	private Integer xDim;
	private Integer yDim;
	private String figureLabel;
	private Date creation_date;
	private Date modification_date;
	
}
