package org.jax.mgi.mgd.api.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends Base {

	@Id
	@Column(name="_Refs_key")
	public Long _refs_key;
	
	@Column(name="authors")
	public String authors;

	@Column(name="_primary")
	public String primaryAuthor;

	@Column(name="title")
	public String title;

	@Column(name="journal")
	public String journal;
	
	@Column(name="vol")
	public String volume;

	@Column(name="issue")
	public String issue;
	
	@Column(name="date")
	public String date;
	
	@Column(name="year")
	public String year;

	@Column(name="pgs")
	public String pages;
	
	@Column(name="abstract")
	public String refAbstract;		// just "abstract" is a Java reserved word, so need a prefix
	
	@Column(name="isReviewArticle")
	public int isReviewArticle;

	@OneToMany (targetEntity=AccessionID.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key")
	@Where(clause="_mgitype_key = 1")
	@OrderBy("_logicaldb_key, preferred desc")
	public List<AccessionID> accessionIDs;

	public List<AccessionID> getAccessionIDs() {
		return this.accessionIDs;
	}

	public void setAccessionIDs(List<AccessionID> accessionIDs) {
		this.accessionIDs = accessionIDs;
	}
	
	@Transient
	public String getJnum() {
		for (AccessionID accID : this.getAccessionIDs()) {
			if ((accID._logicaldb_key == 1) && "J:".equals(accID.prefixPart) && (accID.preferred == 1)) {
				return accID.accID;
			}
		}
		return null;
	}
}
