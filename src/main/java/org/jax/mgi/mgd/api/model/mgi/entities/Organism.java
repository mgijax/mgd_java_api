package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mrk.entities.Chromosome;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Organism Model Object")
@Table(name="mgi_organism")
public class Organism extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mgi_organism_generator")
	@SequenceGenerator(name="mgi_organism_generator", sequenceName = "mgi_organism_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _organism_key;
	
	private String commonname;
	private String latinname;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 20 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_organism_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 20 AND preferred = 1")
	private List<Accession> allAccessionIds;

	@OneToMany()
	@JoinColumn(name="_organism_key", insertable=false, updatable=false)
	private List<OrganismMGIType> mgiTypes;

	@OneToMany()
	@JoinColumn(name="_organism_key", insertable=false, updatable=false)
	private List<Chromosome> chromosomes;	
	
}
