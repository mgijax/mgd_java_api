package org.jax.mgi.mgd.api.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor
@Table(name="mgi_apievent_log")
public class ApiEventLog extends EntityBase {
	
	@Id
	private Integer key;
	private String event;
	private String jsonObject;

}
