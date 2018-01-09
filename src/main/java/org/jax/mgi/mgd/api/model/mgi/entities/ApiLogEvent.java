package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.jax.mgi.mgd.api.model.EntityBase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name="mgi_apilog_event")
public class ApiLogEvent extends EntityBase {
	
	@Id
	private Integer _event_key;
	private String endpoint;
	private String parameters;
	private Date creation_date;

	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_user_key", referencedColumnName="_user_key")
	private User createdBy;
	
	@OneToMany (targetEntity=ApiLogObject.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_event_key", referencedColumnName="_event_key")
	@BatchSize(size=200)
//	@Fetch(value=FetchMode.SUBSELECT)
	private List<ApiLogObject> objects;
}
