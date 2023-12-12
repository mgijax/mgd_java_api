package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "MGISet")
@Table(name="mgi_set")
public class MGISet extends BaseEntity {
	
	@Id
	private int _set_key;
	private String name;
	private int sequenceNum;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// not used
//	@OneToMany()
//	@JoinColumn(name="_set_key", insertable=false, updatable=false)
//	@Where(clause="`_set_key` = 1046")
//	private List<MGISetMember> emapaStageClipboardMembers;

	// this is loaded via MGISetService.java/getBySetUser 
//	@OneToMany()
//	@JoinColumn(name="_set_key", insertable=false, updatable=false)
//	@Where(clause="`_set_key` = 1055")
//	private List<MGISetMember> genotypeClipboardMembers;	
	
}
