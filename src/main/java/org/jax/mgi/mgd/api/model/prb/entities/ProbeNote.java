package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBNotes Model Object")
@Table(name="prb_notes")
public class ProbeNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_notes_generator")
	@SequenceGenerator(name="prb_notes_generator", sequenceName = "prb_notes_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _note_key;
	private int _probe_key;
	private String note;
	private Date creation_date;
	private Date modification_date;

}
