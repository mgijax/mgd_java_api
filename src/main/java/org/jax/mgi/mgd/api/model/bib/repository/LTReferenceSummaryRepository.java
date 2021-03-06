package org.jax.mgi.mgd.api.model.bib.repository;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseRepository;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.translator.LTReferenceSummaryTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

/* Is: a repository that deals with searching for ReferenceSummaryDomain objects
 * Has: one or more DAOs to facilitate storage/retrieval of the entities from which the
 *    ReferenceSummaryDomain object has its data drawn
 * Does: (from the outside, this appears to) retrieve domain objects, store them, search for them
 */
public class LTReferenceSummaryRepository extends BaseRepository<LTReferenceSummaryDomain> {

	/***--- instance variables ---***/

	@Inject
	private LTReferenceDAO referenceDAO;

	LTReferenceSummaryTranslator translator = new LTReferenceSummaryTranslator();

	/***--- (public) instance methods ---***/

	@Override
	public LTReferenceSummaryDomain get(String key) throws APIException {
		return translator.translate(getReference(key));
	}

	@Override
	public SearchResults<LTReferenceSummaryDomain> search(Map<String,Object> params) {
		SearchResults<LTReference> refs = referenceDAO.search(params);
		SearchResults<LTReferenceSummaryDomain> domains = new SearchResults<LTReferenceSummaryDomain>();

		domains.elapsed_ms = refs.elapsed_ms;
		domains.error = refs.error;
		domains.message = refs.message;
		domains.status_code = refs.status_code;
		domains.total_count = refs.total_count;
		domains.all_match_count = refs.all_match_count;

		if (refs.items != null) {
			// walking the references to do the translations individually, because I want a List,
			// not an Iterable
			
			domains.items = new ArrayList<LTReferenceSummaryDomain>();
			for (LTReference ref : refs.items) {
				domains.items.add(translator.translate(ref));
			}
		}
		return domains;
	}

	@Override
	public LTReferenceSummaryDomain update(LTReferenceSummaryDomain domain, User user) throws APIException {
		throw new APIException("Cannot update using a ReferenceSummaryDomain");
	}

	/***--- (private) instance methods ---***/
	
	/* retrieve the Reference object with the given primaryKey
	 */
	private LTReference getReference(String primaryKey) throws APIException {
		if (primaryKey == null) {
			throw new APIException("ReferenceRepository.getReference() : reference key is null");
		}
		LTReference reference = referenceDAO.find(Integer.valueOf(primaryKey));
		if (reference == null) {
			throw new APIException("ReferenceRepository.getReference(): Unknown reference key: " + primaryKey);
		}
		return reference;
	}
}
