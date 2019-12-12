package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "ActualDB Model Object")
@Table(name="acc_actualdb")
public class ActualDB extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="acc_actualdb_generator")
	@SequenceGenerator(name="acc_actualdb_generator", sequenceName = "acc_actualdb_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _actualdb_key;
	private int _logicaldb_key;
	private String name;
	private int active;
	private String url;
	private int allowsMultiple;
	private String delimiter;
	private Date creation_date;
	private Date modification_date;

	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_logicaldb_key", referencedColumnName="_logicaldb_key")
	//private LogicalDB logicaldb;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
