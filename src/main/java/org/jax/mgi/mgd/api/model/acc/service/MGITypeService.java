package org.jax.mgi.mgd.api.model.acc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimMGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.translator.MGITypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGITypeService extends BaseService<MGITypeDomain> {
	
	protected Logger log = Logger.getLogger(MGITypeService.class);
		
	@Inject
	private MGITypeDAO mgitypeDAO;
	
	private MGITypeTranslator translator = new MGITypeTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGITypeDomain> create(MGITypeDomain object, User user) {
		return null;
	}

	@Transactional
	public SearchResults<MGITypeDomain> update(MGITypeDomain object, User user) {
		return null;
	}

	@Transactional
	public MGITypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGITypeDomain domain = new MGITypeDomain();
		if (mgitypeDAO.get(key) != null) {
			domain = translator.translate(mgitypeDAO.get(key),1);
		}
		return domain;
	}

    @Transactional
    public SearchResults<MGITypeDomain> getResults(Integer key) {
        SearchResults<MGITypeDomain> results = new SearchResults<MGITypeDomain>();
        results.setItem(translator.translate(mgitypeDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<MGITypeDomain> delete(Integer key, User user) {
	    return null;
	}

	@Transactional
	public SearchResults<SlimMGITypeDomain> search(SlimMGITypeDomain searchDomain) {	
		// search for 1 mgi type with >= 1 organisms
		// assumes that either key or  name is being searched
		// returns empty result items if vocabulary does not exist
		// returns MGITypeDomain results if vocabulary does exist
			
		SearchResults<SlimMGITypeDomain> results = new SearchResults<SlimMGITypeDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "select * from mgi_organism_mgitype_view"
				+ "\nwhere typename = '" + searchDomain.getName() + "'"
				+ "\norder by sequencenum";	
		log.info(cmd);		

		try {
			SlimMGITypeDomain domain = new SlimMGITypeDomain();						
			List<SlimOrganismDomain> organismList = new ArrayList<SlimOrganismDomain>();
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				SlimOrganismDomain organismDomain = new SlimOrganismDomain();				
		
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setName(rs.getString("typename"));
				organismDomain.setOrganismKey(rs.getString("_organism_key"));
				// for backward compatibility with gxd/ht	
				organismDomain.set_organism_key(rs.getInt("_organism_key"));
				organismDomain.setCommonname(rs.getString("commonname"));
				organismList.add(organismDomain);
			}
			
			domain.setOrganisms(organismList);
			results.setItem(domain);		
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
