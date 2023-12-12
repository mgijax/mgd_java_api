package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Allele Mutation Model Object")
@Table(name="all_allele_mutation")
public class AlleleMutation extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_allele_mutation_generator")
	@SequenceGenerator(name="all_allele_mutation_generator", sequenceName = "all_allele_mutation_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _assoc_key;	
	private int _allele_key;
	private Date creation_date;
	private Date modification_date;
		 
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mutation_key")
	private Term mutation;
	
}
