package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Antigen Model Object")
@Table(name="gxd_antigen")
public class Antigen extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_antigen_generator")
	@SequenceGenerator(name="gxd_antigen_generator", sequenceName = "gxd_antigen_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _antigen_key;
	private String antigenName;
	private String regionCovered;
	private String antigenNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_source_key", referencedColumnName="_source_key")
	private ProbeSource probeSource;
	
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_antigen_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 7 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
}
