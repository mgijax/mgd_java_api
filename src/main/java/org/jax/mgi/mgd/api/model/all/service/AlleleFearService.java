package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleFearTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleFearTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleFearService extends BaseService<AlleleFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AlleleDAO alleleFearDAO;
	@Inject
	private RelationshipService relationshipService;
	
	private AlleleFearTranslator translator = new AlleleFearTranslator();
	private SlimAlleleFearTranslator slimtranslator = new SlimAlleleFearTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "";

	@Transactional
	public SearchResults<AlleleFearDomain> create(AlleleFearDomain domain, User user) {	
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<AlleleFearDomain> update(AlleleFearDomain domain, User user) {
		// translate pwi/incoming AlleleFearDomain json domain to list of RelationshipDomain
		// use this list of domain to process hibernate entities
		
		log.info("AlleleFearService.update");
		
		List<RelationshipDomain> relationshipList = new ArrayList<RelationshipDomain>();
		Boolean modified = false;

    	// Iterate thru incoming allele Fear relationship domain
		for (int i = 0; i < domain.getRelationships().size(); i++) {
			
			// if processStatus == "x", then continue; no need to create domain/process anything
			if (domain.getRelationships().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
				continue;
			}
			
			RelationshipDomain relationshipDomain = new RelationshipDomain();
		
			relationshipDomain.setProcessStatus(domain.getRelationships().get(i).getProcessStatus());
			relationshipDomain.setRelationshipKey(domain.getRelationships().get(i).getRelationshipKey());
			relationshipDomain.setCategoryKey(domain.getRelationships().get(i).getCategoryKey());
			relationshipDomain.setRelationshipTermKey(domain.getRelationships().get(i).getRelationshipTerm_key());
			relationshipDomain.setQualifierKey(domain.getRelationships().get(i).getQualifierKey());
			relationshipDomain.setEvidenceKey(domain.getRelationships().get(i).getEvidenceKey());
			relationshipDomain.setRefsKey(domain.getRelationships().get(i).getRefsKey());
			relationshipDomain.setCreatedByKey(domain.getRelationships().get(i).getCreatedByKey());
			relationshipDomain.setModifiedByKey(domain.getRelationships().get(i).getModifiedByKey());
			
			// add properties to this relationship
			relationshipDomain.setProperties(domain.getRelationships().get(i).getProperties());
            
			// add relationshipDomain to relationshipList
			relationshipList.add(relationshipDomain);         
		}
		
		// process relationships
		if (relationshipList.size() > 0) {
			log.info("send json normalized domain to services");			
			if (relationshipService.process(relationshipList, mgiTypeKey, user)) {
				modified = true;
			}
		}
		
		if (modified) {
			log.info("processAlleleFear/changes processed: " + domain.getAlleleKey());
		}
		else {
			log.info("processAlleleFear/no changes processed: " + domain.getAlleleKey());
		}
		
		log.info("repackage incoming domain as results");		
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results = getResults(Integer.valueOf(domain.getAlleleKey()));
		results.setItem(domain);
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public AlleleFearDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		AlleleFearDomain domain = new AlleleFearDomain();
		if (alleleFearDAO.get(key) != null) {
			domain = translator.translate(alleleFearDAO.get(key));
		}
		alleleFearDAO.clear();
		return domain;	
	}

	@Transactional
	public SearchResults<AlleleFearDomain> delete(Integer key, User user) {
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<AlleleFearDomain> getResults(Integer key) {
		// get the domain -> results
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results.setItem(get(key));
		return results;
	}

	@Transactional	
	public SearchResults<AlleleFearDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		String cmd = "select count(*) as objectCount from mgi_relationship_fear_view";
		
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
	public List<SlimAlleleFearDomain> search(AlleleFearDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimAlleleFearDomain> results = new ArrayList<SlimAlleleFearDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 

		String cmd = "";
		String select = "select distinct v._object_key_1, v.allelesymbol, v.markersymbol";
		String from = "from mgi_relationship_Fear_view v";		
		String where = "where v._object_key_1 is not null";
		String orderBy = "order by v.allelesymbol, v.markersymbol";
		
		String value;

		Boolean from_property = false;
		
		// if parameter exists, then add to where-clause
		
		value = searchDomain.getAlleleKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand v._object_key_1 = " + value;
		}
		
		value = searchDomain.getSymbol();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand v.allelesymbol ilike '" + value + "'";
		}
		
		// accession id
		value = searchDomain.getAccID();
		if (value != null && !value.isEmpty()) {
			String mgiid = value.toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(v.accID) = '" + mgiid.toLowerCase() + "'";
		}

		if (searchDomain.getRelationships() != null) {
						
			RelationshipFearDomain relationshipDomain = searchDomain.getRelationships().get(0);
		
			String cmResults[] = DateSQLQuery.queryByCreationModification("v", 
				relationshipDomain.getCreatedBy(), 
				relationshipDomain.getModifiedBy(), 
				relationshipDomain.getCreation_date(), 
				relationshipDomain.getModification_date());
	
			if (cmResults.length > 0) {
				if (cmResults[0].length() > 0 || cmResults[1].length() > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
				}
			}
			
			value = relationshipDomain.getMarkerKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._object_key_2 = " + value;
			}
			
			relationshipDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v.markersymbol ilike '" + value + "'";
			}
			
			value = relationshipDomain.getCategoryKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._category_key = " + value;
			}
			
			value = relationshipDomain.getRelationshipTerm_key();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._relationshipterm_key = " + value;
			}
			
			value = relationshipDomain.getQualifierKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._qualifier_key = " + value;
			}
			
			value = relationshipDomain.getEvidenceKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._evidence_key = " + value;
			}
									
			value = relationshipDomain.getRefsKey();
			String jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._Refs_key = " + value;
			}
			else if (jnumid != null && !jnumid.isEmpty()) {
				jnumid = jnumid.toUpperCase();
				if (!jnumid.contains("J:")) {
						jnumid = "J:" + jnumid;
				}
				where = where + "\nand v.jnumid = '" + jnumid + "'";
			}
			
			if (relationshipDomain.getProperties() != null) {
				
				value = relationshipDomain.getProperties().get(0).getPropertyNameKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p._propertyname_key = " + value;
					from_property = true;
				}

				value = relationshipDomain.getProperties().get(0).getValue();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p.value ilike '" + value + "'";
					from_property = true;
				}
				
			}
			
		}
		
		if (from_property == true) {
			from = from + ", mgi_relationship_property p";
			where = where + "\nand v._relationship_key = p._relationship_key";
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info("searchCmd: " + cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimAlleleFearDomain domain = new SlimAlleleFearDomain();
				domain = slimtranslator.translate(alleleFearDAO.get(rs.getInt("_object_key_1")));
				alleleFearDAO.clear();				
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
