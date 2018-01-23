package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Organism Model Object")
@Table(name="mgi_organism")
public class Organism extends EntityBase {

	@Id
	private Integer _organism_key;
	private String commonname;
	private String latinname;
	private Date creation_date;
	private Date modification_date;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "mgi_organism_mgitype",
		joinColumns = @JoinColumn(name = "_organism_key"),
		inverseJoinColumns = @JoinColumn(name = "_mgitype_key")
	)
	private Set<MGIType> mgiTypes;

}
