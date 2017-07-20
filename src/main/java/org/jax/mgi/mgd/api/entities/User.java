package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "User Model Object")
@Table(name="mgi_user")
public class User extends Base {
	@Id
	@Column(name="_user_key")
	public int _user_key;

	@Column(name="login")
	public String login;

	@Column(name="name")
	public String name;
}
