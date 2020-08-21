package org.jax.mgi.mgd.api.model.gxd.entities;

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
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Antibody Marker Model Object")
@Table(name="gxd_antibodymarker")
public class AntibodyMarker extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_antibodymarker_generator")
	@SequenceGenerator(name="gxd_antibodymarker_generator", sequenceName = "gxd_antibodymarker_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _antibodymarker_key;
	private int _antibody_key;
	private Date creation_date;
	private Date modification_date;
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Marker marker;	
}
