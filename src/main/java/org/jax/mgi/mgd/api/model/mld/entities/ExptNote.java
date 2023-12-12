package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "MLD Expt Notes Object")
@Table(name="mld_expt_notes")
public class ExptNote extends BaseEntity {

	@Id
	private Integer _expt_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
