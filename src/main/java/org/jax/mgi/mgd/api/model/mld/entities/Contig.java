package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Contig Model Object")
@Table(name="mld_contig")
public class Contig extends BaseEntity {

	@Id
	private Integer _contig_key;
	private Integer mincm;
	private Integer maxcm;
	private String name;
	private Integer minLink;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_expt_key")
	private Experiment experiment;
	
}
