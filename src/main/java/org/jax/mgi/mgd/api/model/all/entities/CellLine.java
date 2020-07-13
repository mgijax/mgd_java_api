package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "CellLine Model Object")
@Table(name="all_cellline")
public class CellLine extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_cellline_generator")
	@SequenceGenerator(name="all_cellline_generator", sequenceName = "all_cellline_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _cellline_key;
	private String cellLine;
	private int isMutant;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_cellline_type_key", referencedColumnName="_term_key")
	private Term cellLineType;
	
	@OneToOne
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	@OneToOne
	@JoinColumn(name="_derivation_key")
	private AlleleCellLineDerivation derivation;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// editable only accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_cellline_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 28")
	@OrderBy(clause ="accid")
	private List<Accession> editAccessionIds;	
}
