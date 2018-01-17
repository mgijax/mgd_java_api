package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Annotation Header Model Object")
@Table(name="voc_annotheader")
public class AnnotHeader extends EntityBase {

	@Id
	private Integer _annotHeader_key;
	private Integer sequenceNum;
	private Integer isNormal;
	private Date approval_date;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_annottype_key", referencedColumnName="_annottype_key")
	private AnnotType annotType;
	
	//need to add _Object_key relationships
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_term_key", referencedColumnName="_term_key")
	private Term headerTerm;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_approvedby_key", referencedColumnName="_user_key")
	private User approvedBy;
}
