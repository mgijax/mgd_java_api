package org.jax.mgi.mgd.api.model.prb.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class ProbeMarkerKey implements Serializable {
	private int _probe_key;
	private int _marker_key;
}
