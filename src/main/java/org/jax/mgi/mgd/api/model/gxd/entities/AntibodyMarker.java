package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Antibody Marker Model Object")
@Table(name="gxd_antibodymarker")
public class AntibodyMarker extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_antibodymarker_generator")
	@SequenceGenerator(name="gxd_antibodymarker_generator", sequenceName = "gxd_antibodymarker_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _antibodymarker_key;
	private int _antibody_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Marker marker;	
}
