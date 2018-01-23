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
@ApiModel(value = "InSituResultStructure Model Object")
@Table(name="gxd_isresultstructure")
public class InSituResultStructure extends EntityBase {

	@EmbeddedId
	private ISResultStructureKey key;
	private Date creation_date;
	private Date modification_date;

//	Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure column: _result_key 
//	(should be mapped with insert=\"false\" update=\"false\")
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_result_key")
//	private InSituResult inSituResult;
	
	//Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure column: _emapa_term_key 
	//(should be mapped with insert=\"false\" update=\"false\")
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_emapa_term_key", referencedColumnName="_term_key")
	//private Term emapaTerm;
	
//	Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure column: _stage_key 
//	(should be mapped with insert=\"false\" update=\"false\"
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="_stage_key")
//	private TheilerStage stage;
//	
	@Getter @Setter
	@Embeddable
	public class ISResultStructureKey implements Serializable {
		private Integer _result_key;
		private Integer _emapa_term_key;
		private Integer _stage_key;
	}

}
