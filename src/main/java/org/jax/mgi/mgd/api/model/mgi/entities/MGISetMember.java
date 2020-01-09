package org.jax.mgi.mgd.api.model.mgi.entities;

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

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGISet Member")
@Table(name="mgi_setmember")
public class MGISetMember extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mgi_setmember_generator")
	@SequenceGenerator(name="mgi_setmember_generator", sequenceName = "mgi_setmember_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _setmember_key;

	private int _object_key;
	private String label;
	private int sequenceNum;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_set_key")
	private MGISet mgiSet;

//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
//	@Where(clause="mgiSet.`_mgitype_key` = 13")
//	private Term term;
//	
//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="_object_key", referencedColumnName="_logicaldb_key", insertable=false, updatable=false)
//	@Where(clause="mgiSet.`_mgitype_key` = 15")
//	private LogicalDB logicalDb;
//	
//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="_object_key", referencedColumnName="_actualdb_key", insertable=false, updatable=false)
//	@Where(clause="mgiSet.`_mgitype_key` = 16")
//	private ActualDB actualDb;
	
}
