package org.jax.mgi.mgd.api.model.acc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimMGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.translator.MGITypeTranslator;
import org.jax.mgi.mgd.api.model.acc.translator.SlimMGITypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class MGITypeService extends BaseService<MGITypeDomain> {
	
	protected Logger log = Logger.getLogger(getClass());
		
	@Inject
	private MGITypeDAO mgitypeDAO;
	
	private MGITypeTranslator translator = new MGITypeTranslator();	
	private SlimMGITypeTranslator slimtranslator = new SlimMGITypeTranslator();

	@Transactional
	public SearchResults<MGITypeDomain> create(MGITypeDomain object, User user) {
		SearchResults<MGITypeDomain> results = new SearchResults<MGITypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGITypeDomain> update(MGITypeDomain object, User user) {
		SearchResults<MGITypeDomain> results = new SearchResults<MGITypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<MGITypeDomain> delete(Integer key, User user) {
		SearchResults<MGITypeDomain> results = new SearchResults<MGITypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGITypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGITypeDomain domain = new MGITypeDomain();
		if (mgitypeDAO.get(key) != null) {
			domain = translator.translate(mgitypeDAO.get(key));
		}
		mgitypeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MGITypeDomain> getResults(Integer key) {
        SearchResults<MGITypeDomain> results = new SearchResults<MGITypeDomain>();
        results.setItem(translator.translate(mgitypeDAO.get(key)));
		mgitypeDAO.clear();
        return results;
    }

	@Transactional
	public List<SlimMGITypeDomain> search(SlimMGITypeDomain searchDomain) {	
		// search for all acc_mgitype
		// only those that have pwi/modules
		// Marker : 2
		// Segment : 3
		// Antibody : 6
		// Antigen : 7
		// GXD HT Sample : 43
		// Allele : 11
		// Relationship : 40
		// MGI Relationship : 40

		List<SlimMGITypeDomain> results = new ArrayList<SlimMGITypeDomain>();

		String cmd = "select * from acc_mgitype"
				+ "\nwhere _mgitype_key in (2, 3, 6, 7, 11, 40, 43)"
				+ "\norder by name";
		log.info(cmd);		

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				SlimMGITypeDomain domain = new SlimMGITypeDomain();									
				domain = slimtranslator.translate(mgitypeDAO.get(rs.getInt("_mgitype_key")));
				mgitypeDAO.clear();	
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
