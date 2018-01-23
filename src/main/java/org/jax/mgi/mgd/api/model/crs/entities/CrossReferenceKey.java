package org.jax.mgi.mgd.api.model.crs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CrossReferenceKey implements Serializable {
	private Integer _cross_key;
	private Integer _marker_key;
	private Integer _refs_key;
}
