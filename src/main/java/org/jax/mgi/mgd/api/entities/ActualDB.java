package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "ActualDB Model Object")
@Table(name="acc_actualdb")
public class ActualDB extends EntityBase {

	@Id
	@Column(name="_actualdb_key")
	private Integer _actualdb_key;
	
	@Column(name="name")
	private String name;

	@Column(name="active")
	private Integer active;

	@Column(name="url")
	private String url;

	@Column(name="allowsmultiple")
	private Integer allowsmultiple;
	
	@Column(name="delimiter")
	private String delimiter;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_logicaldb_key", referencedColumnName="_logicaldb_key")
	private LogicalDB logicaldb;
	
	@Column(name="creation_date")
	private Date creation_date;
	
	@Column(name="modification_date")
	private Date modification_date;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
