package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GelLaneStructure Model Object")
@Table(name="gxd_gellanestructure")
public class GelLaneStructure extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_gellanestructure_generator")
	@SequenceGenerator(name="gxd_gellanestructure_generator", sequenceName = "gxd_gellanestructure_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _gellanestructure_key;
	private int _gellane_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_emapa_term_key", referencedColumnName="_term_key")
	private Term emapaTerm;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_stage_key")
	private TheilerStage theilerStage;		
}
