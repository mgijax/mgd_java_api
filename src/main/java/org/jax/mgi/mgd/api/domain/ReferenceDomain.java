package org.jax.mgi.mgd.api.domain;

import java.util.List;

import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceBook;
import org.jboss.logging.Logger;

/* Is: a domain object that represents a single reference in mgd.
 * Has: fields needed to display/edit in the PWI, where those values for those fields are carried
 * 	back to the GXDI to be put into entity objects and persisted to the database
 * Does: serves as a data-transfer object between the GXDI and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class ReferenceDomain {
	private Logger log = Logger.getLogger(getClass());

	public Long _refs_key;
	public String authors;
	public String primary_author;
	public String title;
	public String journal;
	public String volume;
	public String issue;
	public String date;
	public Integer year;
	public String pages;
	public String isReviewArticle;
	public String jnumid;
	public String doiid;
	public String pubmedid;
	public String mgiid;
	public String gorefid;
	public String reference_type;
	public String ref_abstract;
	public String referencenote;
	public String short_citation;
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String qtl_status;
	public String tumor_status;
	public String is_discard;
	public List<String> workflow_tags;
	public String book_author;
	public String book_title;
	public String place;
	public String publisher;
	public String series_edition;
	
	/***--- constructors ---***/
	
	/* empty constructor - ready for population from JSON */
	public ReferenceDomain() {}
	
	/* pull data from the Reference passed in, using it to populate this domain object for transfer to client
	 */
	public ReferenceDomain(Reference r) {
		this._refs_key = r._refs_key;
		this.authors = r.authors;
		this.primary_author = r.primary_author;
		this.title = r.title;
		this.journal = r.journal;
		this.volume = r.volume;
		this.issue = r.issue;
		this.date = r.date;
		this.year = r.year;
		this.pages = r.pages;
		if (r.isReviewArticle == 0) {
			this.isReviewArticle = "No";
		} else {
			this.isReviewArticle = "Yes";
		}
		this.ref_abstract = r.ref_abstract;
		this.referencenote = r.getReferencenote();
		this.jnumid = r.getJnumid();
		this.doiid = r.getDoiid();
		this.pubmedid = r.getPubmedid();
		this.mgiid = r.getMgiid();
		this.gorefid = r.getGorefid();
		this.reference_type = r.getReferenceType();
		this.short_citation = r.getShort_citation();
		this.ap_status = r.getAp_status();
		this.go_status = r.getGo_status();
		this.gxd_status = r.getGxd_status();
		this.qtl_status = r.getQtl_status();
		this.tumor_status = r.getTumor_status();
		this.workflow_tags = r.getWorkflowTags();
		if (r.is_discard == 0) {
			this.is_discard = "No";
		} else {
			this.is_discard = "Yes";
		}
		
		ReferenceBook bookData = r.getBookData();
		if (bookData != null) {
			this.book_author = bookData.book_author;
			this.book_title = bookData.book_title;
			this.place = bookData.place;
			this.publisher = bookData.publisher;
			this.series_edition = bookData.series_edition;
		}
	}
}
