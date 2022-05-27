package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Index Stage Model Object")
@Table(name="gxd_index_stages")
public class IndexStage extends BaseEntity {

	@EmbeddedId
	private IndexStageKey key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	

//	 Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.IndexStage column: _index_key 
//	 (should be mapped with insert=\"false\" update=\"false\
//	@OneToOne
//	@JoinColumn(name="_index_key")
//	private Index index;
//
//	@OneToOne
//	@JoinColumn(name="_indexassay_key", referencedColumnName="_term_key")
//	private Term indexAssay;
//	
//	@OneToOne
//	@JoinColumn(name="_stageid_key", referencedColumnName="_term_key")
//	private Term indexStage;
	

}
