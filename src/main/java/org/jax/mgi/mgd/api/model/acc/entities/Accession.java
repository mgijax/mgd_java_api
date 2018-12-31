package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@ApiModel(value = "Accession Model Object")
@Table(name="acc_accession")
public class Accession extends BaseEntity {
	
	@Id
	private int _accession_key;
	private String accID;
	private String prefixPart;
	private Integer numericPart;
	private Integer _object_key;
	@Column(name="private")		// just "private" is a Java reserved word
	private Integer isPrivate;
	private Integer preferred;
	private Date creation_date;
	private Date modification_date;

	// due to LTReferenceDomain, this must not be lazy
	@OneToOne
	@JoinColumn(name="_logicaldb_key")
	private LogicalDB logicaldb;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany
	@JoinColumn(name="_accession_key", insertable=false, updatable=false)
	List<AccessionReference> references;
	
}
