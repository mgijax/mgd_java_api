package org.jax.mgi.mgd.api.model;

import org.jboss.logging.Logger;

/* Is: a repository that deals with domain objects and handles their translation to entity objects
 *    for storage to and retrieval from the database
 * Has: one or more DAOs to facilitate storage/retrieval of the entities from which the domain
 *    object has its data drawn
 * Does: (from the outside, this appears to) retrieve domain objects, store them, search for them
 * Notes: 1. This is basically a shim layer, sitting over the top of the DAOs.
 *    2. The modification methods (create, update, delete) persist changes to the database.
 */

public abstract class BaseRepository<D extends BaseDomain> {
	
	/***--- instance variables ---***/
	
	protected Logger log = Logger.getLogger(getClass());
		
}
