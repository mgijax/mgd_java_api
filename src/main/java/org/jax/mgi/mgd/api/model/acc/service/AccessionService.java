package org.jax.mgi.mgd.api.model.acc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.AccessionDAO;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class AccessionService extends BaseService<AccessionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AccessionDAO accessionDAO;

	private AccessionTranslator translator = new AccessionTranslator();
	private SlimAccessionTranslator slimtranslator = new SlimAccessionTranslator();

	@Transactional
	public SearchResults<AccessionDomain> create(AccessionDomain object, User user) {
		SearchResults<AccessionDomain> results = new SearchResults<AccessionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AccessionDomain> update(AccessionDomain object, User user) {
		SearchResults<AccessionDomain> results = new SearchResults<AccessionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AccessionDomain> delete(Integer key, User user) {
		SearchResults<AccessionDomain> results = new SearchResults<AccessionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AccessionDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AccessionDomain domain = new AccessionDomain();
		if (accessionDAO.get(key) != null) {
			domain = translator.translate(accessionDAO.get(key));
		}
		accessionDAO.clear();
		return domain;		
	}

    @Transactional
    public SearchResults<AccessionDomain> getResults(Integer key) {
        SearchResults<AccessionDomain> results = new SearchResults<AccessionDomain>();
        results.setItem(translator.translate(accessionDAO.get(key)));
        accessionDAO.clear();
        return results;
    }

	@Transactional
	public List<AccessionDomain> search(AccessionDomain searchDomain) {
		// search for accession by id ; more can be added later
		// returns empty result items if vocabulary does not exist
		// returns AccessionDomain results if vocabulary does exist
			
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		// building SQL command : select + from + where + orderBy
		String cmd = "select _accession_key, accID"
				+ "\nfrom acc_accession"
				+ "\nwhere accID = '" + searchDomain.getAccID() + "'"
				+ "\norder by accID";	
		log.info(cmd);		

		try {
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {							
				AccessionDomain domain = new AccessionDomain();						
				domain = translator.translate(accessionDAO.get(rs.getInt("_accession_key")));
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
				domain = slimtranslator.translate(accessionDAO.get(rs.getInt("_accession_key")));
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
	public List<SlimAccessionDomain> validStrainAccessionId(SlimAccessionDomain domain) {
		// use SlimAccessionDomain to return list of validated strain accession id for logicaldbKey
		// expects full accID (prefixPart + numericPart)
		// returns empty list if value contains "%"
		// returns empty list if value does not exist

		List<SlimAccessionDomain> results = new ArrayList<SlimAccessionDomain>();
		
		if (domain.getAccID().contains("%")) {
			return results;
		}

		String cmd = "\nselect * from acc_accession"
				+ "\nwhere _logicaldb_key = " + domain.getLogicaldbKey()
				+ "\nand _mgitype_key = 10"
				+ "\nand lower(accid) = '" + domain.getAccID().toLowerCase() + "'";
			
		// ignore the accession id for an existing strain...
		if (domain.getObjectKey() != null && !domain.getObjectKey().isEmpty()) {
			cmd = cmd + "\nand _object_key != " + domain.getObjectKey();
		}
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimAccessionDomain sdomain = new SlimAccessionDomain();	
				sdomain = slimtranslator.translate(accessionDAO.get(rs.getInt("_accession_key")));
				accessionDAO.clear();									
				results.add(sdomain);
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
	
	private List<AccessionDomain> getAccessionDomainList(String cmd) {
		// execute accession cmd and return list of accession domains
		// assumes the certain parameters are returned from cmd are return
		
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AccessionDomain domain = new AccessionDomain();
				domain = translator.translate(accessionDAO.get(rs.getInt("_accession_key")));
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
		
	@Transactional
	public List<AccessionDomain> getMCLEditAccessionIds(Integer key) {
		// gets mutant cell line accession ids : edit list
		// see all_cellline_acc_view for details
		// returns list of accession domain

		List<AccessionDomain> results = new ArrayList<AccessionDomain>();

		String cmd = "select * from all_cellline_acc_view "
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
		String isPreferred = "1";
		String isPrivate = "0";
		String doSplit = "1";
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAccession/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			if (domain.get(i).getPreferred() == null || domain.get(i).getPreferred().isEmpty()) {
				isPreferred = "1";
			}
			else {
				isPreferred = domain.get(i).getPreferred();
			}
			
			if (domain.get(i).getIsPrivate() == null || domain.get(i).getIsPrivate().isEmpty()) {
				isPrivate = "0";
			}
			else {
				isPrivate = domain.get(i).getIsPrivate();
			}
			
			// doi ids numeric may be too big to split
			if (domain.get(i).getLogicaldbKey().equals("65")) {
				doSplit = "0";
			}

			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				// minimum domain info for create:
				// processStatus (‘c’ for create) , logicaldbKey, mgitypekey, objectKey, accid

				if (domain.get(i).getAccID() == null || domain.get(i).getAccID().isEmpty()) {
					continue;
				}
				
				log.info("processAccession create");
				
				// refsKey can be null; set to -1
				String refsKey = "-1";
				if (domain.get(i).getReferences() != null) {
				    refsKey = domain.get(i).getReferences().get(0).getRefsKey();
				}
				
				cmd = "select count(*) from ACC_insert ("
							+ user.get_user_key().intValue()
							+ "," + parentKey
							+ ",'" + domain.get(i).getAccID() + "'"
							+ "," + domain.get(i).getLogicaldbKey()
							+ ",'" + mgiTypeName + "'"
							+ "," + refsKey
							+ "," + isPreferred
							+ "," + isPrivate
							+ "," + doSplit + ")";
				log.info("cmd: " + cmd);
				Query query = accessionDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAccession delete");
				cmd = "select count(*) from ACC_delete_byAccKey (" + domain.get(i).getAccessionKey() + ")";
				log.info("cmd: " + cmd);
				Query query = accessionDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
				log.info("processAccession delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {

				if (domain.get(i).getAccID() == null || domain.get(i).getAccID().isEmpty()) {
					continue;
				}

				log.info("processAccession update");
				Accession entity = accessionDAO.get(Integer.valueOf(domain.get(i).getAccessionKey()));
				
				// origRefsKey can be null; default = -1
				String origRefsKey = "-1";
				if (entity.getReferences().size() > 0) {
					log.info("processAccession update/getting original reference");
				    origRefsKey = String.valueOf(entity.getReferences().get(0).getReference().get_refs_key());
				}
				
				// refsKey can be null; default = -1
				String refsKey = "-1";
				if (domain.get(i).getReferences() != null) {
					log.info("processAccession update/getting new reference");
					refsKey = domain.get(i).getReferences().get(0).getRefsKey();
				}
				
				// need to add "v_private" to this SP
				cmd = "select count(*) from ACC_update ("
						+ user.get_user_key().intValue()
						+ "," + domain.get(i).getAccessionKey()
						+ ",'" + domain.get(i).getAccID() + "'"
						+ "," + isPrivate
						+ "," + origRefsKey
						+ "," + refsKey
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
		
		log.info("processAccession/processing successful");
		return modified;
	}

	@Transactional	
	public List<SlimAccessionDomain> validateGOIsoform(SlimAccessionDomain searchDomain) {
		// validates if GO/Isoform matches given Marker 
		// return SlimAccessionDomain
		
		List<SlimAccessionDomain> results = new ArrayList<SlimAccessionDomain>();

		String cmd = "select a.accID from ACC_Accession a, VOC_Annot va" 
				+ "\nwhere a._LogicalDB_key = 183"
				+ "\nand a.accID = '" + searchDomain.getAccID() + "'"
				+ "\nand a._Object_key = va._Term_key"
				+ "\nand va._AnnotType_key = 1019"
				+ "\nand va._Object_key = " + searchDomain.getObjectKey();

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAccessionDomain domain = new SlimAccessionDomain();
				domain.setAccID(rs.getString("accID"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	// Quick search for accession IDs and/or marker symbols
	public List<SlimAccessionDomain> getQSResultByAccid(String ids) {
		String cmd;

		// First search as accession IDs
		//

		// replace spaces with ","
		ids = ids.replaceAll("\\s+", ",");

		String accids = "'" + String.join("','", ids.split(",")) + "'";
		cmd = "select aa.*, t.name as typename, d.name as ldbname "
			+ "\nfrom acc_accession a, acc_mgitype t, acc_logicaldb d, acc_accession aa"
			+ "\nwhere lower(a.accid) in (" + accids.toLowerCase() + ")"
			+ "\nand a._logicaldb_key in (1,15,31,34,169,170,173,191)"
			+ "\nand a._mgitype_key in (1,2,3,4,6,8,9,11,12,13)"
			+ "\nand a._mgitype_key = t._mgitype_key"
			+ "\nand a._logicaldb_key = d._logicaldb_key"
			+ "\nand aa._object_key = a._object_key"
			+ "\nand aa._mgitype_key = a._mgitype_key"
			+ "\nand aa.preferred = 1"
			+ "\nand aa._logicaldb_key in (1,15,31,34,169,170,173,191)"
			+ "\nand aa._mgitype_key = t._mgitype_key"
			+ "\nand (aa._mgitype_key != 1 OR aa.prefixPart = 'J:')"

			;
		log.info(cmd);
		List<SlimAccessionDomain> summaryResults = new ArrayList<SlimAccessionDomain>();
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAccessionDomain domain = new SlimAccessionDomain();
				domain.setAccID(rs.getString("accid"));
				domain.setLogicaldbKey(rs.getString("_logicaldb_key"));
				domain.setLogicaldbName(rs.getString("ldbname"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setMgiTypeName(rs.getString("typename"));
				// If this accid is for a marker, go get its symbol
				if (rs.getString("_mgitype_key").equals("2")) {
				    String symbol = getMarkerSymbol(rs.getString("accid"));
				    domain.setSymbol(symbol);
				}
				summaryResults.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		// Now search again interpreting the ids as symbols
		for (SlimAccessionDomain d : getQSResultBySymbol(accids)) {
		    summaryResults.add(d);
		}

		return summaryResults;
	}

	// Returns the symbol of the marker with the given mgi id.
	private String getMarkerSymbol (String accid) {
		String cmd = "select m.symbol "
		+ "\nfrom mrk_marker m, acc_accession a "
		+ "\nwhere m._marker_key = a._object_key "
		+ "\nand a._mgitype_key = 2 "
		+ "\nand a._logicaldb_key = 1 "
		+ "\nand a.accid = '" + accid + "'"
		;
		String symbol = "";
		try {
		    log.info(cmd);
		    ResultSet rs = sqlExecutor.executeProto(cmd);
		    while (rs.next()) {
		        symbol = rs.getString("symbol");
		    }
		    sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return symbol;
	}

	// Returns the accession IDs for the given marker symbols.
	private List<SlimAccessionDomain> getQSResultBySymbol (String symbols) {
		String cmd = ""
		+ "\nselect m.symbol, a.*, 'Marker' as typename, 'MGI' as ldbname"
		+ "\nfrom mrk_marker m, acc_accession a"
		+ "\nwhere m._organism_key = 1"
		+ "\nand LOWER(m.symbol) in (" + symbols.toLowerCase() + ")"
		+ "\nand m._marker_key = a._object_key"
		+ "\nand a._mgitype_key = 2"
		+ "\nand a._logicaldb_key = 1"
		+ "\nand a.preferred = 1"
		;
		log.info(cmd);
		List<SlimAccessionDomain> summaryResults = new ArrayList<SlimAccessionDomain>();
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAccessionDomain domain = new SlimAccessionDomain();
				domain.setAccID(rs.getString("accid"));
				domain.setLogicaldbKey(rs.getString("_logicaldb_key"));
				domain.setLogicaldbName(rs.getString("ldbname"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setMgiTypeName(rs.getString("typename"));
				domain.setSymbol(rs.getString("symbol"));
				summaryResults.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return summaryResults;
	}
}
