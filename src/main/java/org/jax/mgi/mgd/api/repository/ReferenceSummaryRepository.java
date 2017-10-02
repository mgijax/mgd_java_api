package org.jax.mgi.mgd.api.repository;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.domain.ReferenceSummaryDomain;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.translators.ReferenceSummaryTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

/* Is: a repository that deals with searching for ReferenceSummaryDomain objects
 * Has: one or more DAOs to facilitate storage/retrieval of the entities from which the
 *    ReferenceSummaryDomain object has its data drawn
 * Does: (from the outside, this appears to) retrieve domain objects, store them, search for them
 */
public class ReferenceSummaryRepository extends Repository<ReferenceSummaryDomain> {

	/***--- instance variables ---***/

	@Inject
	private ReferenceDAO referenceDAO;

	ReferenceSummaryTranslator translator = new ReferenceSummaryTranslator();

	/***--- (public) instance methods ---***/

	@Override
	public ReferenceSummaryDomain get(int primaryKey) throws APIException {
		return translator.translate(getReference(primaryKey));
	}

	@Override
	public SearchResults<ReferenceSummaryDomain> search(Map<String,Object> params) {
		SearchResults<Reference> refs = referenceDAO.search(params);
		SearchResults<ReferenceSummaryDomain> domains = new SearchResults<ReferenceSummaryDomain>();

		domains.elapsed_ms = refs.elapsed_ms;
		domains.error = refs.error;
		domains.message = refs.message;
		domains.status_code = refs.status_code;
		domains.total_count = refs.total_count;
		domains.all_match_count = refs.all_match_count;

		if (refs.items != null) {
			// walking the references to do the translations individually, because I want a List,
			// not an Iterable
			
			domains.items = new ArrayList<ReferenceSummaryDomain>();
			for (Reference ref : refs.items) {
				domains.items.add(translator.translate(ref));
			}
		}
		return domains;
	}

	@Override
	public ReferenceSummaryDomain update(ReferenceSummaryDomain domain, User user) throws APIException {
		throw new APIException("Cannot update using a ReferenceSummaryDomain");
	}

	@Override
	public ReferenceSummaryDomain delete(ReferenceSummaryDomain domain, User user) throws APIException {
		throw new APIException("Cannot delete using a ReferenceSummaryDomain");
	}

	@Override
	public ReferenceSummaryDomain create(ReferenceSummaryDomain domain, User username) throws APIException {
		throw new APIException("Cannot create using a ReferenceSummaryDomain");
	}

	/***--- (private) instance methods ---***/
	
	/* retrieve the Reference object with the given primaryKey
	 */
	private Reference getReference(Integer primaryKey) throws APIException {
		if (primaryKey == null) {
			throw new APIException("ReferenceRepository.getReference() : reference key is null");
		}
		Reference reference = referenceDAO.find(primaryKey);
		if (reference == null) {
			throw new APIException("ReferenceRepository.getReference(): Unknown reference key: " + primaryKey);
		}
		return reference;
	}
}
