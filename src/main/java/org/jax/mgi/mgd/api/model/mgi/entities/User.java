package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "User Model Object")
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
