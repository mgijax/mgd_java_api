package org.jax.mgi.mgd.api.model.bib.entities;

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
public class ReferenceAssociatedData extends BaseEntity {

	@Id
	private int _refs_key;
	private int has_alleles;
	private int has_antibodies;
	private int has_go;
	private int has_gxdindex;
	private int has_gxdimages;
	private int has_gxdspecimens;
	private int has_gxdresults;
	private int has_mapping;	
	private int has_markers;
	private int has_probes;
	private int has_strain;
	
}
