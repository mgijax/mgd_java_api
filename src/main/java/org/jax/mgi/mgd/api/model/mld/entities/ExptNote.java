package org.jax.mgi.mgd.api.model.mld.entities;

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
@ApiModel(value = "MLD Expt Notes Object")
@Table(name="mld_expt_notes")
public class ExptNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mld_expt_note_generator")
	@SequenceGenerator(name="mld_expt_note_generator", sequenceName = "mld_expt_note_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _expt_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
