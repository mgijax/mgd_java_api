package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "User Model Object")
@Table(name="mgi_user")
public class User extends Base {
	@Id
	@Column(name="_user_key")
	private int _user_key;

	@Column(name="login")
	public String login;

	@Column(name="name")
	private String name;
}
