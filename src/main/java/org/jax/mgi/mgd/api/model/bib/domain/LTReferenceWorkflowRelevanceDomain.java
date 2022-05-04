package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowRelevance;

/* Is: a domain object that represents data for a single workflow group relevance setting via the API
 * Has: data relating to a single workflow relevance status change for a single reference
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class LTReferenceWorkflowRelevanceDomain extends BaseDomain {

	public String refsKey;
	public boolean isCurrent;
	public String creation_date;
	public String modification_date;
	public String relevance;
	public String relevance_abbreviation;
	public String confidence;
	public String version;
	public String createdby;
	public String modifiedby;
	
	public LTReferenceWorkflowRelevanceDomain() {}

	public LTReferenceWorkflowRelevanceDomain(LTReferenceWorkflowRelevance rws) {
		this.refsKey = rws.getRefsKey();
		this.creation_date = rws.getCreationDate();
		this.modification_date = rws.getModificationDate();
		this.createdby = rws.getCreatedBy();
		this.modifiedby = this.createdby;
		this.relevance = rws.getRelevance();
		this.version = rws.getVersion();
		this.relevance_abbreviation = rws.getRelevanceAbbreviation();

		if (rws.getConfidence() != null) {
			this.confidence = rws.getConfidence().toString();
		}

		this.isCurrent = true;
		if (rws.getIsCurrent() == 0) {
			this.isCurrent = false;
		}
	}
}
