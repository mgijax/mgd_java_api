package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Citation Model Object")
@Table(name="bib_citation_cache")
public class ReferenceCitationData extends EntityBase {
	@Id
	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="short_citation")
	private String short_citation;
}
