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
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndex;
import org.jax.mgi.mgd.api.model.gxd.translator.GXDIndexTranslator;
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
	private SQLExecutor sqlExecutor = new SQLExecutor();

	String mgiTypeKey = "8";
	String mgiTypeName = "Assay";
	
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
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		indexDAO.persist(entity);
				
		// process gxd_indexstages
//		if (domain.getSpecimens() != null && !domain.getSpecimens().isEmpty()) {
//			if (specimenService.process(entity.get_assay_key(), domain.getSpecimens(), user)) {
//				modified = true;
//			}
//		}
		
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
		
		// process gxd_indexstages
//		if (domain.getSpecimens() != null && !domain.getSpecimens().isEmpty()) {
//			if (specimenService.process(Integer.valueOf(domain.getAssayKey()), domain.getSpecimens(), user)) {
//				modified = true;
//			}
//		}
		
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
		String cmd = "select count(*) as objectCount from gxd_assay";
		
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
	public List<GXDIndexDomain> search(GXDIndexDomain searchDomain) {

		List<GXDIndexDomain> results = new ArrayList<GXDIndexDomain>();
		
		String cmd = "";
		String select = "select distinct a._assay_key, r._refs_key, r.jnumid, r.numericpart, t.assayType, m.symbol";
		String from = "from gxd_assay a, gxd_assaytype t, bib_citation_cache r, mrk_marker m";
		String where = "where a._assaytype_key = t._assaytype_key"
				+ "\nand a._refs_key = r._refs_key"
				+ "\nand a._marker_key = m._marker_key";
		String orderBy = "order by r.numericpart, t.assayType, m.symbol";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		Boolean from_accession = false;
		Boolean from_specimen = false;

		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
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
			where = where + "\nand a._marker_key = " + value;					
		}
		else {
			value = searchDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand m.symbol ilike '" + value + "'";
			}
		}
		
		// image stages
		
//		if (searchDomain.getSpecimens() != null) {
//			
//			value = searchDomain.getSpecimens().get(0).getSpecimenLabel();
//			value = value.replace("'",  "''");
//			if (value != null && !value.isEmpty()) {
//				where = where + "\nand s.specimenLabel ilike '" + value + "'";				
//				from_specimen = true;
//			}
//		}
		
		// assay accession id 
//		value = searchDomain.getAccID();			
//		if (value != null && !value.isEmpty()) {	
//			if (!value.contains("MGI:")) {
//				value = "MGI:" + value;
//			}
//			where = where + "\nand acc.accID = '" + value + "'";
//			from_accession = true;
//		}
				
		if (from_accession == true) {
			from = from + ", gxd_assay_acc_view acc";
			where = where + "\nand a._assay_key = acc._object_key"; 
		}
		if (from_specimen == true) {
			from = from + ", gxd_specimen s";
			where = where + "\nand a._assay_key = s._assay_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		//cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
//		try {
//			ResultSet rs = sqlExecutor.executeProto(cmd);
//			while (rs.next()) {
//				GXDIndexDomain domain = new GXDIndexDomain();
//				domain = translator.translate(GXDIndexDAO.get(rs.getInt("_assay_key")));	
//				domain.setAssayDisplay(rs.getString("jnumid") + "; " + domain.getAssayTypeAbbrev() + "; " + rs.getString("symbol"));	
//				domain.setRefsKey(rs.getString("_refs_key"));
//				domain.setJnumid(rs.getString("jnumid"));
//				domain.setJnum(rs.getString("numericpart"));				
//				GXDIndexDAO.clear();
//				results.add(domain);
//			}
//			sqlExecutor.cleanup();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return results;
	}
	
}
