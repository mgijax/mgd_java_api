package org.jax.mgi.mgd.api.domain;

import java.util.List;

/* Is: a domain object that represents data for a bulk update of references via the API
 * Has: a list of database keys for the references to be modified and a workflow tag to apply to them
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class ReferenceBulkDomain extends DomainBase {
	public List<Long> _refs_keys;
	public String workflow_tag;
	public String workflow_tag_operation;	// "add" or "remove"; "add" is the default
}
