package org.jax.mgi.mgd.api.domain;

import java.util.List;

/* Is: a domain object that represents a single logged API event in mgd.
 * Has: fields needed to display/edit in the PWI, where those values for those fields are carried
 * 	back to the API to be put into entity objects and persisted to the database
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class ApiLogDomain extends DomainBase {
	public Integer _event_key;
	public String username;
	public String creation_date;
	public String endpoint;
	public String parameters;
	public String mgitype;
	public List<Integer> objectKeys;
}
