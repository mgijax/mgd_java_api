package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "History Model Object")
@Table(name="mrk_history")
public class MarkerHistory extends EntityBase {

	@EmbeddedId
	private MarkerHistoryKey key;
	private String name;
	private Date event_date;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_marker_event_key")
	private Event markerEvent;

	@OneToOne
	@JoinColumn(name="_marker_eventreason_key")
	private EventReason eventReason;

	@OneToOne
	@JoinColumn(name="_history_key", referencedColumnName="_marker_key")
	private Marker markerHistory;

	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
