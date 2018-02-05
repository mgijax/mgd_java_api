package org.jax.mgi.mgd.api.model.bib.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Allele Association Model Object")
@Table(name="mgi_reference_assoc")
public class ReferenceAlleleAssociation extends EntityBase {
	@Id
	@Column(name="_assoc_key")
	private int _assoc_key;

	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="_mgitype_key")
	private int _mgitype_key;

	@Column(name="_object_key")
	private int _allele_key;

	@OneToOne (targetEntity=Accession.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key = 11` and _logicaldb_key = 1 and preferred = 1")
	private Accession alleleID;
}
