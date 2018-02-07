package org.jax.mgi.mgd.api.model.ri.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "RI_Summary_Expt_Ref Model Object")
@Table(name="ri_summary_expt_ref")
public class RISummaryExptRef extends EntityBase {

	@EmbeddedId
	private RISummaryExptRefKey key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;

}
