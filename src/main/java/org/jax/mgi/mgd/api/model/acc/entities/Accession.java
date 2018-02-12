package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@ApiModel(value = "Accession Model Object")
@Table(name="acc_accession")
public class Accession extends EntityBase {
	@Id
	private int _accession_key;
	private String accID;
	private Integer preferred;

	@Column(name="private")		// just "private" is a Java reserved word
	private Integer is_private;

	private Integer _logicaldb_key;
	private Integer _object_key;
	private Integer _mgitype_key;
	private String prefixPart;
	private Integer numericPart;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByUser;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByUser;
	
	@ManyToMany
	@JoinTable(name = "acc_accessionreference",
		joinColumns = @JoinColumn(name = "_accession_key"),
		inverseJoinColumns = @JoinColumn(name = "_refs_key")
	)
	private List<Reference> references;

}
