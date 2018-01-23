package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Concordance Model Object")
@Table(name="mld_concordance")
public class Concordance extends EntityBase {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private String chromosome;
	private Integer cpp;
	private Integer cpn;
	private Integer cnp;
	private Integer cnn;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Marker marker;
	
}
