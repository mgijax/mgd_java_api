package org.jax.mgi.mgd.api.model.crs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CrossProgenyKey implements Serializable {
	private Integer _cross_key;
	private Integer sequenceNum;
}