package org.jax.mgi.mgd.api.model.mrk.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class MarkerMCVCacheKey implements Serializable {
	private Integer _marker_key;
	private Integer _mcvTerm_key;
}
