package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Term EMAPA Model Object")
@Table(name="voc_term_emapa")
public class TermEMAPA extends BaseEntity {

	@Id
	private int _Term_key;
	private Integer startStage;
	private Integer endStage;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_defaultparent_key", referencedColumnName="_term_key")
	private Term defaultParent;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToMany()
//	@JoinColumn(name="_Term_key", referencedColumnName="_EMAPA_Term_key", insertable=false, updatable=false)
	@JoinColumn(name="_EMAPA_Term_key", referencedColumnName="_Term_key", insertable=false, updatable=false)
	private List<TermEMAPS> emapsTerms;

}
