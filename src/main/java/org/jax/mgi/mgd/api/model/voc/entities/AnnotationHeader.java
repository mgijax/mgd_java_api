package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Annotation Header Model Object")
@Table(name="voc_annotheader")
public class AnnotationHeader extends BaseEntity {

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voc_annotheader_generator")
	//@SequenceGenerator(name="voc_annotheader_generator", sequenceName = "voc_annotheader_seq", allocationSize=1)
	//@ApiModelProperty(value="primary key")	
	private int _annotheader_key;
	private Integer _object_key;
	private Integer sequenceNum;
	private Integer isNormal;
	private Date approval_date;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_annottype_key")
	private AnnotationType annotType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_term_key")
	private Term term;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_approvedby_key", referencedColumnName="_user_key")
	private User approvedBy;
	
}
