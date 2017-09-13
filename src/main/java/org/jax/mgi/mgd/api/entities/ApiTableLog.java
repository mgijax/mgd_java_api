package org.jax.mgi.mgd.api.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor
@Table(name="mgi_api_log")
public class ApiTableLog {

	@Id
	private Integer key;
	private String type;
	private String oldObject;
	private String newObject;

}
