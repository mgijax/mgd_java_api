package org.jax.mgi.mgd.api.model.gxd.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Index Stage Model Object")
@Table(name="gxd_index_stages")
public class IndexStage extends EntityBase {

	@EmbeddedId
	private IndexStageKey key;
	private Date creation_date;
	private Date modification_date;
	
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	

//	 Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.IndexStage column: _index_key 
//	 (should be mapped with insert=\"false\" update=\"false\
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_index_key")
//	private Index index;
//
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_indexassay_key", referencedColumnName="_term_key")
//	private Term indexAssay;
//	
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_stageid_key", referencedColumnName="_term_key")
//	private Term indexStage;
	
	@Getter @Setter
	@Embeddable
	public class IndexStageKey implements Serializable {
		private Integer _index_key;
		private Integer _indexassay_key;
		private Integer _stageid_key;
	}
}
