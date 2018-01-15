package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Annotation Header Model Object")
@Table(name="voc_annotheader")
public class AnnotHeader extends EntityBase {

	@Id
	private Integer _annotHeader_key;
	private Integer sequenceNum;
	private Integer isNormal;
	private Date approval_date;
	private Date creation_date;
	private Date modification_date;
	
}
