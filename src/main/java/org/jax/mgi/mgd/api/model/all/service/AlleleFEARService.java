package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFEARDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFEARDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleFEARTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleFEARTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFEARDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleFEARService extends BaseService<AlleleFEARDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AlleleDAO allelefearDAO;
	@Inject
	private RelationshipService relationshipService;
	
	private AlleleFEARTranslator translator = new AlleleFEARTranslator();
	private SlimAlleleFEARTranslator slimtranslator = new SlimAlleleFEARTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "11";

	@Transactional
	public SearchResults<AlleleFEARDomain> create(AlleleFEARDomain domain, User user) {	
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<AlleleFEARDomain> update(AlleleFEARDomain domain, User user) {
		// translate pwi/incoming AlleleFEARDomain json domain to list of RelationshipDomain
		// use this list of domain to process hibernate entities
		
		log.info("AlleleFEARService.update");
		
		List<RelationshipDomain> relationshipList = new ArrayList<RelationshipDomain>();
		Boolean modified = false;

    	// Iterate thru incoming allele FEAR relationship domain
		for (int i = 0; i < domain.getRelationships().size(); i++) {
			
			// if processStatus == "x", then continue; no need to create domain/process anything
			if (domain.getRelationships().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
				continue;
			}
			
			RelationshipDomain relationshipDomain = new RelationshipDomain();
		
			relationshipDomain.setProcessStatus(domain.getRelationships().get(i).getProcessStatus());
			relationshipDomain.setRelationshipKey(domain.getRelationships().get(i).getRelationshipKey());
			relationshipDomain.setCategoryKey(domain.getRelationships().get(i).getCategoryKey());
			relationshipDomain.setObject1(domain.getRelationships().get(i).getAlleleKey());
			relationshipDomain.setObject2(domain.getRelationships().get(i).getMarkerKey());
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
			log.info("processAlleleFEAR/changes processed: " + domain.getAlleleKey());
		}
		else {
			log.info("processAlleleFEAR/no changes processed: " + domain.getAlleleKey());
		}
		
		log.info("repackage incoming domain as results");		
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results = getResults(Integer.valueOf(domain.getAlleleKey()));
		results.setItem(domain);
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public AlleleFEARDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		AlleleFEARDomain domain = new AlleleFEARDomain();
		if (allelefearDAO.get(key) != null) {
			domain = translator.translate(allelefearDAO.get(key));
		}
		allelefearDAO.clear();
		return domain;	
	}

	@Transactional
	public SearchResults<AlleleFEARDomain> delete(Integer key, User user) {
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<AlleleFEARDomain> getResults(Integer key) {
		// get the domain -> results
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results.setItem(get(key));
		return results;
	}

	@Transactional	
	public SearchResults<AlleleFEARDomain> getObjectCount(Integer key) {
		// return the object count from the database
		
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		String cmd = "select count(*) as objectCount from mgi_relationship_fear_view where _object_key_1 = " + key;
		
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
	public List<SlimAlleleFEARDomain> search(AlleleFEARDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimAlleleFEARDomain> results = new ArrayList<SlimAlleleFEARDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 

		String cmd = "";
		String select = "select distinct v._object_key_1, v.allelesymbol, v.markersymbol";
		String from = "from mgi_relationship_fear_view v";		
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
			where = where + "\nand v.allelesymbol ilike '" + searchDomain.getSymbol() + "'";
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
						
			RelationshipFEARDomain relationshipDomain = searchDomain.getRelationships().get(0);
		
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
				SlimAlleleFEARDomain domain = new SlimAlleleFEARDomain();
				domain = slimtranslator.translate(allelefearDAO.get(rs.getInt("_object_key_1")));
				allelefearDAO.clear();				
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
