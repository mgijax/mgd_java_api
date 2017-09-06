package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference Associated Data Model Object")
@Table(name="bib_associateddata_view")
public class ReferenceAssociatedData extends EntityBase {
	@Id
	@Column(name="_refs_key")
	public long _refs_key;

	@Column(name="has_gxdindex")
	public int has_gxdindex;

	@Column(name="has_gxdimages")
	public int has_gxdimages;

	@Column(name="has_gxdspecimens")
	public int has_gxdspecimens;

	@Column(name="has_gxdresults")
	public int has_gxdresults;

	@Column(name="has_antibodies")
	public int has_antibodies;

	@Column(name="has_probes")
	public int has_probes;

	@Column(name="has_markers")
	public int has_markers;

	@Column(name="has_alleles")
	public int has_alleles;
}
