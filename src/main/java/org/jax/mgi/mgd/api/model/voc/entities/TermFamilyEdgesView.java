package org.jax.mgi.mgd.api.model.voc.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="voc_termfamilyedges_view")
public class TermFamilyEdgesView extends BaseEntity {

	@Id
    @Schema(name="primary key")
	private int _edge_key;
	private int _child_key;
	private int _parent_key;
	private String searchid;
	private String label;
}
