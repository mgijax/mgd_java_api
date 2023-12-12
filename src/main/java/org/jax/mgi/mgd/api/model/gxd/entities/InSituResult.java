package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "InSituResult Model Object")
@Table(name="gxd_insituresult")
public class InSituResult extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_insituresult_generator")
	@SequenceGenerator(name="gxd_insituresult_generator", sequenceName = "gxd_insituresult_seq", allocationSize=1)
	@Schema(name="primary key")		
	private int _result_key;
	private int _specimen_key;
	private Integer sequenceNum;
	private String resultNote;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strength_key", referencedColumnName="_term_key")
	private Term strength;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_pattern_key", referencedColumnName="_term_key")
	private Term pattern;

	@OneToMany()
	@JoinColumn(name="_result_key", insertable=false, updatable=false)
	private List<InSituResultStructure> structures;

	@OneToMany()
	@JoinColumn(name="_result_key", insertable=false, updatable=false)
	private List<InSituResultCellType> celltypes;
	
	@OneToMany()
	@JoinColumn(name="_result_key", insertable=false, updatable=false)
	private List<InSituResultImageView> imagePanes;
		
}
