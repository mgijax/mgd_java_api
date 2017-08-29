package org.jax.mgi.mgd.api.domain;

import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;

/* Is: a domain object that represents data for a single workflow group status change via the API
 * Has: data relating to a single workflow group status change for a single reference
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class ReferenceWorkflowStatusDomain extends DomainBase {
	public Long _refs_key;
	public boolean is_current;
	public String creation_date;
	public String modification_date;
	public String group;
	public String group_abbreviation;
	public String status;
	public String createdby_user;
	public String modifiedby_user;
	
	public ReferenceWorkflowStatusDomain(ReferenceWorkflowStatus rws) {
		this._refs_key = rws._refs_key;
		this.creation_date = rws.getCreationDate();
		this.modification_date = rws.getModificationDate();
		this.createdby_user = rws.createdByUser.getLogin();
		this.modifiedby_user = rws.createdByUser.getLogin();
		this.group = rws.getGroup();
		this.group_abbreviation = rws.getGroupAbbreviation();
		this.status = rws.getStatus();
		this.is_current = true;
		if (rws.isCurrent == 0) {
			this.is_current = false;
		}
	}
}
