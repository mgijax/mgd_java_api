package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBMarker Model Object")
@Table(name="prb_marker")
public class ProbeMarker extends BaseEntity {

	// This only needs to be done if there is no other primary key for the table
	@EmbeddedId
	private ProbeMarkerKey key;

	private String relationship;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
    @ManyToOne
    @JoinColumn(name = "_marker_key", insertable = false, updatable = false)
    private Marker marker;
    
    @ManyToOne
    @JoinColumn(name = "_probe_key", insertable = false, updatable = false)
    private Probe probe;

}
