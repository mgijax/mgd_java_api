package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.gxd.entities.TheilerStage;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Term EMAPS Model Object")
@Table(name="voc_term_emaps")
public class TermEMAPS extends BaseEntity {

	@Id
	private Integer _Term_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_stage_key")
	private TheilerStage stage;

	@OneToOne
	@JoinColumn(name="_defaultparent_key", referencedColumnName="_term_key")
	private Term defaultParent;

	@OneToOne
	@JoinColumn(name="_EMAPA_Term_key", referencedColumnName="_term_key")
	private Term emapaTerm;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
