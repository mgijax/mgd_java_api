package org.jax.mgi.mgd.api.model.prb.entities;

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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe Model Object")
@Table(name="prb_probe")
public class Probe extends EntityBase {

	@Id
	private Integer _probe_key;
	private String name;
	private Integer derivedFrom;
	private String primer1sequence;
	private String primer2sequence;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String productSize;
	private Date creation_date;
	private Date modification_date;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_source_key")
	private ProbeSource source;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term vector;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_segmenttype_key", referencedColumnName="_term_key")
	private Term segmentType;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// Complex Many to Many
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_probe_key")
	private Set<ProbeMarker> probeMarkers;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_probe_key", referencedColumnName="_object_key")
	@Where(clause="_mgitype_key = 3 AND preferred = 1 AND _logicaldb_key = 1")
	private Accession mgiAccessionId;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_probe_key")
	@Where(clause="_mgitype_key = 3 AND preferred = 1")
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
