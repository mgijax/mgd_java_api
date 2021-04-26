package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowRelevance;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowStatus;

/* Is: a domain object that represents data for a single workflow group relevance setting via the API
 * Has: data relating to a single workflow relevance status change for a single reference
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class LTReferenceWorkflowRelevanceDomain extends BaseDomain {
	public String refsKey;
	public boolean is_current;
	public String creation_date;
	public String modification_date;
	public String relevance;
	public String relevance_abbreviation;
	public String confidence;
	public String createdby_user;
	public String modifiedby_user;
	
	public LTReferenceWorkflowRelevanceDomain() {}

	public LTReferenceWorkflowRelevanceDomain(LTReferenceWorkflowRelevance rws) {
		this.refsKey = rws.getRefsKey();
		this.creation_date = rws.getCreationDate();
		this.modification_date = rws.getModificationDate();
		this.createdby_user = rws.getCreatedByUser().getLogin();
		this.modifiedby_user = this.createdby_user;
		this.relevance = rws.getRelevance();
		this.relevance_abbreviation = rws.getRelevanceAbbreviation();

		if (rws.getConfidence() != null) {
			this.confidence = rws.getConfidence().toString();
		}

		this.is_current = true;
		if (rws.getIsCurrent() == 0) {
			this.is_current = false;
		}
	}
}
