package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;

import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;

// DAOs, entities and translators
import org.jax.mgi.mgd.api.model.mgi.entities.User;
//import org.jax.mgi.mgd.api.model.gxd.dao.HTDAO;
//import org.jax.mgi.mgd.api.model.gxd.entities.foo;
//import org.jax.mgi.mgd.api.model.gxd.translator.foo;


import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;

import org.jboss.logging.Logger;

@RequestScoped
public class HTService extends BaseService<HTDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private SQLExecutor sqlExecutor = new SQLExecutor();



// future DAOs
//	@Inject
//	private HTDAO htDAO;

// future services
//	@Inject
//	private HTService htService;

// ---WILL NEED A TRANSLATOR	
//	private SlimAssayTranslator slimtranslator = new SlimAssayTranslator();


	
	@Transactional
	public List<SlimHTDomain> search(HTDomain searchDomain) {

		List<SlimHTDomain> results = new ArrayList<SlimHTDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select acc.accid, hte._experiment_key, hte._curationstate_key ";
		String from = "from gxd_htexperiment hte, acc_accession acc ";
		String where = "where hte._experiment_key = acc._object_key ";
		String orderBy = "order by acc.accid ";

		// used to hold searchDomain values
		String value;

		Boolean from_accession = false;

		
		// accession id 
		value = searchDomain.getPrimaryid();			
		if (value != null && !value.isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + value + "'";
			from_accession = true;
		}
		
		
//		if (from_accession == true) {
//			from = from + ", gxd_assay_acc_view acc";
//			where = where + "\nand a._assay_key = acc._object_key"; 
//		}

		
		// log for easy copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimHTDomain domain = new SlimHTDomain();

				domain.set_experiment_key(rs.getString("_experiment_key"));
				domain.setPrimaryid(rs.getString("accid"));
				domain.set_curationstate_key(rs.getString("_curationstate_key"));
				results.add(domain);		
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}



/*
*  TODO - SHELLED OUT OVER-RIDES OF BASE CLASS'S METHODS
*
*/




    @Transactional
    public SearchResults<HTDomain> getResults(Integer key) {
        SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//        results.setItem(translator.translate(assayDAO.get(key)));
        return results;
    } 

	@Transactional
	public HTDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		HTDomain domain = new HTDomain();
//		if (assayDAO.get(key) != null) {
//			domain = translator.translate(assayDAO.get(key));
//		}
		return domain;
	}

	@Transactional
	public SearchResults<HTDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//		Assay entity = assayDAO.get(key);
//		results.setItem(translator.translate(assayDAO.get(key)));
//		assayDAO.remove(entity);
		return results;
	}  


	@Transactional
	public SearchResults<HTDomain> update(HTDomain domain, User user) {
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"
				
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		log.info("HT update");
		

		return results;		
	}


	@Transactional
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		log.info("HT create");

		return results;
	}











	
}
