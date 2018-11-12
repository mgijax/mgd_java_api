package org.jax.mgi.mgd.api.model.mrk.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable 
public class MarkerHistoryKey implements Serializable {
	private int _marker_key;
	private int sequenceNum;
}
