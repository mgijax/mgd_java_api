package org.jax.mgi.mgd.api.model.mld.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable 
public class ExptMarkerKey implements Serializable {
	private Integer _expt_key;
	private Integer sequenceNum;
}
