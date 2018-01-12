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
@ApiModel(value = "Note Chunk Object")
@Table(name="mgi_notechunk")
public class NoteChunk extends EntityBase {
	@Id
	private Integer _note_key;
	private String sequenceNum;
	private Date creation_date;
	private Date modification_date;

}
