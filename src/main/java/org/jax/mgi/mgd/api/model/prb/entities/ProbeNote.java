package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Probe/Notes Model Object")
@Table(name="prb_notes")
public class ProbeNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_notes_generator")
	@SequenceGenerator(name="prb_notes_generator", sequenceName = "prb_notes_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _note_key;
	private int _probe_key;
	private String note;
	private Date creation_date;
	private Date modification_date;

}
