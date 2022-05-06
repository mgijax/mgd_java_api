package org.jax.mgi.mgd.api.model.bib.repository;

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
		log.info("LTReferenceSummaryRepository/get()");
		return translator.translate(getReference(key));
	}

	@Override
	public SearchResults<LTReferenceSummaryDomain> search(Map<String,Object> params) {
		return null;
	}

	@Override
	public LTReferenceSummaryDomain update(LTReferenceSummaryDomain domain, User user) throws APIException {
		throw new APIException("Cannot update using a ReferenceSummaryDomain");
	}

	/***--- (private) instance methods ---***/
	
	/* retrieve the Reference object with the given primaryKey
	 */
	private LTReference getReference(String primaryKey) throws APIException {
		log.info("LTReferenceSummaryRepository/getReference()");
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
