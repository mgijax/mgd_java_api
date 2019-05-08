package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.SlimReferenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceService extends BaseService<ReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceDAO referenceDAO;
	
	private ReferenceTranslator translator = new ReferenceTranslator();
	private SlimReferenceTranslator slimtranslator = new SlimReferenceTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ReferenceDomain> create(ReferenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ReferenceDomain> update(ReferenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ReferenceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain		
		return translator.translate(referenceDAO.get(key),0);
	}

    @Transactional
    public SearchResults<ReferenceDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results.setItem(translator.translate(referenceDAO.get(key),0));
		return results;   	
    }

	@Transactional
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		Reference entity = referenceDAO.get(key);
		results.setItem(translator.translate(referenceDAO.get(key),0));
		referenceDAO.remove(entity);
		return results;
	}

	@Transactional	
	public List<SlimReferenceDomain> validJnum(String value) {
		// use SlimReferenceDomain to return list of validated reference
		// one value is expected
		// accepts value :  J:xxx or xxxx
		// returns empty list if value contains "%"
		// returns empty list if value does not exist

		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();
		
		if (value.contains("%")) {
			return results;
		}

		String cmd = "\nselect _refs_key from bib_citation_cache";
		String where = "\nwhere ";
		
		value = value.toLowerCase();
		if (value.contains("j:")) {
			where = where + "lower(jnumid) = '" + value + "'";
		}
		else {
			where = where + "numericpart = " + value;			
		}
		
		cmd = cmd + where;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimReferenceDomain domain = new SlimReferenceDomain();						
				domain = slimtranslator.translate(referenceDAO.get(rs.getInt("_refs_key")));			
				referenceDAO.clear();
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
	public List<SlimReferenceDomain> validateJnumCopyright(SlimReferenceDomain domain) {
		// use SlimReferenceDomain to return list of validated reference & copyright

		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();

		// validate the jnum
		String jnum = "";
		if (domain.getJnum() != null && !domain.getJnum().isEmpty()) {
			jnum = domain.getJnum();
		}
		else if (domain.getJnumID() != null && !domain.getJnumID().isEmpty()) {
			jnum = domain.getJnumID();
		}
		results = validJnum(jnum);
		
		log.info("copyright check");
		// set copyright to incoming json package
		results.get(0).setCopyright(domain.getCopyright());
		results.get(0).setNeedsDXDOIid(false);
		
		// if results is not null/empty
		// if reference key is not null/empty
		// if copyright is not null/empty
		// that is, do not overwrite an existing copyright
		
		if (results != null && !results.isEmpty()) {
			
			String key = results.get(0).getRefsKey();
			
			log.info("copyright validation");
			log.info(results.get(0).getCopyright());
			
			if (key != null && !key.isEmpty() 
					&& (results.get(0).getCopyright() == null
					|| results.get(0).getCopyright().isEmpty())) {
				
				// return copyright/null is OK
				
				String cmd = "\nselect * from bib_getCopyright(" + key + ")";
				log.info("cmd: " + cmd);

				try {
					Query query = referenceDAO.createNativeQuery(cmd);
					String r = (String) query.getSingleResult();
					results.get(0).setCopyright(r);
					
					// if DXDOI is missing....
					if (r.contains("DXDOI(||)")) {
						results.get(0).setNeedsDXDOIid(true);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}			
			}
		}
		
		return results;
	}
	
}
