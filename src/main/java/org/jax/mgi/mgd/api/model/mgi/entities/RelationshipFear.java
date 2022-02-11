package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.List;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Immutable
@ApiModel(value = "Relationship Fear (Allele/Marker)")
@Table(name="mgi_relationship_fear_view")
public class RelationshipFear extends BaseEntity {
	
	@Id	
	private int _relationship_key;
	private int _category_key;
	private String categoryTerm;
	private int _object_key_1;
	private String alleleSymbol;
	private int _object_key_2;
	private String markerSymbol;
	private int _relationshipTerm_key;
	private String relationshipTerm;
	private int _qualifier_key;
	private String qualifierTerm;
	private int _evidence_key;
	private String evidenceTerm;
	private int _refs_key;
	private String jnumid;
	private int jnum;
	private String short_citation;
	private String _createdby_key;
	private String createdBy;	
	private String _modifiedby_key;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 

	// relationship/property
	@OneToMany()
	@JoinColumn(name="_relationship_key", insertable=false, updatable=false)
	private List<RelationshipProperty> properties;
	
}
