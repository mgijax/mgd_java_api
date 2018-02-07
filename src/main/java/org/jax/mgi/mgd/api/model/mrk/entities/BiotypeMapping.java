package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "BiotypeMapping Model Object")
@Table(name="mrk_biotypemapping")
public class BiotypeMapping extends EntityBase {

	@Id
	private Integer _biotypemapping_key;
	private Integer useMCVchildren;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_biotypevocab_key", referencedColumnName="_vocab_key")
	private Vocabulary biotypeVocab;
	
	@OneToOne
	@JoinColumn(name="_biotypeterm_key", referencedColumnName="_term_key")
	private Term biotypeTerm;
	
	@OneToOne
	@JoinColumn(name="_mcvterm_key", referencedColumnName="_term_key")
	private Term mcvTerm;
	
	@OneToOne
	@JoinColumn(name="_marker_type_key")
	private MarkerType markerType;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
