package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Genotype Model Object")
@Table(name="gxd_genotype")
public class Genotype extends EntityBase {

	@Id
	private Integer _genotype_key;
	private Integer isConditional;
	private String note;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_existsas_key", referencedColumnName="_term_key")
	private Term existsAs;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_genotype_key", referencedColumnName="_object_key")
	@Where(clause="_mgitype_key = 12 AND preferred = 1 AND _logicaldb_key = 1")
	private Accession mgiAccessionId;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key")
	@Where(clause="_mgitype_key = 12 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
	@Transient
	public Set<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
		return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	}
	
	@Transient
	public Set<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
		HashSet<Accession> set = new HashSet<Accession>();
		for(Accession a: allAccessionIds) {
			if(a.get_logicaldb_key() == db_key) {
				set.add(a);
			}
		}
		return set;
	}

}
