package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name="mgi_apilog_object")
public class ApiLogObject {

	@Id
	private Integer _LogObject_key;
	private Integer _object_key;

	@OneToOne (targetEntity=MGIType.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_mgitype_key", referencedColumnName="_mgitype_key")
	private MGIType mgiType;

	@OneToOne (targetEntity=ApiLogEvent.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_event_key", referencedColumnName="_event_key")
	private ApiLogEvent event;
}
