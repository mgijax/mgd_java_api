package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;

import jakarta.persistence.Column;
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
@Schema(description = "HTSample Model Object")
@Table(name="gxd_htsample")
public class HTSample extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_htsample_generator")
	@SequenceGenerator(name="gxd_htsample_generator", sequenceName = "gxd_htsample_seq", allocationSize=1)
	@Schema(name="primary key")
	private Integer _sample_key;
	private Integer _experiment_key;
 	private String name;
	private String age;
	@Column(columnDefinition = "numeric")
	private Integer ageMin;
	@Column(columnDefinition = "numeric")
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
