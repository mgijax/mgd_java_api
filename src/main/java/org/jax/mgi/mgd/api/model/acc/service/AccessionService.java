package org.jax.mgi.mgd.api.model.acc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.AccessionDAO;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AccessionService extends BaseService<AccessionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AccessionDAO accessionDAO;

	private AccessionTranslator translator = new AccessionTranslator();
	private SlimAccessionTranslator slimtranslator = new SlimAccessionTranslator();
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
		// get the DAO/entity and translate -> domain
		AccessionDomain domain = new AccessionDomain();
		if (accessionDAO.get(key) != null) {
			domain = translator.translate(accessionDAO.get(key),1);
		}
		return domain;		
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
	public List<AccessionDomain> search(AccessionDomain searchDomain) {
		// search for accession by id ; more can be added later
		// returns empty result items if vocabulary does not exist
		// returns AccessionDomain results if vocabulary does exist
			
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "select _accession_key, accID"
				+ "\nfrom acc_accession"
				+ "\nwhere accID = '" + searchDomain.getAccID() + "'"
				+ "\norder by accID";	
		log.info(cmd);		

		try {
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {							
				AccessionDomain domain = new AccessionDomain();						
				domain = translator.translate(accessionDAO.get(rs.getInt("_accession_key")),1);
				accessionDAO.clear();	
				results.add(domain);
			}
			
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	// validate
	
	@Transactional	
	public List<SlimAccessionDomain> validIsDuplicate(String key, String accid, String logicaldbKey, String mgiTypeKey) {
		// use SlimAccessionDomain to return list of validated accession id
		// one value is expected
		// expects full accID (prefixPart + numericPart)
		// returns empty list if value contains "%"
		// returns empty list if value does not exist

		List<SlimAccessionDomain> results = new ArrayList<SlimAccessionDomain>();
		
		if (accid.contains("%")) {
			return results;
		}

		String cmd = "\nselect * from acc_accession"
				+ "\nwhere _object_key = " + key		
				+ "\nand _logicaldb_key = " + logicaldbKey
				+ "\nand _mgitype_key = " + mgiTypeKey
				+ "\nand lower(accid) = '" + accid.toLowerCase() + "'";
			
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimAccessionDomain domain = new SlimAccessionDomain();	
				domain = slimtranslator.translate(accessionDAO.get(rs.getInt("_accession_key")),1);
				accessionDAO.clear();									
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
		
	//
	// get list of accession id domains by using sqlExecutor
	//
	
	@Transactional
	private List<AccessionDomain> getAccessionDomainList(String cmd) {
		// execute accession cmd and return list of accession domains
		// assumes the certain parameters are returned from cmd are return
		
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AccessionDomain domain = new AccessionDomain();
				domain = translator.translate(accessionDAO.get(rs.getInt("_accession_key")),1);
				accessionDAO.clear();					
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@Transactional
	public List<AccessionDomain> getMarkerEditAccessionIds(Integer key) {
		// gets marker accession ids : edit list
		// see mrk_accref1_view for details
		// returns list of accession domain

		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		String cmd = "select * from mrk_accref1_view "
				+ "\nwhere _object_key = " + key
				+ "\norder by _accession_key";
		log.info(cmd);
		
		try {
			results = getAccessionDomainList(cmd);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@Transactional
	public List<AccessionDomain> getMarkerNonEditAccessionIds(Integer key) {
		// gets marker accession ids : non-edit list
		// see mrk_accref2_view for details
		// returns list of accession domain
		
		// list of results to be returned
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		String cmd = "select * from mrk_accref2_view "
				+ "\nwhere _object_key = " + key
				+ "\norder by _accession_key";
		log.info(cmd);
		
		try {
			results = getAccessionDomainList(cmd);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	//
	// process accession ids : create, delete, update
	//
	
	@Transactional
	public Boolean process(String parentKey, List<AccessionDomain> domain, String mgiTypeName, User user) {
		// process accession associations (create, delete, update)
		// using stored procedure methods (ACC_insert(), ACC_delete_byAccKey(), ACC_update())
		// using entity to compare domain vs entity
		// but not using entity to handle actual create/delete/update processing
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAccession/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				// minimum domain info for create:
				// processStatus (‘c’ for create) , logicaldbKey, mgitypekey, objectKey, accid

				log.info("processAccession create");
				
				// refsKey can be null; set to -1
				String refsKey = "-1";
				if (domain.get(i).getReferences() != null) {
				    refsKey = 	domain.get(i).getReferences().get(0).getRefsKey();
				}
				System.out.println("AccessionService refsKey " + refsKey);
				cmd = "select count(*) from ACC_insert ("
							+ user.get_user_key().intValue()
							//+ "," + domain.get(i).getObjectKey()
							+ "," + parentKey
							+ ",'" + domain.get(i).getAccID() + "'"
							+ "," + domain.get(i).getLogicaldbKey()
							+ ",'" + mgiTypeName + "'"
							+ "," + refsKey + ","
							+ "1,0,1)";
				log.info("cmd: " + cmd);
				Query query = accessionDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAccession delete");
				cmd = "select count(*) from ACC_delete_byAccKey ("
						+ domain.get(i).getAccessionKey()
						+ ")";
				log.info("cmd: " + cmd);
				Query query = accessionDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
				log.info("processAccession delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAccession update");

				Boolean isUpdated = false;
				Accession entity = accessionDAO.get(Integer.valueOf(domain.get(i).getAccessionKey()));
		
				if (!entity.getAccID().equals(domain.get(i).getAccID())) {
					log.info("AccessionService: accid not the same");
					isUpdated = true;
				}
				
				// Our assumption is that if the domain OR entity has no AccessionReferences
				// than it is not supposed to have one - and it is the UI responsibility to
				// pass in appropriate values. e.g. Markers accIDs have AccessionReferences, but VariantSequence 
				// accIDs do not. 
				// What we do here is only compare IFF both domain and entity have a value.
				if ((domain.get(i).getReferences() == null || domain.get(i).getReferences().get(0).getRefsKey().isEmpty()) 
						|| entity.getReferences() == null) {
					log.info("AccessionService: domain is null or empty or entity is null");
					continue;
				}
				// if not entity/null and not domain/empty, then check if equivalent
				else { 
					if (entity.getReferences().get(0).getReference().get_refs_key() != Integer.parseInt(domain.get(i).getReferences().get(0).getRefsKey())) {
						log.info("AccessionService: references not the same");
						isUpdated = true;
					}
				}
				
				if (isUpdated) {
					log.info("AccessionService: is modified " );
					cmd = "select count(*) from ACC_update ("
							+ user.get_user_key().intValue()
							+ "," + domain.get(i).getAccessionKey()
							+ ",'" + domain.get(i).getAccID() + "'"
							+ "," + domain.get(i).getObjectKey()
							+ "," + entity.getReferences().get(0).getReference().get_refs_key()
							+ "," + domain.get(i).getReferences().get(0).getRefsKey()
							+ ")";
					log.info("cmd: " + cmd);
					Query query = accessionDAO.createNativeQuery(cmd);
					query.getResultList();
					modified = true;
					log.info("processAccession/changes processed: " + domain.get(i).getAccessionKey());
				}
				else {
					log.info("processAccession/no changes processed: " + domain.get(i).getAccessionKey());
				}
			}
			else {
				log.info("processAccession/no changes processed: " + domain.get(i).getAccessionKey());
			}
		}
		
		log.info("processAccession/processing successful");
		return modified;
	}
		
}
