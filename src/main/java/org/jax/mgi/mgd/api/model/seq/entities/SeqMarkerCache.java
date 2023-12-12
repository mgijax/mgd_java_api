package org.jax.mgi.mgd.api.model.seq.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
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
@Schema(description = "Seq Marker Cache Entity Object")
@Table(name="seq_marker_cache")
public class SeqMarkerCache extends BaseEntity {

	@Id
	@Schema(name="primary key")
	private int _cache_key;
    private int _marker_key;
    private int _sequence_key;
    private int _organism_key;   
    private int _refs_key; 
    private int _qualifier_key;
    private int _sequenceType_key;
    private int _logicalDB_key;
    private int _marker_Type_key;
    private int _biotypeConflict_key;
    private String accID;
    private String rawbiotype;    
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sequenceprovider_key", referencedColumnName="_term_key")
	private Term sequenceProvider;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
