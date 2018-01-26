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
@ApiModel(value = "Text Model Object")
@Table(name="voc_text")
public class VocabularyText extends EntityBase {

	@Id
	private Integer _Term_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
	
}
