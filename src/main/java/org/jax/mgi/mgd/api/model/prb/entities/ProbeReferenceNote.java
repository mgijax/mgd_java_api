package org.jax.mgi.mgd.api.model.prb.entities;

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
@Schema(description = "Probe/Reference/Notes Model Object")
@Table(name="prb_ref_notes")
public class ProbeReferenceNote extends BaseEntity {

	@Id
	private int _reference_key;
	private String note;
	private Date creation_date;
	private Date modification_date;
}
