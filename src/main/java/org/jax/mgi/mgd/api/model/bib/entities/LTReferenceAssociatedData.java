package org.jax.mgi.mgd.api.model.bib.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Associated Data Model Object")
@Table(name="bib_associateddata_view")
public class LTReferenceAssociatedData extends BaseEntity {
	@Id
	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="has_alleles")
	private int has_alleles;
	
	@Column(name="has_antibodies")
	private int has_antibodies;

	@Column(name="has_go")
	private int has_go;
	
	@Column(name="has_gxdindex")
	private int has_gxdindex;

	@Column(name="has_gxdimages")
	private int has_gxdimages;

	@Column(name="has_gxdspecimens")
	private int has_gxdspecimens;

	@Column(name="has_gxdresults")
	private int has_gxdresults;
	
	@Column(name="has_mapping")
	private int has_mapping;
		
	@Column(name="has_markers")
	private int has_markers;
	
	@Column(name="has_probes")
	private int has_probes;

	@Column(name="has_strain")
	private int has_strain;
	
}
