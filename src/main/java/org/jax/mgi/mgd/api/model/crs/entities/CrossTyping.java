package org.jax.mgi.mgd.api.model.crs.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Typing Model Object")
@Table(name="crs_typings")
public class CrossTyping extends BaseEntity {

	@EmbeddedId
	private CrossTypingKey key;
	private String data;
	private Date creation_date;
	private Date modification_date;

}
