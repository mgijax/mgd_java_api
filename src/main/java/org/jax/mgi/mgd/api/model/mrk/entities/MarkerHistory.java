package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "History Model Object")
@Table(name="mrk_history")
public class MarkerHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mrk_history_generator")
	@SequenceGenerator(name="mrk_history_generator", sequenceName = "mrk_history_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _assoc_key;
	
	private int _marker_key;
	private int sequenceNum;
	private String name;
	
	// don't care about the timestamp for this date
	@Temporal(TemporalType.DATE)
	private Date event_date;
	
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_event_key")
	private Event markerEvent;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_eventreason_key")
	private EventReason markerEventReason;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_history_key", referencedColumnName="_marker_key")
	private Marker markerHistory;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
