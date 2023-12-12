package org.jax.mgi.mgd.api.model.gxd.entities;

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
@Schema(description = "Assay Note Model Object")
@Table(name="gxd_assaynote")
public class AssayNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_assaynote_generator")
	@SequenceGenerator(name="gxd_assaynote_generator", sequenceName = "gxd_assaynote_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _assaynote_key;
	private int _assay_key;
	private String assayNote;
	private Date creation_date;
	private Date modification_date;
}
