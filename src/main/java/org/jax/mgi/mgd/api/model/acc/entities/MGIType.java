package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGIType Model Object")
@Table(name="acc_mgitype")
public class MGIType extends EntityBase {

	@Id
	private Integer _mgitype_key;
	private String name;
	private String tableName;
	private String primaryKeyName;
	private String identifyColumnName;
	private String dbView;
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

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "mgi_organism_mgitype",
		joinColumns = @JoinColumn(name = "_mgitype_key"),
		inverseJoinColumns = @JoinColumn(name = "_organism_key")
	)
	@OrderColumn(name="sequencenum")
	private Set<Organism> organisms;

}
