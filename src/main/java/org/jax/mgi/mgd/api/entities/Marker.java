package org.jax.mgi.mgd.api.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Marker Model Object")
@Table(name="mrk_marker")
public class Marker extends EntityBase {

	@Id
	private Long _marker_key;
	
	@OneToMany(mappedBy="_object_key", fetch=FetchType.EAGER)
	@Where(clause="_mgitype_key = 2 AND preferred = 1")
	private List<AccessionID> accessionIDs;

	private String symbol;
	private String name;
	private String chromosome;
	
	
}
