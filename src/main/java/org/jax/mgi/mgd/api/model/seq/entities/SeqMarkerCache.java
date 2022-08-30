package org.jax.mgi.mgd.api.model.seq.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Seq Marker Cache Entity Object")
@Table(name="seq_marker_cache")
public class SeqMarkerCache extends BaseEntity {

	@Id
	@ApiModelProperty(value="primary key")
	private int _cache_key;
    private int _marker_key;
    private int _sequence_key;
    private int _organism_key;   
    private int _refs_key; 
    private int _qualifier_key;
    private int _sequenceProvider_key;
    private int _sequenceType_key;
    private int _logicalDB_key;
    private int _marker_Type_key;
    private int _biotypeConflict_key;
    private String accID;
    private String rawbiotype;    
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
