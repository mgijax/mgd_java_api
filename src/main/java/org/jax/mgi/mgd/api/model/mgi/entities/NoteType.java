package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Note Type Object")
@Table(name="mgi_notetype")
public class NoteType extends EntityBase {
	@Id
	private Integer _noteType_key;
	private String noteType;
	
	@Column(name="private") // just "private" is a Java reserved word
	private Integer is_private;

	private Date creation_date;
	private Date modification_date;

}
