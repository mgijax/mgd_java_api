package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.SlimReferenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceService extends BaseService<ReferenceDomain> {

	protected static Logger log = Logger.getLogger(MGISynonymService.class);

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
				domain = slimtranslator.translate(referenceDAO.get(rs.getInt("_refs_key")),1);			
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
			
}
