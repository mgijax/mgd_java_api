package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTSample Model Object")
@Table(name="gxd_htsample")
public class HTSample extends BaseEntity {

	@Id
	private Integer _sample_key;
	private Integer _experiment_key;
	private Integer _organism_key;
	private Integer _relevance_key;
	private Integer _sex_key;
	private Integer _emapa_key;
	private Integer _stage_key;
 	private String name;
	private String age;
	private Date creation_date;
	private Date modification_date;
 
	// notes
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_sample_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 43 and `_notetype_key` = 1048")
	private List<Note> notes;
	
	@OneToOne
	@JoinColumn(name="_emapa_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	private Term emapaTerm;

	@OneToOne
	@JoinColumn(name="_emapa_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	private TermEMAPA emapaObject;
		
	@OneToOne
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
