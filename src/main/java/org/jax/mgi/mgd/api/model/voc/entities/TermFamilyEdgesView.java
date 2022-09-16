package org.jax.mgi.mgd.api.model.voc.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="voc_termfamilyedges_view")
public class TermFamilyEdgesView extends BaseEntity {

	@Id
    @ApiModelProperty(value="primary key")
	private int _edge_key;
	private int _child_key;
	private int _parent_key;
	private String parentid;
	private String accid;
	private String label;
}
