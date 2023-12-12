package org.jax.mgi.mgd.api.model.acc.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
@Entity
@Schema(description = "AccessionMax Model Object")
@Table(name="acc_accessionmax")
public class AccessionMax extends BaseEntity {
	@Id
	private String prefixPart;
	private Integer maxNumericPart;
	private Date creation_date;
	private Date modification_date;
}
