package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Immutable
@Schema(description = "Relationship Fear (Allele/Marker)")
@Table(name="mgi_relationship_fear_view")
public class RelationshipFearByAllele extends BaseEntity {
	
	@Id	
	private int _relationship_key;
	private int _category_key;
	private String categoryTerm;
	private int _object_key_1;
	private String alleleSymbol;
	private int _object_key_2;
	private String markerSymbol;
	private String markerAccID;
	private int _organism_key;
	private String organism;
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
	private Integer _createdby_key;
	private String createdBy;	
	private Integer _modifiedby_key;
	private String modifiedBy;
	@Column(columnDefinition = "timestamp")
	private String creation_date;
	@Column(columnDefinition = "timestamp")
	private String modification_date; 

	//  1042 | Relationship
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_relationship_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 40 and `_notetype_key` = 1042")
	private List<Note> note;		
}
