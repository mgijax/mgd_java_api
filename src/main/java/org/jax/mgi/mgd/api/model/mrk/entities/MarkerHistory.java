package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "History Model Object")
@Table(name="mrk_history")
public class MarkerHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mrk_history_generator")
	@SequenceGenerator(name="mrk_history_generator", sequenceName = "mrk_history_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _assoc_key;
	
	private int _marker_key;
	private int sequenceNum;
	private String name;
	
	// don't care about the timestamp for this date
	@Temporal(TemporalType.DATE)
	@Column(columnDefinition = "timestamp")
	private Date event_date;
	
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_event_key", referencedColumnName="_term_key")
	private Term markerEvent;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_eventreason_key", referencedColumnName="_term_key")
	private Term markerEventReason;

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
