package org.jax.mgi.mgd.api.model.ri.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class RISummaryExptRefKey implements Serializable {
	private Integer _risummary_key;
	private Integer _expt_key;
}