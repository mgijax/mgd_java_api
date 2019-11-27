package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Allele Mutation Model Object")
@Table(name="all_allele_mutation")
public class AlleleMutation extends BaseEntity {

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_allelemutation_generator")
	//@SequenceGenerator(name="all_allelemutation_generator", sequenceName = "all_allelemutation_seq", allocationSize=1)
	//@ApiModelProperty(value="primary key")
	//private int _allelemutation_key;
	
	private int _allele_key;
	private Date creation_date;
	private Date modification_date;
		 
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mutation_key")
	private Term mutation;
	
}
