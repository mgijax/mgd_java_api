package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGI Translation Type Object")
@Table(name="mgi_translationtype")
public class MGITranslationType extends BaseEntity {
	@Id
	private Integer _translationType_key;
	private String translationType;
	private String compressionChars;
	private Integer regularExpression;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_mgitype_key")
	private MGIType mgiType;
	
	@OneToOne
	@JoinColumn(name="_vocab_key")
	private Vocabulary vocab;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany
	@JoinColumn(name="_translationType_key")
	private List<MGITranslation> translations;
}
