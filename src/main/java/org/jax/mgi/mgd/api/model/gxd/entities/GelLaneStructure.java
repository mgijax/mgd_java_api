package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GelLaneStructure Model Object")
@Table(name="gxd_gellanestructure")
public class GelLaneStructure extends EntityBase {

	@EmbeddedId
	private GelLaneStructureKey key;
	private Date creation_date;
	private Date modification_date;
	
//	@OneToOne
//	@JoinColumn(name="_gellane_key")
//	private GelLane gelLane;
//	
//	Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.GelLaneStructure column: _emapa_term_key 
//	(should be mapped with insert=\"false\" update=\"false\")
//	@OneToOne
//	@JoinColumn(name="_emapa_term_key", referencedColumnName="_term_key", insertable=false, updatable=false)
//	private Term emapaTerm;
//	
//	@OneToOne
//	@JoinColumn(name="_stage_key")
//	private TheilerStage stage;

	
}
