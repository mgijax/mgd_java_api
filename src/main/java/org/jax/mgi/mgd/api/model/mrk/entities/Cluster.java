package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cluster Model Object")
@Table(name="mrk_cluster")
public class Cluster extends EntityBase {

	@Id
	private Integer _cluster_key;
	private String clusterID;
	private String version;
	private Date cluster_date;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_clustertype_key", referencedColumnName="_term_key")
	private Term clusterType;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_clustersource_key", referencedColumnName="_term_key")
	private Term clusterSource;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	//@OneToMany(fetch=FetchType.EAGER)
	//@JoinColumn(name="_cluster_key", referencedColumnName="_object_key")
	//@Where(clause="_mgitype_key = 39")
	//private Set<MGIProperty> properties;
	
}
