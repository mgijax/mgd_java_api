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
@Schema(description = "GelBand Model Object")
@Table(name="gxd_gelband")
public class GelBand extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_gelband_generator")
	@SequenceGenerator(name="gxd_gelband_generator", sequenceName = "gxd_gelband_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _gelband_key;
	private int _gellane_key;
	private String bandNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strength_key", referencedColumnName="_term_key")
	private Term strength;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gelrow_key")
	private GelRow gelRow;
		
}
