package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Column;
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
@Schema(description = "Probe/Reference Model Object")
@Table(name="prb_reference")
public class ProbeReference extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_reference_generator")
	@SequenceGenerator(name="prb_reference_generator", sequenceName = "prb_reference_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _reference_key;
	private int _probe_key;
	@Column(columnDefinition = "int2")
	private int hasRmap;
	@Column(columnDefinition = "int2")
	private int hasSequence;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// notes
	@OneToMany()
	@JoinColumn(name="_reference_key", referencedColumnName="_reference_key", insertable=false, updatable=false)
	private List<ProbeReferenceNote> referenceNote;	
	
	// aliases
	@OneToMany()
	@JoinColumn(name="_reference_key", referencedColumnName="_reference_key", insertable=false, updatable=false)
	private List<ProbeAlias> aliases;

	// rflvs
	@OneToMany()
	@JoinColumn(name="_reference_key", referencedColumnName="_reference_key", insertable=false, updatable=false)
	private List<ProbeRFLV> rflvs;
	
}
