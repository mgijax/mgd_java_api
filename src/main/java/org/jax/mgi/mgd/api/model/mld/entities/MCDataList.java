package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MCDataList Model Object")
@Table(name="mld_mcdatalist")
public class MCDataList extends BaseEntity {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private String alleleLine;
	private Integer offspringNmbr;
	private Date creation_date;
	private Date modification_date;
}
