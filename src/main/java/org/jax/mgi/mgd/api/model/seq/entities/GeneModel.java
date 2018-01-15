package org.jax.mgi.mgd.api.model.seq.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GeneModel Model Object")
@Table(name="seq_genemodel")
public class GeneModel extends EntityBase {

	@Id
	private Integer _sequence_key;
	private String rawBiotype;
	private Integer exonCount;
	private Integer transcriptCount;
	private Date creation_date;
	private Date modification_date;
}
