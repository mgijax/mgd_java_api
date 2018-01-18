package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_biotypevocab_key", referencedColumnName="_vocab_key")
	private Vocabulary biotypeVocab;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_biotypeterm_key", referencedColumnName="_term_key")
	private Term biotypeTerm;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_mcvterm_key", referencedColumnName="_term_key")
	private Term mcvTerm;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_type_key", referencedColumnName="_marker_type_key")
	private Type markerType;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

}
