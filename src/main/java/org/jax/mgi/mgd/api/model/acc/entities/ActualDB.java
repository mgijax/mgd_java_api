package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "ActualDB Model Object")
@Table(name="acc_actualdb")
public class ActualDB extends BaseEntity {

	@Id
	private Integer _actualdb_key;
	private String name;
	private Integer active;
	private String url;
	private Integer allowsMultiple;
	private String delimiter;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_logicaldb_key", referencedColumnName="_logicaldb_key")
	private LogicalDB logicaldb;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
