package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGISet Member Object")
@Table(name="mgi_setmember")
public class MGISetMember extends EntityBase {
	@Id
	private Integer _setmember_key;
	
	private Integer _object_key;
	private String label;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_set_key")
	private MGISet mgiSet;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="mgiSet._mgitype_key = 13")
	private Term term;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_logicaldb_key", insertable=false, updatable=false)
	@Where(clause="mgiSet._mgitype_key = 15")
	private LogicalDB logicalDb;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_actualdb_key", insertable=false, updatable=false)
	@Where(clause="mgiSet._mgitype_key = 16")
	private ActualDB actualDb;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_statistic_key", insertable=false, updatable=false)
	@Where(clause="mgiSet._mgitype_key = 34")
	private MGIStatistic stat;
	
}
