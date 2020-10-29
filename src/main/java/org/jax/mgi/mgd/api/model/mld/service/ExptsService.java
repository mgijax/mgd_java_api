package org.jax.mgi.mgd.api.model.mld.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.ExptsDAO;
import org.jax.mgi.mgd.api.model.mld.domain.ExptsDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;
import org.jax.mgi.mgd.api.model.mld.translator.ExptsTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ExptsService extends BaseService<ExptsDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ExptsDAO exptsDAO;
	
	private ExptsTranslator translator = new ExptsTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	//private String mgiTypeKey = "4";
	
	@Transactional
	public SearchResults<ExptsDomain> create(ExptsDomain domain, User user) {	
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		Expts entity = new Expts();

		log.info("processExpt/create");		
		
		entity.setCreation_date(new Date());
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		exptsDAO.persist(entity);
		
		// return entity translated to domain
		log.info("processExpt/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<ExptsDomain> update(ExptsDomain domain, User user) {
		// update existing entity object from in-coming domain
		
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		Expts entity = exptsDAO.get(Integer.valueOf(domain.getExptKey()));
		Boolean modified = true;
		
		log.info("processExpt/update");				
		
		// finish update
		if (modified) {		
			entity.setModification_date(new Date());
			exptsDAO.update(entity);
			log.info("processExpt/changes processed: " + domain.getExptKey());		
		}
		
		// return entity translated to domain
		log.info("processExpt/update/returning results");
		results.setItem(translator.translate(entity));		
		log.info("processExpt/update/returned results succsssful");
		return results;			
	}

	@Transactional
	public SearchResults<ExptsDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		Expts entity = exptsDAO.get(key);
		results.setItem(translator.translate(exptsDAO.get(key)));
		exptsDAO.remove(entity);
		return results;
	}

	@Transactional
	public ExptsDomain get(Integer key) {
		// get the DAO/entity and translate -> domain

		ExptsDomain domain = new ExptsDomain();

		if (exptsDAO.get(key) != null) {
			domain = translator.translate(exptsDAO.get(key));		
		}
		
		return domain;
	}

    @Transactional
    public SearchResults<ExptsDomain> getResults(Integer key) {
        SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
        results.setItem(translator.translate(exptsDAO.get(key)));
        return results;
    }

	@Transactional	
	public SearchResults<ExptsDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		String cmd = "select count(*) as objectCount from mld_expts";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional
	public List<ExptsDomain> search(ExptsDomain searchDomain) {

		List<ExptsDomain> results = new ArrayList<ExptsDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct e.*";
		String from = "from mld_expts_view e";
		String where = "where e._expt_key is not null";
		String orderBy = "order by e.jnum";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		Boolean from_accession = false;
		
		// if parameter exists, then add to where-clause
//		String cmResults[] = DateSQLQuery.queryByCreationModification("p", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
//		if (cmResults.length > 0) {
//			from = from + cmResults[0];
//			where = where + cmResults[1];
//		}

		if (searchDomain.getExptType() != null && !searchDomain.getExptType().isEmpty()) {
			where = where + "\nand e.exptType = '" + searchDomain.getExptType() + "'";
		}
		
		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand e._refs_key = " + searchDomain.getRefsKey();
		}
		
		// building from...
		
		if (from_accession == true) {
			from = from + ", acc_accession acc";
			where = where + "\nand acc._mgitype_key = 4 and e._expt_key = acc._object_key and acc.prefixPart = 'MGI:'";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ExptsDomain domain = new ExptsDomain();
				domain = translator.translate(exptsDAO.get(rs.getInt("_probe_key")));				
				exptsDAO.clear();
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
