package org.jax.mgi.mgd.api.model.seq.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GeneTrap Model Object")
@Table(name="seq_genetrap")
public class GeneTrap extends BaseEntity {

	@Id
	private Integer _sequence_key;
	private Integer goodHitCount;
	private Integer pointCoordinate;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_tagmethod_key", referencedColumnName="_term_key")
	private Term tagMethod;

	@OneToOne
	@JoinColumn(name="_vectorend_key", referencedColumnName="_term_key")
	private Term vectorEnd;

	@OneToOne
	@JoinColumn(name="_reversecomp_key", referencedColumnName="_term_key")
	private Term reverseComp;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
