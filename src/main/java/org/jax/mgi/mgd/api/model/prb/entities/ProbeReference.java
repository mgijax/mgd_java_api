package org.jax.mgi.mgd.api.model.prb.entities;

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
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBReference Model Object")
@Table(name="prb_reference")
public class ProbeReference extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_reference_generator")
	@SequenceGenerator(name="prb_reference_generator", sequenceName = "prb_reference_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _reference_key;
	private int _probe_key;
	private int hasRmap;
	private int hasSequence;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// accession ids (non-MGI)
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_probe_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 3 and `_logicaldb_key != 1`")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> accessionIds;
	
	// aliases
	@OneToMany()
	@JoinColumn(name="_reference_key", referencedColumnName="_reference_key", insertable=false, updatable=false)
	private List<ProbeAlias> aliases;
	
}
