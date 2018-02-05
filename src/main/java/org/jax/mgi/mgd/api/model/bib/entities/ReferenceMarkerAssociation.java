package org.jax.mgi.mgd.api.model.bib.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@ApiModel(value = "Marker Association Model Object")
@Table(name="mrk_reference")
public class ReferenceMarkerAssociation extends EntityBase {

	@EmbeddedId
	private ReferenceMarkerAssociationKey keys;
	private String jnumID;

	@OneToOne (targetEntity=Accession.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_logicaldb_key` = 1 and preferred = 1")
	private Accession markerID;
}
