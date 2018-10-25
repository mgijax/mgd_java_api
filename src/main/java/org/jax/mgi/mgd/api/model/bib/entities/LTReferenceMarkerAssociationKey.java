package org.jax.mgi.mgd.api.model.bib.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class LTReferenceMarkerAssociationKey implements Serializable {
	protected int _refs_key;
	protected int _marker_key;
}
