package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResult;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Pane Model Object")
@Table(name="img_imagepane")
public class ImagePane extends BaseEntity {

	@Id
	private Integer _imagePane_key;
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

	@ManyToMany
	@JoinTable(name = "gxd_insituresultimage",
		joinColumns = @JoinColumn(name = "_imagepane_key"),
		inverseJoinColumns = @JoinColumn(name = "_result_key")
	)
	private Set<InSituResult> inSituResults;
}
