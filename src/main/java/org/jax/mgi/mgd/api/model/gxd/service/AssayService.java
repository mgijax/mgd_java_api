package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAssayTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AssayService extends BaseService<AssayDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AssayDAO assayDAO;
	
	private AssayTranslator translator = new AssayTranslator();
	private SlimAssayTranslator slimtranslator = new SlimAssayTranslator();

	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AssayDomain> create(AssayDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		Assay entity = new Assay();
		
		log.info("processAssay/create");
		
		//
		// IN PROGRESSS
		//
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		assayDAO.persist(entity);
		
		// return entity translated to domain
		log.info("processAssay/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<AssayDomain> update(AssayDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		Assay entity = assayDAO.get(Integer.valueOf(domain.getAssayKey()));
		Boolean modified = false;
//		String mgiTypeKey = "8";
//		String mgiTypeName = "Assay";
		
		log.info("processAssay/update");
		
		//
		// IN PROGRESSS
		//
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			assayDAO.update(entity);
			log.info("processAssay/changes processed: " + domain.getAssayKey());
		}
		else {
			log.info("processAssay/no changes processed: " + domain.getAssayKey());
		}
			
		// return entity translated to domain
		log.info("processAssay/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAssay/update/returned results succsssful");
		return results;
	}
	
	@Transactional
	public AssayDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AssayDomain domain = new AssayDomain();
		if (assayDAO.get(key) != null) {
			domain = translator.translate(assayDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AssayDomain> getResults(Integer key) {
        SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
        results.setItem(translator.translate(assayDAO.get(key)));
        return results;
    }
 
	@Transactional
	public SearchResults<AssayDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		Assay entity = assayDAO.get(key);
		results.setItem(translator.translate(assayDAO.get(key)));
		assayDAO.remove(entity);
		return results;
	}   

	@Transactional
	public List<SlimAssayDomain> search(AssayDomain searchDomain) {

		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._assay_key";
		String from = "from gxd_assay a, gxd_assaytype t";
		String where = "where a._assaytype_key = t._assaytype_key";
		String orderBy = "order by a.assaytype";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
		Boolean from_marker = false;
		Boolean from_accession = false;
		Boolean from_reference = false;
		
		//
		// IN PROGRESSS
		//
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		// marker
		if (searchDomain.getMarkerKey() != null && !searchDomain.getMarkerKey().isEmpty()) {
			where = where + "\nand a._marker_key = " + searchDomain.getMarkerKey();
		}
		if (searchDomain.getMarkerSymbol() != null && !searchDomain.getMarkerSymbol().isEmpty()) {
			where = where + "\nand m.symbol ilike '" + searchDomain.getMarkerSymbol() + "'";
			from_marker = true;
		}		
		
		// assay accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			from_accession = true;
		}
						
		// reference
		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand r._Refs_key = " + searchDomain.getRefsKey();
			from_reference = true;
		}
	
		if (from_marker == true) {
			from = from + ", mrk_marker m";
			where = where + "\nand a._marker_key = m._marker_key";
		}
		if (from_accession == true) {
			from = from + ", gxd_assay_acc_view acc";
			where = where + "\nand a._assay_key = acc._object_key"; 
		}
		if (from_reference == true) {
			from = from + ", bib_citation_cache r";
			where = where + "\nand a._refs_key = r._refs_key";
		}

		// make this easy to copy/paste for troubleshooting
		//cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAssayDomain domain = new SlimAssayDomain();
				domain = slimtranslator.translate(assayDAO.get(rs.getInt("_assay_key")));				
				assayDAO.clear();
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
