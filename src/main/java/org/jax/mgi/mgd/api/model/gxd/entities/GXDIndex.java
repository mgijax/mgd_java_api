package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
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
@Schema(description = "GXD Index Model Object")
@Table(name="gxd_index")
public class GXDIndex extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_index_generator")
	@SequenceGenerator(name="gxd_index_generator", sequenceName = "gxd_index_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _index_key;
	private String comments;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_priority_key", referencedColumnName="_term_key")
	private Term priority;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_conditionalmutants_key", referencedColumnName="_term_key")
	private Term conditionalMutants;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany()
	@JoinColumn(name="_index_key", insertable=false, updatable=false)
	private List<GXDIndexStage> indexStages;
	
//	@OneToMany()
//	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
//	private List<ExpressionCache> expressionCache;
	
}
