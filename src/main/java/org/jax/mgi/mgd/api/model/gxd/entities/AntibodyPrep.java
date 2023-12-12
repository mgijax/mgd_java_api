package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "AntibodyPrep Model Object")
@Table(name="gxd_antibodyprep")
public class AntibodyPrep extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_antibodyprep_generator")
	@SequenceGenerator(name="gxd_antibodyprep_generator", sequenceName = "gxd_antibodyprep_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _antibodyprep_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibody_key")
	private Antibody antibody;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_secondary_key", referencedColumnName="_term_key")
	private Term secondary;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_label_key", referencedColumnName="_term_key")
	private Term label;
	
}
