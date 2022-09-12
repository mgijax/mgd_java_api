package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Antibody Model Object")
@Table(name="gxd_antibody")
public class Antibody extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_antibody_generator")
	@SequenceGenerator(name="gxd_antibody_generator", sequenceName = "gxd_antibody_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _antibody_key;
	private String antibodyName;
	private String antibodyNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibodyclass_key", referencedColumnName="_term_key")
	private Term antibodyClass;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibodytype_key", referencedColumnName="_term_key")
	private Term antibodyType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antigen_key", referencedColumnName="_antigen_key")
	private Antigen antigen;
		
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_antibody_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 6 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;

	// antibody alias
	@OneToMany()
	@JoinColumn(name="_antibody_key", referencedColumnName="_antibody_key", insertable=false, updatable=false)
	private List<AntibodyAlias> aliases;	
	
	// antibody marker
	@OneToMany()
	@JoinColumn(name="_antibody_key", referencedColumnName="_antibody_key", insertable=false, updatable=false)
	private List<AntibodyMarker> markers;

	// reference associations
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_antibody_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 6")
	private List<MGIReferenceAssoc> refAssocs;

}
