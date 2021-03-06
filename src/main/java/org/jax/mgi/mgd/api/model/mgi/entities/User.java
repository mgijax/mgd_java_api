package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "User Model Object")
@Table(name="mgi_user")
public class User extends BaseEntity {
	@Id
	private Integer _user_key;
	private String login;
	private String orcid;
	private String name;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_usertype_key", referencedColumnName="_term_key")
	private Term userType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_userstatus_key", referencedColumnName="_term_key")
	private Term userStatus;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_group_key", referencedColumnName="_term_key")
	private Term group;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
