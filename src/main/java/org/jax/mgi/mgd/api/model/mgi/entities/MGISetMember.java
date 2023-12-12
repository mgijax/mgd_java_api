package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "MGISet Member")
@Table(name="mgi_setmember")
public class MGISetMember extends BaseEntity {
	
	@Id
	private int _setmember_key;
	private int _set_key;
	private int _object_key;
	private String label;
	private int sequenceNum;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// set member/emapa
	@OneToMany()
	@JoinColumn(name="_setmember_key", referencedColumnName="_setmember_key", insertable=false, updatable=false)
	private List<MGISetMemberEmapa> emapaStages;
	
	// genotype accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> genotypeAccessionIds;

}
