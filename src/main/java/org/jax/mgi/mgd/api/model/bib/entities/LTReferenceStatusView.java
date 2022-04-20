package org.jax.mgi.mgd.api.model.bib.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Status View Model Object")
@Table(name="bib_status_view")
public class LTReferenceStatusView extends BaseEntity {

	@Id
	private int _refs_key;
	private String ap_status;
	private String go_status;
	private String gxd_status;
	private String pro_status;
	private String qtl_status;
	private String tumor_status;
}
