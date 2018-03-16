package org.jax.mgi.mgd.api.model.prb.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class ProbeMarkerKey implements Serializable {
	private Integer _probe_key;
	private Integer _marker_key;
}
