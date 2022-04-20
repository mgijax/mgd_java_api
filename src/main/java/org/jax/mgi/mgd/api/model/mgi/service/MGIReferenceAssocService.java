package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIReferenceAssocDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIReferenceAssocService extends BaseService<MGIReferenceAssocDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGIReferenceAssocDAO referenceAssocDAO;

	private MGIReferenceAssocTranslator translator = new MGIReferenceAssocTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> create(MGIReferenceAssocDomain domain, User user) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();		
		List<MGIReferenceAssocDomain> domainList = new ArrayList<MGIReferenceAssocDomain>();
		domainList.add(domain);
		process(null, domainList, domain.getMgiTypeKey(), user);
		results.setItem(domain);	
		return results;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> update(MGIReferenceAssocDomain domain, User user) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGIReferenceAssocDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
		if (referenceAssocDAO.get(key) != null) {
			domain = translator.translate(referenceAssocDAO.get(key));
		}
		referenceAssocDAO.clear();
		return domain;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> getResults(Integer key) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();
		results.setItem(translator.translate(referenceAssocDAO.get(key)));
		referenceAssocDAO.clear();
		return results;
	}

	@Transactional	
	public List<MGIReferenceAssocDomain> getByMarker(Integer key) {

		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();

		String cmd = "\nselect _assoc_key from mgi_reference_marker_view " 
				+ "\nwhere _object_key = " + key
				+ "\norder by _refassoctype_key, jnum";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
				domain = translator.translate(referenceAssocDAO.get(rs.getInt("_assoc_key")));
				referenceAssocDAO.clear();
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
	public List<MGIReferenceAlleleAssocDomain> getAlleles(Integer key) {
		// return list of reference/allele associations for given reference key
		
		List<MGIReferenceAlleleAssocDomain> results = new ArrayList<MGIReferenceAlleleAssocDomain>();

		String cmd = "\nselect r.* from MGI_Reference_Allele_View r "
				+ "\nwhere r._Refs_key = " + key
				+"\norder by r.symbol, r.assocType";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				// use super-class translator
				MGIReferenceAssocDomain superDomain = new MGIReferenceAssocDomain();
				superDomain = translator.translate(referenceAssocDAO.get(rs.getInt("_assoc_key")));
				referenceAssocDAO.clear();
				
				MGIReferenceAlleleAssocDomain domain = new MGIReferenceAlleleAssocDomain();
			
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);				
				domain.setAssocKey(superDomain.getAssocKey());
				domain.setObjectKey(superDomain.getObjectKey());
				domain.setMgiTypeKey(superDomain.getMgiTypeKey());
				domain.setRefAssocType(superDomain.getRefAssocType());
				domain.setRefAssocTypeKey(superDomain.getRefAssocTypeKey());
				domain.setRefsKey(superDomain.getRefsKey());
				domain.setModification_date(superDomain.getModification_date());
				domain.setModifiedBy(superDomain.getModifiedBy());
				domain.setModifiedByKey(superDomain.getModifiedByKey());
				
				// subclass-specific info from SQL query
				domain.setAlleleSymbol(rs.getString("symbol"));
				domain.setAlleleAccID(rs.getString("accID"));
				domain.setAlleleMarkerSymbol(rs.getString("markerSymbol"));
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
	public List<MGIReferenceMarkerAssocDomain> getMarkers(Integer key) {
		// return list of reference/marker associations for given reference key
		
		List<MGIReferenceMarkerAssocDomain> results = new ArrayList<MGIReferenceMarkerAssocDomain>();

		String cmd = "\nselect r.* from MGI_Reference_Marker_View r "
				+ "\nwhere r._Refs_key = " + key
				+"\norder by r.symbol, r.assocType";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				// use super-class translator
				MGIReferenceAssocDomain superDomain = new MGIReferenceAssocDomain();
				superDomain = translator.translate(referenceAssocDAO.get(rs.getInt("_assoc_key")));
				referenceAssocDAO.clear();
				
				MGIReferenceMarkerAssocDomain domain = new MGIReferenceMarkerAssocDomain();
			
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);				
				domain.setAssocKey(superDomain.getAssocKey());
				domain.setObjectKey(superDomain.getObjectKey());
				domain.setMgiTypeKey(superDomain.getMgiTypeKey());
				domain.setRefAssocType(superDomain.getRefAssocType());
				domain.setRefAssocTypeKey(superDomain.getRefAssocTypeKey());
				domain.setRefsKey(superDomain.getRefsKey());
				domain.setModification_date(superDomain.getModification_date());
				domain.setModifiedBy(superDomain.getModifiedBy());
				domain.setModifiedByKey(superDomain.getModifiedByKey());
				
				// subclass-specific info from SQL query			
				domain.setMarkerSymbol(rs.getString("symbol"));
				domain.setMarkerAccID(rs.getString("accID"));
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
	public List<MGIReferenceStrainAssocDomain> getStrains(Integer key) {
		// return list of reference/strain associations for given reference key
		
		List<MGIReferenceStrainAssocDomain> results = new ArrayList<MGIReferenceStrainAssocDomain>();

		String cmd = "\nselect r.* from MGI_Reference_Strain_View r "
				+ "\nwhere r._Refs_key = " + key
				+"\norder by r.strain, r.assocType";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				// use super-class translator
				MGIReferenceAssocDomain superDomain = new MGIReferenceAssocDomain();
				superDomain = translator.translate(referenceAssocDAO.get(rs.getInt("_assoc_key")));
				referenceAssocDAO.clear();
				
				MGIReferenceStrainAssocDomain domain = new MGIReferenceStrainAssocDomain();
			
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);				
				domain.setAssocKey(superDomain.getAssocKey());
				domain.setObjectKey(superDomain.getObjectKey());
				domain.setMgiTypeKey(superDomain.getMgiTypeKey());
				domain.setRefAssocType(superDomain.getRefAssocType());
				domain.setRefAssocTypeKey(superDomain.getRefAssocTypeKey());
				domain.setRefsKey(superDomain.getRefsKey());
				domain.setModification_date(superDomain.getModification_date());
				domain.setModifiedBy(superDomain.getModifiedBy());
				domain.setModifiedByKey(superDomain.getModifiedByKey());
				
				// subclass-specific info from SQL query			
				domain.setStrainSymbol(rs.getString("strain"));
				domain.setStrainAccID(rs.getString("accID"));
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
	public Boolean process(String parentKey, List<MGIReferenceAssocDomain> domain, String mgiTypeKey, User user) {
		// process reference associations (create, delete, update)
		// if parent is different in each domain row, then set parentKey = null
		
		String objectKey = null;
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processReferenceAssoc/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {		
			
			// if parentKey is null, then use object key 
			if (parentKey == null || parentKey.isEmpty()) {
				objectKey = domain.get(i).getObjectKey();
			}
			else {
				objectKey = parentKey;
			}
			
			// still no object...break
			if (objectKey == null || objectKey.isEmpty()) {
				continue;
			}

			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				// minimum domain info for create:
				// processStatus (‘c’ for create) , mgiTypeKey, parentKey/objectKey, refsKey, refType (string)
							
				log.info("processReferenceAssoc create");

				// if reference is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getRefsKey().isEmpty()) {
					continue;
				}	
			
				// if mgiTypeKey = marker, then default synonym type "exact" (1004)
				if (mgiTypeKey.equals("2")) {
					if (domain.get(i).getRefAssocTypeKey() == null || domain.get(i).getRefAssocTypeKey().isEmpty()) {
						domain.get(i).setRefAssocTypeKey("1018");
					}
				}
				// if mgiTypeKey = antibody, then default refAssoc type "primary" (1026)
				else if (mgiTypeKey.equals("6")) {
					if (domain.get(i).getRefAssocTypeKey() == null || domain.get(i).getRefAssocTypeKey().isEmpty()) {
						domain.get(i).setRefAssocTypeKey("1026");
					}
				}
				// if mgiTypeKey = strain, then default refAssoc type "Selected" (1009)
				else if (mgiTypeKey.equals("10")) {
					if (domain.get(i).getRefAssocTypeKey() == null || domain.get(i).getRefAssocTypeKey().isEmpty()) {
						domain.get(i).setRefAssocTypeKey("1009");
					}
				}

				// select count(*) from MGI_insertReferenceAssoc (1014,6,0,275403,1027)
				cmd = "select count(*) from MGI_insertReferenceAssoc ("
							+ user.get_user_key().intValue()
							+ "," + mgiTypeKey
							+ "," + objectKey
							+ "," + domain.get(i).getRefsKey()
							+ "," + domain.get(i).getRefAssocTypeKey()
							+ ")";
				log.info("cmd: " + cmd);
				Query query = referenceAssocDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processReferenceAssoc delete");
				MGIReferenceAssoc entity = referenceAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				referenceAssocDAO.remove(entity);
				log.info("processReferenceAssoc delete successful");
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processReferenceAssoc update");

				cmd = "select count(*) from MGI_updateReferenceAssoc ("
						+ user.get_user_key().intValue()
						+ "," + mgiTypeKey
						+ "," + objectKey
						+ "," + domain.get(i).getRefsKey()
						+ "," + domain.get(i).getRefAssocTypeKey()
						+ "," + domain.get(i).getAssocKey()
						+ ")";
				log.info("cmd: " + cmd);
				Query query = referenceAssocDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
		}
		
		log.info("processReferenceAssoc/processing successful");
		return modified;
	}

	@Transactional
	public Boolean processAlleleAssoc(List<MGIReferenceAlleleAssocDomain> domain, User user) {
		// process reference/allele associations (create, delete, update)
		// from sub-class (Allele), build super-class and pass to "process()"
		
		if (domain == null || domain.isEmpty()) {
			return false;
		}

		List<MGIReferenceAssocDomain> listOfSuperDomains = new ArrayList<MGIReferenceAssocDomain>();

		// iterate thru the list of rows in the subclass-domain
		
		for (int i = 0; i < domain.size(); i++) {		
			MGIReferenceAssocDomain superDomain = new MGIReferenceAssocDomain();
			superDomain.setProcessStatus(domain.get(i).getProcessStatus());				
			superDomain.setAssocKey(domain.get(i).getAssocKey());
			superDomain.setObjectKey(domain.get(i).getObjectKey());
			superDomain.setMgiTypeKey(domain.get(i).getMgiTypeKey());
			superDomain.setRefAssocType(domain.get(i).getRefAssocType());
			superDomain.setRefAssocTypeKey(domain.get(i).getRefAssocTypeKey());
			superDomain.setRefsKey(domain.get(i).getRefsKey());
			listOfSuperDomains.add(superDomain);
		}
		
		return process(null, listOfSuperDomains, "11", user);
	}

	@Transactional
	public Boolean processStrainAssoc(List<MGIReferenceStrainAssocDomain> domain, User user) {
		// process reference/strain associations (create, delete, update)
		// from sub-class (Strain), build super-class and pass to "process()"
		
		if (domain == null || domain.isEmpty()) {
			return false;
		}
	
		List<MGIReferenceAssocDomain> listOfSuperDomains = new ArrayList<MGIReferenceAssocDomain>();

		// iterate thru the list of rows in the subclass-domain
		
		for (int i = 0; i < domain.size(); i++) {		
			MGIReferenceAssocDomain superDomain = new MGIReferenceAssocDomain();
			superDomain.setProcessStatus(domain.get(i).getProcessStatus());				
			superDomain.setAssocKey(domain.get(i).getAssocKey());
			superDomain.setObjectKey(domain.get(i).getObjectKey());
			superDomain.setMgiTypeKey(domain.get(i).getMgiTypeKey());
			superDomain.setRefAssocType(domain.get(i).getRefAssocType());
			superDomain.setRefAssocTypeKey(domain.get(i).getRefAssocTypeKey());
			superDomain.setRefsKey(domain.get(i).getRefsKey());
			listOfSuperDomains.add(superDomain);
		}
		
		return process(null, listOfSuperDomains, "10", user);
	}

	@Transactional
	public Boolean processMarkerAssoc(List<MGIReferenceMarkerAssocDomain> domain, User user) {
		// process reference/strain associations (create, delete, update)
		// from sub-class (Marker), build super-class and pass to "process()"
		
		if (domain == null || domain.isEmpty()) {
			return false;
		}
	
		List<MGIReferenceAssocDomain> listOfSuperDomains = new ArrayList<MGIReferenceAssocDomain>();

		// iterate thru the list of rows in the subclass-domain
		
		for (int i = 0; i < domain.size(); i++) {		
			MGIReferenceAssocDomain superDomain = new MGIReferenceAssocDomain();
			superDomain.setProcessStatus(domain.get(i).getProcessStatus());				
			superDomain.setAssocKey(domain.get(i).getAssocKey());
			superDomain.setObjectKey(domain.get(i).getObjectKey());
			superDomain.setMgiTypeKey(domain.get(i).getMgiTypeKey());
			superDomain.setRefAssocType(domain.get(i).getRefAssocType());
			superDomain.setRefAssocTypeKey(domain.get(i).getRefAssocTypeKey());
			superDomain.setRefsKey(domain.get(i).getRefsKey());
			listOfSuperDomains.add(superDomain);
		}
		
		return process(null, listOfSuperDomains, "2", user);
	}
		
}
