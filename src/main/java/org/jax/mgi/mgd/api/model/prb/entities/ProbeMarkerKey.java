package org.jax.mgi.mgd.api.model.prb.entities;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class ProbeMarkerKey {

	private Integer _probe_key;
	private Integer _marker_key;
}
