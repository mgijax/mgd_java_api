package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIKeyValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTRawSample Model Object")
@Table(name="gxd_htrawsample")
public class HTRawSample extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_htrawsample_generator")
	@SequenceGenerator(name="gxd_htrawsample_generator", sequenceName = "gxd_htrawsample_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private Integer _rawsample_key;
	private Integer _experiment_key;
	private String accid;
	private Date creation_date;
	private Date modification_date;
 		
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// key value pairs
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_rawsample_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 47 ")
	private List<MGIKeyValue> keyValuePairs;

}
