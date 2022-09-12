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
@ApiModel(value = "GelBand Model Object")
@Table(name="gxd_gelband")
public class GelBand extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_gelband_generator")
	@SequenceGenerator(name="gxd_gelband_generator", sequenceName = "gxd_gelband_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _gelband_key;
	private int _gellane_key;
	private String bandNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strength_key", referencedColumnName="_term_key")
	private Term strength;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gelrow_key")
	private GelRow gelRow;
		
}
