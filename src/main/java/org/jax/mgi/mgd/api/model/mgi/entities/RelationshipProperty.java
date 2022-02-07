package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Relationship Property Object")
@Table(name="mgi_relationship_property")
public class RelationshipProperty extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mgi_relationship_property_generator")
	@SequenceGenerator(name="mgi_relationship_property_generator", sequenceName = "mgi_relationship_property_seq", allocationSize=1)		
	private int _relationshipproperty_key;
	private int _relationship_key;
	private String value;
	private int sequenceNum;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_propertyname_key", referencedColumnName="_term_key")
	private Term propertyName;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
}
