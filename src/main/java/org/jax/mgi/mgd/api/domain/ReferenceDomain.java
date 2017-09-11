package org.jax.mgi.mgd.api.domain;

import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceAssociatedData;
import org.jax.mgi.mgd.api.entities.ReferenceBook;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowData;

/* Is: a domain object that represents a single reference in mgd.
 * Has: fields needed to display/edit in the PWI, where those values for those fields are carried
 * 	back to the GXDI to be put into entity objects and persisted to the database
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class ReferenceDomain extends DomainBase {
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
	public String has_pdf;
	public String has_supplemental;
	public String link_to_supplemental;
	public String has_extracted_text;
	public List<String> associated_data;
	
	/***--- constructors ---***/
	
	/* empty constructor - ready for population from JSON */
	public ReferenceDomain() {}
	
	/* pull data from the Reference passed in, using it to populate this domain object for transfer to client
	 */
	public ReferenceDomain(Reference r) {
		this._refs_key = r.get_refs_key();
		this.authors = r.getAuthors();
		this.primary_author = r.getPrimary_author();
		this.title = r.getTitle();
		this.journal = r.getJournal();
		this.volume = r.getVolume();
		this.issue = r.getIssue();
		this.date = r.getDate();
		this.year = r.getYear();
		this.pages = r.getPages();
		if (r.getIsReviewArticle() == 0) {
			this.isReviewArticle = "No";
		} else {
			this.isReviewArticle = "Yes";
		}
		this.ref_abstract = r.getRef_abstract();
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
		if (r.getIs_discard() == 0) {
			this.is_discard = "No";
		} else {
			this.is_discard = "Yes";
		}
		
		this.associated_data = new ArrayList<String>();
		ReferenceAssociatedData flags = r.getAssociatedData();
		if (flags != null) {
			if (flags.getHas_gxdindex() != 0) { this.associated_data.add("GXD Index"); }
			if (flags.getHas_gxdimages() != 0) { this.associated_data.add("GXD/CRE Images"); }
			if (flags.getHas_gxdspecimens() != 0) { this.associated_data.add("GXD/CRE Specimens"); }
			if (flags.getHas_probes() != 0) { this.associated_data.add("Probes"); }
			if (flags.getHas_antibodies() != 0) { this.associated_data.add("Antibodies"); }
			if (flags.getHas_gxdresults() != 0) { this.associated_data.add("GXD/CRE Results"); }
			if (flags.getHas_gxdresults() != 0) { this.associated_data.add("GXD/CRE Assays"); }
			if (flags.getHas_alleles() != 0) { this.associated_data.add("Alleles"); }
			if (flags.getHas_markers() != 0) { this.associated_data.add("Markers"); }
		}
		
		ReferenceBook bookData = r.getBookData();
		if (bookData != null) {
			this.book_author = bookData.getBook_author();
			this.book_title = bookData.getBook_title();
			this.place = bookData.getPlace();
			this.publisher = bookData.getPublisher();
			this.series_edition = bookData.getSeries_edition();
		}
		
		ReferenceWorkflowData workflowData = r.getWorkflowData();
		if (workflowData != null) {
			this.has_supplemental = workflowData.getSupplemental();
			this.link_to_supplemental = workflowData.getLink_supplemental();
			if (workflowData.getHas_pdf() == 0) {
				this.has_pdf = "No";
			} else {
				this.has_pdf = "Yes";
			}
			if ((workflowData.getExtracted_text() != null) && (workflowData.getExtracted_text().length() > 0)) {
				this.has_extracted_text = "Yes";
			} else {
				this.has_extracted_text = "No";
			}
		}
	}
}
