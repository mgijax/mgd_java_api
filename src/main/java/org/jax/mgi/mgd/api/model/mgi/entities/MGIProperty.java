package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGI_Property Object")
@Table(name="mgi_property")
public class MGIProperty extends EntityBase {
	@Id
	private Integer _propertyType_key;
	private String value;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_propertytype_key")
	private PropertyType propertyType;
	
	@OneToOne
	@JoinColumn(name="_propertyterm_key", referencedColumnName="_term_key")
	private Term propertyTerm;
	
	@OneToOne
	@JoinColumn(name="_mgitype_key")
	private MGIType mgitype;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
