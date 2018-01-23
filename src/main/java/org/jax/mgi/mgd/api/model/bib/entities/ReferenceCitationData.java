package org.jax.mgi.mgd.api.model.bib.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Citation Model Object")
@Table(name="bib_citation_cache")
public class ReferenceCitationData extends EntityBase {
	@Id
	private int _refs_key;
	private String short_citation;
	private String jnumid;
	private Integer numericPart;
	private String mgiid;
	private String doiid;
	private String pubmedid;
}
