package org.jax.mgi.mgd.api.model.gxd.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class GelLaneStructureKey implements Serializable {
	private Integer _gellane_key;
	private Integer _emapa_term_key;
	private Integer _stage_key;
}
