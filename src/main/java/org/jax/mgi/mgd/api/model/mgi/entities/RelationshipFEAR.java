package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Immutable
@ApiModel(value = "Relationship FEAR (Allele/Marker)")
@Table(name="mgi_relationship_fear_view")
public class RelationshipFEAR extends BaseEntity {
	
	@Id	
	private int _relationship_key;
	private int _categtory_key;
	private String categoryTerm;
	private int _allele_key;
	private String alleleSymbol;
	private String accID;
	private int _marker_key;
	private String markerSymbol;
	private int _relationshipterm_key;
	private String relationshipTerm;
	private int _qualifier_key;
	private String qualifierTerm;
	private int _evidence_key;
	private String evidenceTerm;
	private int _refs_key;
	private String jnumid;
	private int jnum;
	private String short_citation;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 

}
