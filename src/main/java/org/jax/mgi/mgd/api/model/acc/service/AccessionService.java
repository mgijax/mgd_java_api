package org.jax.mgi.mgd.api.model.acc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.AccessionDAO;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionReferenceDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.search.AccessionSearchForm;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AccessionService extends BaseService<AccessionDomain> implements BaseSearchInterface<AccessionDomain, AccessionSearchForm> {

	protected static Logger log = Logger.getLogger(AccessionService.class);

	@Inject
	private AccessionDAO accessionDAO;

	private AccessionTranslator translator = new AccessionTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<AccessionDomain> create(AccessionDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AccessionDomain> update(AccessionDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AccessionDomain get(Integer key) {
		return translator.translate(accessionDAO.get(key));
	}

    @Transactional
    public SearchResults<AccessionDomain> getResults(Integer key) {
        SearchResults<AccessionDomain> results = new SearchResults<AccessionDomain>();
        results.setItem(translator.translate(accessionDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<AccessionDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AccessionDomain> search(AccessionSearchForm searchForm) {
		SearchResults<Accession> accessions;
		
		if(searchForm.getOrderBy() != null) {
			accessions = accessionDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			accessions = accessionDAO.search(searchForm.getSearchFields());
		}
		
		Iterable<AccessionDomain> newItems = translator.translateEntities(accessions.items, searchForm.getSearchDepth());
		return new SearchResults<AccessionDomain>(newItems);
	}
	
	public List<AccessionDomain> markerNucleotideAccessionIds(Integer key) {

		// list of results to be returned
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		String cmd = "select * from mrk_accref1_view "
				+ "\nwhere _logicaldb_key in (9) and _object_key = " + key
				+ "\norder by _accession_key";
		log.info(cmd);
		
		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				AccessionDomain domain = new AccessionDomain();
				domain.setAccessionKey(rs.getString("_accession_key"));
				domain.setLogicaldbKey(rs.getString("_logicaldb_key"));
				domain.setLogicaldb(rs.getString("logicaldb"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setAccID(rs.getString("accid"));
				domain.setPrefixPart(rs.getString("prefixpart"));
				domain.setNumericPart(rs.getString("numericpart"));
				domain.setIsPrivate(rs.getString("private"));
				domain.setPreferred(rs.getString("preferred"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdby"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedby"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
				
				// attach reference to domain
				AccessionReferenceDomain refDomain = new AccessionReferenceDomain();
				List<AccessionReferenceDomain> references = new ArrayList<AccessionReferenceDomain>();
				refDomain.setAccessionKey(rs.getString("_accession_key"));
				refDomain.setRefKey(rs.getString("_refs_key"));
				refDomain.setJnumid(rs.getString("jnumid"));
				refDomain.setJnum(rs.getString("jnum"));
				refDomain.setShort_citation(rs.getString("short_citation"));
				// using acc_accession create/modify information
				refDomain.setCreatedByKey(rs.getString("_createdby_key"));
				refDomain.setCreatedBy(rs.getString("createdby"));
				refDomain.setModifiedByKey(rs.getString("_modifiedby_key"));
				refDomain.setModifiedBy(rs.getString("modifiedby"));
				refDomain.setCreation_date(rs.getString("creation_date"));
				refDomain.setModification_date(rs.getString("modification_date"));				
				references.add(refDomain);
				domain.setReferences(references);
				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
	
	@Transactional
	public static void processAccession(String parentKey, List<AccessionDomain> domain, AccessionDAO accessionDAO, ReferenceDAO referenceDAO, String mgiTypeKey, String logicaldbKey, User user) {
		// process accession associations (create, delete, update)
		// using stored procedure methods (ACC_insertNoChecks(), ACC_delete_byAccKey(), ACC_update())
		// using entity to compare domain vs entity
		// but not using entity to handle actual create/delete/update processing
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAccession/nothing to process");
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getAccessionKey() == null 
					|| domain.get(i).getAccessionKey().isEmpty()) {
				
				log.info("processAccession create");

				cmd = "select count(*) from ACC_insertNoChecks ("
							+ user.get_user_key().intValue()
							+ "," + domain.get(i).getObjectKey()
							+ ",'" + domain.get(i).getAccID() + "'"
							+ "," + logicaldbKey
							+ "," + mgiTypeKey
							+ "," + domain.get(i).getReferences().get(0).getRefKey()
							+ ")";
				log.info("cmd: " + cmd);
				Query query = accessionDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getAccessionKey() == null || domain.get(i).getAccessionKey().isEmpty()) {
				log.info("processAccession delete");
				cmd = "select count(*) from ACC_delete_byAccKey ("
						+ domain.get(i).getAccessionKey()
						+ ")";
				log.info("cmd: " + cmd);
				Query query = accessionDAO.createNativeQuery(cmd);
				query.getResultList();
				log.info("processAccession delete successful");
			}
			else {
				log.info("processAccession update");

				Boolean modified = false;
				Accession entity = accessionDAO.get(Integer.valueOf(domain.get(i).getAccessionKey()));
		
				if (!entity.getAccID().equals(domain.get(i).getAccID())) {
					modified = true;
				}
				
				// reference can be null
				// may be null coming from entity
				if (entity.getReferences() == null) {
					if (!domain.get(i).getReferences().get(0).getRefKey().isEmpty()) {
						modified = true;
					}
				}
				// may be empty coming from domain
				else if (domain.get(i).getReferences().get(0).getRefKey().isEmpty()) {
					modified = true;
				}
				// if not entity/null and not domain/empty, then check if equivalent
				else if (!entity.getReferences().get(0).getReference().get_refs_key().equals(Integer.valueOf(domain.get(i).getReferences().get(0).getRefKey()))) {
					modified = true;
				}
				
				if (modified == true) {
					cmd = "select count(*) from ACC_update ("
							+ user.get_user_key().intValue()
							+ "," + domain.get(i).getAccessionKey()
							+ ",'" + domain.get(i).getAccID() + "'"
							+ "," + domain.get(i).getObjectKey()
							+ "," + entity.getReferences().get(0).getReference().get_refs_key()
							+ "," + domain.get(i).getReferences().get(0).getRefKey()
							+ ")";
					log.info("cmd: " + cmd);
					Query query = accessionDAO.createNativeQuery(cmd);
					query.getResultList();
					log.info("processAccession/changes processed: " + domain.get(i).getAccessionKey());
				}
				else {
					log.info("processAccession/no changes processed: " + domain.get(i).getAccessionKey());
				}
			}
		}
		
		log.info("processAccession/processing successful");
		return;
	}
	
}
