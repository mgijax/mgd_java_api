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
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GelLaneStructure Model Object")
@Table(name="gxd_gellanestructure")
public class GelLaneStructure extends EntityBase {

	@EmbeddedId
	private GLStructureKey key;
	private Date creation_date;
	private Date modification_date;
	
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_gellane_key")
//	private GelLane gelLane;
//	
//	Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.GelLaneStructure column: _emapa_term_key 
//	(should be mapped with insert=\"false\" update=\"false\")
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_emapa_term_key", referencedColumnName="_term_key")
//	private Term emapaTerm;
//	
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_stage_key")
//	private TheilerStage stage;
	
	@Getter @Setter
	@Embeddable
	public class GLStructureKey implements Serializable {
		private Integer _gellane_key;
		private Integer _emapa_term_key;
		private Integer _stage_key;
	}
	
}
