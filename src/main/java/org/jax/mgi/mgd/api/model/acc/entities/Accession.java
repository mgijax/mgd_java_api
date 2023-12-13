package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Schema(description = "Accession Model Object")
@Table(name="acc_accession")
public class Accession extends BaseEntity {
	
	@Id
	private int _accession_key;
	private String accID;
	private String prefixPart;
	// int that allows null must be Integer
	private Integer numericPart;
	private Integer _object_key;
	@Column(name="private", columnDefinition="int2")		// just "private" is a Java reserved word
	private Integer isPrivate;
	@Column(columnDefinition="int2")
	private Integer preferred;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
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
	
	@OneToMany()
	@JoinColumn(name="_accession_key", insertable=false, updatable=false)
	List<AccessionReference> references;
	
}
