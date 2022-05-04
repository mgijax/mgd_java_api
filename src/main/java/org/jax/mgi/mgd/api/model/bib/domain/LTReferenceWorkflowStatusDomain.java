package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowStatus;

/* Is: a domain object that represents data for a single workflow group status change via the API
 * Has: data relating to a single workflow group status change for a single reference
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class LTReferenceWorkflowStatusDomain extends BaseDomain {

	public String refsKey;
	public boolean isCurrent;
	public String group;
	public String group_abbreviation;
	public String status;
	public String createdBy;
	public String modifiedBy;
	public String creation_date;
	public String modification_date;
	
	public LTReferenceWorkflowStatusDomain() {}

	public LTReferenceWorkflowStatusDomain(LTReferenceWorkflowStatus rws) {
		this.refsKey = rws.getRefsKey();
		this.group = rws.getGroup();
		this.group_abbreviation = rws.getGroupAbbreviation();
		this.status = rws.getStatus();
		this.creation_date = rws.getCreationDate();
		this.modification_date = rws.getModificationDate();
		this.createdBy = rws.getCreatedBy();
		this.modifiedBy = rws.getModifiedBy();
		
		this.isCurrent = true;
		if (rws.getIsCurrent() == 0) {
			this.isCurrent = false;
		}
	}
}
