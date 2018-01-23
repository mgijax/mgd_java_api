package org.jax.mgi.mgd.api.model.gxd.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class IndexStageKey implements Serializable {
	private Integer _index_key;
	private Integer _indexassay_key;
	private Integer _stageid_key;
}
