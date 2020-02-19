package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "InSitu Result Image Model Object")
@Table(name="gxd_insituresultimage")
public class InSituResultImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_insituresultimage_generator")
	@SequenceGenerator(name="gxd_insituresultimage_generator", sequenceName = "gxd_insituresultimage_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _resultimage_key;
	private int _result_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagepane_key")
	private ImagePane imagepane;

}
