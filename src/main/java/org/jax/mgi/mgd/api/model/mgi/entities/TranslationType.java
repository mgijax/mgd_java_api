package org.jax.mgi.mgd.api.model.mgi.entities;

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
@ApiModel(value = "Translation Type Object")
@Table(name="mgi_translationtype")
public class TranslationType extends EntityBase {
	@Id
	private Integer _translationType_key;
	private String translationType;
	private String compressionChars;
	private Integer regularExpression;
	private Date creation_date;
	private Date modification_date;

}
