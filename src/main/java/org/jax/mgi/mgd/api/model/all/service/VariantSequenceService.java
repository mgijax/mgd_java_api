package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.VariantSequenceDAO;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.translator.VariantSequenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VariantSequenceService extends BaseService<VariantSequenceDomain> {

	protected Logger log = Logger.getLogger(VariantSequenceService.class);

	@Inject
	private VariantSequenceDAO variantSequenceDAO;
	
	// translate an entity to a domain to return in the results
	private VariantSequenceTranslator translator = new VariantSequenceTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VariantSequenceDomain> create(VariantSequenceDomain domain, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<VariantSequenceDomain> update(VariantSequenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public VariantSequenceDomain get(Integer key) {
		return translator.translate(variantSequenceDAO.get(key),1);
	}

    @Transactional
    public SearchResults<VariantSequenceDomain> getResults(Integer key) {
        SearchResults<VariantSequenceDomain> results = new SearchResults<VariantSequenceDomain>();
        results.setItem(translator.translate(variantSequenceDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<VariantSequenceDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VariantSequenceDomain> search(VariantSequenceDomain searchDomain) {

		List<VariantSequenceDomain> results = new ArrayList<VariantSequenceDomain>();

		// building SQL command : select + from + where + orderBy
		String cmd = "";		
		String select = "select vs.*, t1.term, u1.login as createdBy, u2.login as modifiedBy";
		String from = "from all_variant_sequence vs, voc_term t1, mgi_user u1, mgi_user u2";
		String where = "where vs._sequence_type_key = t1._term_key"
					+ "\nand vs._createdby_key = u1._user_key"
					+ "\nand vs._modifiedby_key = u2._user_key";
		String orderBy = "order by t1.term";
		String limit = "LIMIT 1000";

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("vs", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getVariantSequenceKey() != null && !searchDomain.getVariantSequenceKey().isEmpty()) {
			where = where + "\nand vs._variantsequence_key = " + searchDomain.getVariantSequenceKey();
		}
		if (searchDomain.getVariantKey() != null && !searchDomain.getVariantKey().isEmpty()) {
			where = where + "\nand vs._variant_key = " + searchDomain.getVariantKey();
		}		
		if (searchDomain.getSequenceTypeKey() != null && !searchDomain.getSequenceTypeKey().isEmpty()) {
			where = where + "\nand vs._sequence_type_key = " + searchDomain.getSequenceTypeKey();
		}
		if (searchDomain.getStartCoordinate() != null && !searchDomain.getStartCoordinate().isEmpty()) {
			where = where + "\nand vs.startCoordinate = " + searchDomain.getStartCoordinate();
		}
		if (searchDomain.getEndCoordinate() != null && !searchDomain.getEndCoordinate().isEmpty()) {
			where = where + "\nand vs.endCoordinate = " + searchDomain.getEndCoordinate();
		}
		if (searchDomain.getReferenceSequence() != null && !searchDomain.getReferenceSequence().isEmpty()) {
			where = where + "\nand vs.referenceSequence = '" + searchDomain.getReferenceSequence() + "'";
		}
		if (searchDomain.getVariantSequence() != null && !searchDomain.getVariantSequence().isEmpty()) {
			where = where + "\nand vs.variantSequence = '" + searchDomain.getVariantSequence() + "'";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				VariantSequenceDomain domain = new VariantSequenceDomain();
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setVariantSequenceKey(rs.getString("_variantsequence_key"));			
				domain.setVariantKey(rs.getString("_variant_key"));
				domain.setSequenceTypeKey(rs.getString("_sequence_type_key"));
				domain.setSequenceTypeTerm(rs.getString("term"));
			    domain.setStartCoordinate(rs.getString("startCoordinate"));
			    domain.setEndCoordinate(rs.getString("endCoordinate"));
				domain.setReferenceSequence(rs.getString("referenceSequence"));
				domain.setVariantSequence(rs.getString("variantSequence"));	
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdBy"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedBy"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
