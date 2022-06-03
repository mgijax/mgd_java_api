package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceCitationCacheDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GXDIndexDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndex;
import org.jax.mgi.mgd.api.model.gxd.translator.GXDIndexTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGXDIndexTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GXDIndexService extends BaseService<GXDIndexDomain> {
	
	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GXDIndexDAO indexDAO;
	@Inject
	private ReferenceCitationCacheDAO referenceDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private GXDIndexStageService stageService;
	
	private GXDIndexTranslator translator = new GXDIndexTranslator();
	private SlimGXDIndexTranslator slimtranslator = new SlimGXDIndexTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<GXDIndexDomain> create(GXDIndexDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		GXDIndex entity = new GXDIndex();
		Boolean modified = false;
		
		log.info("processGXDIndex/create");

		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));
		entity.setPriority(termDAO.get(Integer.valueOf(domain.getPriorityKey())));
		entity.setConditionalMutants(termDAO.get(Integer.valueOf(domain.getConditionalMutantsKey())));
		
		if (domain.getComments() == null || domain.getComments().isEmpty()) {
			entity.setComments(null);
		}
		else {
			entity.setComments(domain.getComments());
		}
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		indexDAO.persist(entity);
				
		// process gxd_indexstages
		if (domain.getIndexStages() != null && !domain.getIndexStages().isEmpty()) {
			if (stageService.process(entity.get_index_key(), domain.getIndexStages(), user)) {
				modified = true;
			}
		}
		
		// return entity translated to domain
		log.info("processGXDIndex/create/returning results : " + modified);
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<GXDIndexDomain> update(GXDIndexDomain domain, User user) {
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"
				
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		GXDIndex entity = indexDAO.get(Integer.valueOf(domain.getIndexKey()));
		Boolean modified = false;
		
		log.info("processGXDIndex/update");
		
		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));
		entity.setPriority(termDAO.get(Integer.valueOf(domain.getPriorityKey())));
		entity.setConditionalMutants(termDAO.get(Integer.valueOf(domain.getConditionalMutantsKey())));
		
		if (domain.getComments() == null || domain.getComments().isEmpty()) {
			entity.setComments(null);
		}
		else {
			entity.setComments(domain.getComments());
		}
		
		// process gxd_indexstages
		if (domain.getIndexStages() != null && !domain.getIndexStages().isEmpty()) {
			if (stageService.process(entity.get_index_key(), domain.getIndexStages(), user)) {
				modified = true;
			}
		}
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			indexDAO.update(entity);
			log.info("processGXDIndex/changes processed: " + domain.getIndexKey());
		}
		else {
			log.info("processGXDIndex/no changes processed: " + domain.getIndexKey());
		}
		
		// return entity translated to domain
		log.info("processGXDIndex/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processGXDIndex/update/returned results succsssful");
		return results;		
	}
	 
	@Transactional
	public SearchResults<GXDIndexDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		GXDIndex entity = indexDAO.get(key);
		results.setItem(translator.translate(indexDAO.get(key)));
		indexDAO.remove(entity);
		return results;
	}  
		
	@Transactional
	public GXDIndexDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GXDIndexDomain domain = new GXDIndexDomain();
		if (indexDAO.get(key) != null) {
			domain = translator.translate(indexDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<GXDIndexDomain> getResults(Integer key) {
        SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
        results.setItem(translator.translate(indexDAO.get(key)));
        return results;
    } 

	@Transactional	
	public SearchResults<GXDIndexDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		String cmd = "select count(*) as objectCount from gxd_index";
		
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
	public List<SlimGXDIndexDomain> search(GXDIndexDomain searchDomain) {

		List<SlimGXDIndexDomain> results = new ArrayList<SlimGXDIndexDomain>();
		
		String cmd = "";
		String select = "select distinct i._index_key, r._refs_key, r.jnumid, r.numericpart, m.symbol";
		String from = "from gxd_index i, bib_citation_cache r, mrk_marker m";
		String where = "where i._refs_key = r._refs_key"
				+ "\nand i._marker_key = m._marker_key";
		String orderBy = "order by r.numericpart, m.symbol";
		String value;

		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("i", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		// reference
		value = searchDomain.getRefsKey();
		String jnumid = searchDomain.getJnumid();		
		if (value != null && !value.isEmpty()) {
			where = where + "\nand r._Refs_key = " + value;
		}
		else if (jnumid != null && !jnumid.isEmpty()) {
			jnumid = jnumid.toUpperCase();
			if (!jnumid.contains("J:")) {
					jnumid = "J:" + jnumid;
			}
			where = where + "\nand r.jnumid = '" + jnumid + "'";
		}
		
		// marker
		value = searchDomain.getMarkerKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand i._marker_key = " + value;					
		}
		else {
			value = searchDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand m.symbol ilike '" + value + "'";
			}
		}

		value = searchDomain.getPriorityKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand i._priority_key = " + value;
		}
		
		value = searchDomain.getConditionalMutantsKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand i._conditionalmutants_key = " + value;
		}
		
		value = searchDomain.getComments();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand i.comments ilike '" + value + "'";
		}

		// image stages
		if (searchDomain.getIndexStages() != null) {
			for (int i = 0; i < searchDomain.getIndexStages().size(); i++) {
				value = searchDomain.getIndexStages().get(i).getStageidKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand exists (select 1 from gxd_index_stages s where i._index_key = s._index_key"
							+ " and s._indexassay_key = " + searchDomain.getIndexStages().get(i).getIndexAssayKey()
							+ " and s._stageid_key = " + value + ")";				
				}
			}
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimGXDIndexDomain domain = new SlimGXDIndexDomain();
				domain = slimtranslator.translate(indexDAO.get(rs.getInt("_index_key")));					
				indexDAO.clear();
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
