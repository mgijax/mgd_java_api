package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

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
@Schema(description = "Probe Strain Marker Model Object")
@Table(name="prb_strain_marker")

public class ProbeStrainMarker extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_strain_marker_generator")
	@SequenceGenerator(name="prb_strain_marker_generator", sequenceName = "prb_strain_marker_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _strainmarker_key;
	private int _strain_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key")
	private Allele allele;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_qualifier_key", referencedColumnName="_term_key")
	private Term qualifier;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
