package org.jax.mgi.mgd.api.model.gxd.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

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
@Schema(description = "HTExperiment Model Object")
@Table(name="gxd_htexperiment")
public class HTExperiment extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_htexperiment_generator")
	@SequenceGenerator(name="gxd_htexperiment_generator", sequenceName = "gxd_htexperiment_seq", allocationSize=1)
	@Schema(name="primary key")	
	private Integer _experiment_key;
	private String name;
	private String description;
	private Date release_date;
	private Date lastupdate_date;
	private Date evaluated_date;
	private Date initial_curated_date;
	private Date last_curated_date;
	private Date creation_date;
	private Date modification_date;
	private BigDecimal confidence;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 42 and `_logicaldb_key` = 189")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> primaryIDs;
	
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 42 and `_logicaldb_key` = 190")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> secondaryIDs;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_evaluatedby_key", referencedColumnName="_user_key")
	private User evaluatedBy; 

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_initialcuratedby_key", referencedColumnName="_user_key")
	private User initialcuratedBy; 

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_lastcuratedby_key", referencedColumnName="_user_key")
	private User lastcuratedBy; 

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_source_key", referencedColumnName="_term_key")
	private Term sourceTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_evaluationstate_key", referencedColumnName="_term_key")
	private Term evaluationState;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_experimenttype_key", referencedColumnName="_term_key")
	private Term experimentType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_studytype_key", referencedColumnName="_term_key")
	private Term studyType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_curationstate_key", referencedColumnName="_term_key")
	private Term curationState;

	// notes
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 42 and `_notetype_key` = 1047")
	private List<Note> notes;

	// properties
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 42")
	@OrderBy(clause="_propertyterm_key desc, sequenceNum")
	private List<MGIProperty> properties;

	// experiment variables
	@OneToMany()
	@JoinColumn(name="_experiment_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	private List<HTExperimentVariable> experiment_variables;

	// samples
	@OneToMany()
	@JoinColumn(name="_experiment_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	@OrderBy(clause="name")
	private List<HTSample> samples;

}









