package org.jax.mgi.mgd.api.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Marker Model Object")
@Table(name="mrk_marker")
@PrimaryKeyJoinColumn(name = "_object_key")
public class Marker extends Base {

	@Id
	public Long _marker_key;
	
	@OneToMany(mappedBy="_object_key", fetch=FetchType.EAGER)
	public List<Accession> accessions;
	
	public String symbol;
	public String name;
	public String chromosome;
	
	
}
