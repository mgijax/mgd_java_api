package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAntibodyTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyService extends BaseService<AntibodyDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyDAO antibodyDAO;
	
	private AntibodyTranslator translator = new AntibodyTranslator();
	private SlimAntibodyTranslator slimtranslator = new SlimAntibodyTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AntibodyDomain> create(AntibodyDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		Antibody entity = new Antibody();
		
		log.info("processAntibody/create");
		
		//
		// IN PROGRESSS
		//
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		antibodyDAO.persist(entity);
		
		// return entity translated to domain
		log.info("processAntibody/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<AntibodyDomain> update(AntibodyDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		Antibody entity = antibodyDAO.get(Integer.valueOf(domain.getAntibodyKey()));
		Boolean modified = false;
//		String mgiTypeKey = "6";
//		String mgiTypeName = "Antibody";
		
		log.info("processAntibody/update");
		
		//
		// IN PROGRESSS
		//
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			antibodyDAO.update(entity);
			log.info("processAntibody/changes processed: " + domain.getAntibodyKey());
		}
		else {
			log.info("processAntibody/no changes processed: " + domain.getAntibodyKey());
		}
			
		// return entity translated to domain
		log.info("processAntibody/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAntibody/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<AntibodyDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		Antibody entity = antibodyDAO.get(key);
		results.setItem(translator.translate(antibodyDAO.get(key)));
		antibodyDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public AntibodyDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyDomain domain = new AntibodyDomain();
		if (antibodyDAO.get(key) != null) {
			domain = translator.translate(antibodyDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyDomain> getResults(Integer key) {
        SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
        results.setItem(translator.translate(antibodyDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		String cmd = "select count(*) as objectCount from gxd_antibody";
		
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
	public List<SlimAntibodyDomain> search(AntibodyDomain searchDomain) {

		List<SlimAntibodyDomain> results = new ArrayList<SlimAntibodyDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_antibody a";
		String where = "where a._antibody_key is not null";
		String orderBy = "order by a.antibodyName";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
		Boolean from_accession = false;
		
		//
		// IN PROGRESSS
		//
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			from_accession = true;
		}						
	
		if (from_accession == true) {
			from = from + ", gxd_antibody_acc_view acc";
			where = where + "\nand a._assay_key = acc._object_key"; 
		}

		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAntibodyDomain domain = new SlimAntibodyDomain();
				domain = slimtranslator.translate(antibodyDAO.get(rs.getInt("_antibody_key")));				
				antibodyDAO.clear();
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
