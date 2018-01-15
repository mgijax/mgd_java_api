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
@ApiModel(value = "Term EMAPA Model Object")
@Table(name="voc_annottype")
public class Term_EMAPA extends EntityBase {

	@Id
	private Integer _Term_key;
	private Integer startStage;
	private Integer endStage;
	private Date creation_date;
	private Date modification_date;
	
}
