package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Assay Note Model Object")
@Table(name="gxd_assaynote")
public class AssayNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_assaynote_generator")
	@SequenceGenerator(name="gxd_assaynote_generator", sequenceName = "gxd_assaynote_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _assaynote_key;
	private int _assay_key;
	private String assayNote;
	private Date creation_date;
	private Date modification_date;
}
