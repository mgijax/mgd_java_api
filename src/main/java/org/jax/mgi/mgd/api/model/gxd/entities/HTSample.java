package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
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
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTSample Model Object")
@Table(name="gxd_htsample")
public class HTSample extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_htsample_generator")
	@SequenceGenerator(name="gxd_htsample_generator", sequenceName = "gxd_htsample_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private Integer _sample_key;
	private Integer _experiment_key;
 	private String name;
	private String age;
	private Integer ageMin;
	private Integer ageMax;	
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_relevance_key", referencedColumnName="_term_key")
	private Term relevance;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sex_key", referencedColumnName="_term_key")
	private Term sex;
 	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_celltype_term_key", referencedColumnName="_term_key")
	private Term cellTypeTerm;
	
	// _emapa_key is mapped to two objects, and one must be insert/update false
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_emapa_key", referencedColumnName="_term_key")
	private Term emapaTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_emapa_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	private TermEMAPA emapaObject;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_stage_key")
	private TheilerStage theilerStage;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// notes
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_sample_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 43 and `_notetype_key` = 1048")
	private List<Note> notes;

}
