package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Term EMAPS Model Object")
@Table(name="voc_term_emaps")
public class TermEMAPS extends BaseEntity {

	@Id
	private int _term_key;
	private int _stage_key;
	private int _EMAPA_Term_key;
	private Date creation_date;
	private Date modification_date;

//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="_stage_key")
//	private TheilerStage stage;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_term_key", referencedColumnName="_term_key")
	private Term term;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_defaultparent_key", referencedColumnName="_term_key")
	private Term defaultParent;

//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="_EMAPA_Term_key", referencedColumnName="_term_key")
//	private Term emapaTerm;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
