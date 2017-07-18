package org.jax.mgi.mgd.api.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference/Allele Association Model Object")
@Table(name="mgi_reference_assoc")
public class ReferenceAlleleAssociation extends Base {
	@Id
	@Column(name="_assoc_key")
	public int _assoc_key;

	@Column(name="_refs_key")
	public int _refs_key;

	@Column(name="_mgitype_key")
	public int _mgitype_key;

	@Column(name="_object_key")
	public int _allele_key;

	@OneToOne (targetEntity=AccessionID.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	@Where(clause="_mgitype_key = 11 and _logicaldb_key = 1 and preferred = 1")
	private AccessionID alleleID;
}
