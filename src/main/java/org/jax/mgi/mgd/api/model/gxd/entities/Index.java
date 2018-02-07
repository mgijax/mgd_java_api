package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GXD Index Model Object")
@Table(name="gxd_index")
public class Index extends EntityBase {

	@Id
	private Integer _index_key;
	private String comments;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
	@OneToOne
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
	@OneToOne
	@JoinColumn(name="_priority_key", referencedColumnName="_term_key")
	private Term priority;
	
	@OneToOne
	@JoinColumn(name="_conditionalmutants_key", referencedColumnName="_term_key")
	private Term conditionalMutants;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany
	@JoinColumn(name="_index_key")
	private Set<TheilerStage> stages;
}
