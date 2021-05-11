package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Visualization Method Model Object")
@Table(name="gxd_visualizationmethod")
public class VisualizationMethod extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_visualization_generator")
	@SequenceGenerator(name="gxd_visualization_generator", sequenceName = "gxd_visualization_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _visualization_key;
	private String visualization;
	private Date creation_date;
	private Date modification_date;
}
