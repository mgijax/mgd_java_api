package org.jax.mgi.mgd.api.model.crs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CrossReferenceKey implements Serializable {
	private int _cross_key;
	private int _marker_key;
	private int _refs_key;
}
