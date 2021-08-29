package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTExperiment Model Object")
@Table(name="gxd_htexperiment")
public class HTExperiment extends BaseEntity {

	@Id
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

	private Integer _evaluationstate_key;
	private Integer _experimenttype_key;
	private Integer _studytype_key;
	private Integer _curationstate_key;

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

	@OneToOne
	@JoinColumn(name="_evaluatedby_key", referencedColumnName="_user_key")
	private User evaluatedBy; 

	@OneToOne
	@JoinColumn(name="_initialcuratedby_key", referencedColumnName="_user_key")
	private User initialcuratedBy; 

	@OneToOne
	@JoinColumn(name="_lastcuratedby_key", referencedColumnName="_user_key")
	private User lastcuratedBy; 

	@OneToOne
	@JoinColumn(name="_source_key", referencedColumnName="_term_key")
	private Term sourceTerm;
	
	// Editor/Coordinator
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 42 and `_notetype_key` = 1047")
	private List<Note> notes;

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key")
	@Where(clause="`_mgitype_key` = 42")
	@OrderBy(clause="_propertyterm_key desc, sequenceNum")
	private List<MGIProperty> properties;

}








