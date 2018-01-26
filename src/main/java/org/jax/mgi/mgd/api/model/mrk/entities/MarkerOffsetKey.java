package org.jax.mgi.mgd.api.model.mrk.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable 
public class MarkerOffsetKey implements Serializable {
	private Integer _marker_key;
	private Integer source;
}
