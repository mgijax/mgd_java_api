package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Cache Model Object")
@Table(name="mrk_reference")
public class MarkerReferenceCache extends BaseEntity {

	// This only needs to be done if there is no other primary key for the table
	@EmbeddedId
	private MarkerReferenceCacheKey key;
	private Date creation_date;
	private Date modification_date;
	
    @ManyToOne
    @JoinColumn(name = "_marker_key", insertable = false, updatable = false)
    private Marker marker;
    
    @ManyToOne
    @JoinColumn(name = "_refs_key", insertable = false, updatable = false)
    private Reference reference;

}
