package org.jax.mgi.mgd.api.model.gxd.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class InSituResultStructureKey implements Serializable {
	private Integer _result_key;
	private Integer _emapa_term_key;
	private Integer _stage_key;
}
