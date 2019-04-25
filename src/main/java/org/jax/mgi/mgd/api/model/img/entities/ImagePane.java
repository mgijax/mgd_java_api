package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

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
	
	private String paneLabel;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_image_key")
	private Image image;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;	
}
