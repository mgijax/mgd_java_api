package org.jax.mgi.mgd.api.model.mld.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class HitKey implements Serializable {
	private Integer _expt_key;
	private Integer _probe_key;
	private Integer _target_key;
}
