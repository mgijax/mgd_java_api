package org.jax.mgi.mgd.api.model.img.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Image Model Object")
@Table(name="img_image")
public class Image extends BaseEntity {

	@Id
	private Integer _image_key;
	private Integer xDim;
	private Integer yDim;
	private String figureLabel;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_mgitype_key")
	private MGIType mgitype;
	
	@OneToOne
	@JoinColumn(name="_imageclass_key", referencedColumnName="_term_key")
	private Term imageClass;
	
	@OneToOne
	@JoinColumn(name="_imagetype_key", referencedColumnName="_term_key")
	private Term imageType;
	
	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
	@OneToOne
	@JoinColumn(name="_thumbnailimage_key", referencedColumnName="_image_key")
	private Image thumbnailImage;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne
	@JoinColumn(name="_image_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 9 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_image_key")
	@Where(clause="`_mgitype_key` = 9 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
	@Transient
	public Set<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
		return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	}
	
	@Transient
	public Set<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
		HashSet<Accession> set = new HashSet<Accession>();
		for(Accession a: allAccessionIds) {
			if(a.getLogicaldb().get_logicaldb_key() == db_key) {
				set.add(a);
			}
		}
		return set;
	}

}
