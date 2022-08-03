package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@ApiModel(value = "InSituResult Model Object")
@Table(name="gxd_insituresult")
public class InSituResult extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_insituresult_generator")
	@SequenceGenerator(name="gxd_insituresult_generator", sequenceName = "gxd_insituresult_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")		
	private int _result_key;
	private int _specimen_key;
	private Integer sequenceNum;
	private String resultNote;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strength_key", referencedColumnName="_term_key")
	private Term strength;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_pattern_key", referencedColumnName="_term_key")
	private Term pattern;

	@OneToMany()
	@JoinColumn(name="_result_key", insertable=false, updatable=false)
	private List<InSituResultStructure> structures;

	@OneToMany()
	@JoinColumn(name="_result_key", insertable=false, updatable=false)
	private List<InSituResultCellType> celltypes;
	
	@OneToMany()
	@JoinColumn(name="_result_key", insertable=false, updatable=false)
	private List<InSituResultImageView> imagePanes;
		
}
