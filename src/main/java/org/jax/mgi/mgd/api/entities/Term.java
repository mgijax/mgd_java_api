package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Term Model Object")
@Table(name="voc_term")
public class Term extends EntityBase {

	@Id
	private Integer _term_key;
	private String term;
	private String abbreviation;
	private Integer sequenceNum;
	private Integer isObsolete;
	private Date creation_date;
	private Date modification_date;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_vocab_key", referencedColumnName="_vocab_key")
	private Vocabulary vocab;
	
	
}
