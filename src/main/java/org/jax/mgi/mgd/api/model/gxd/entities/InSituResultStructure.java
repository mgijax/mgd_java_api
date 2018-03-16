package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "InSituResultStructure Model Object")
@Table(name="gxd_isresultstructure")
public class InSituResultStructure extends BaseEntity {

	@EmbeddedId
	private InSituResultStructureKey key;
	private Date creation_date;
	private Date modification_date;

//	Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure column: _result_key 
//	(should be mapped with insert=\"false\" update=\"false\")
//	@OneToOne
//	@JoinColumn(name="_result_key")
//	private InSituResult inSituResult;
	
	//Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure column: _emapa_term_key 
	//(should be mapped with insert=\"false\" update=\"false\")
	//@OneToOne
	//@JoinColumn(name="_emapa_term_key", referencedColumnName="_term_key")
	//private Term emapaTerm;
	
//	Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure column: _stage_key 
//	(should be mapped with insert=\"false\" update=\"false\"
//	@OneToOne
//	@JoinColumn(name="_stage_key")
//	private TheilerStage stage;
//	


}
