package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearByMarkerDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerFearDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerFearDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerFearTranslator;
import org.jax.mgi.mgd.api.model.mrk.translator.SlimMarkerFearTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class MarkerFearService extends BaseService<MarkerFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MarkerDAO markerDAO;
	
	@Inject
	private RelationshipService relationshipService;
	
	private MarkerFearTranslator translator = new MarkerFearTranslator();
	private SlimMarkerFearTranslator slimtranslator = new SlimMarkerFearTranslator();

	private String mgiTypeKey = "40";

	@Transactional
	public SearchResults<MarkerFearDomain> create(MarkerFearDomain domain, User user) {	
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerFearDomain> update(MarkerFearDomain domain, User user) {
		// translate pwi/incoming MarkerFearDomain json domain to list of RelationshipDomain
		// use this list of domain to process hibernate entities
		
		log.info("Marker FearService.update");
		
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		Marker entity = markerDAO.get(Integer.valueOf(domain.getMarkerKey()));
		List<RelationshipDomain> relationshipDomain = new ArrayList<RelationshipDomain>();		
		Boolean modified = false;

		if (domain.getClusterHasMember() != null) {
	    	// Iterate thru incoming marker fear relationship domain
			for (int i = 0; i < domain.getClusterHasMember().size(); i++) {
				
				// if processStatus == "x", then continue; no need to create domain/process anything
				if (domain.getClusterHasMember().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
					continue;
				}
				
				// if no marker, continue
				if (domain.getClusterHasMember().get(i).getMarkerKey2().isEmpty()) {
					continue;
				}
				
				RelationshipDomain rdomain = new RelationshipDomain();
			
				rdomain.setProcessStatus(domain.getClusterHasMember().get(i).getProcessStatus());
				rdomain.setRelationshipKey(domain.getClusterHasMember().get(i).getRelationshipKey());
				rdomain.setObjectKey1(domain.getClusterHasMember().get(i).getMarkerKey1());
				rdomain.setObjectKey2(domain.getClusterHasMember().get(i).getMarkerKey2());
				rdomain.setCategoryKey(domain.getClusterHasMember().get(i).getCategoryKey());
				rdomain.setRelationshipTermKey(domain.getClusterHasMember().get(i).getRelationshipTermKey());
				rdomain.setQualifierKey(domain.getClusterHasMember().get(i).getQualifierKey());
				rdomain.setEvidenceKey(domain.getClusterHasMember().get(i).getEvidenceKey());
				rdomain.setRefsKey(domain.getClusterHasMember().get(i).getRefsKey());
				rdomain.setCreatedByKey(domain.getClusterHasMember().get(i).getCreatedByKey());
				rdomain.setModifiedByKey(domain.getClusterHasMember().get(i).getModifiedByKey());
				
				// add notes to this relationship
				rdomain.setNote(domain.getClusterHasMember().get(i).getNote());
				
				// add relationshipDomain to relationshipList
				relationshipDomain.add(rdomain);         
			}
		}
		
		// process relationships
		if (relationshipDomain.size() > 0) {
			log.info("send json normalized domain to services");			
			if (relationshipService.process(relationshipDomain, mgiTypeKey, user)) {
				modified = true;
			}
		}
		
		if (modified) {
			log.info("processMarkerFear/changes processed: " + domain.getMarkerKey());
		}
		else {
			log.info("processMarkerFear/no changes processed: " + domain.getMarkerKey());
		}
				
		log.info("repackage incoming domain as results");		
		results.setItem(translator.translate(entity));		
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public MarkerFearDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		MarkerFearDomain domain = new MarkerFearDomain();
		if (markerDAO.get(key) != null) {
			domain = translator.translate(markerDAO.get(key));
		}
		markerDAO.clear();
		return domain;	
	}

	@Transactional
	public SearchResults<MarkerFearDomain> delete(Integer key, User user) {
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerFearDomain> getResults(Integer key) {
		// get the domain -> results
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		results.setItem(get(key));
		return results;
	}

	@Transactional	
	public SearchResults<MarkerFearDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		String cmd = "select count(distinct _object_key_1) as objectCount from mgi_relationship_fearbymarker_view";
		
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
	public List<SlimMarkerFearDomain> search(MarkerFearDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimMarkerFearDomain> results = new ArrayList<SlimMarkerFearDomain>();

		String cmd = "";
		String select = "select distinct m._marker_key, m.symbol";
		String from = "from mrk_marker m, acc_accession aa";		
		String where = "where m._marker_key = aa._object_key and aa._mgitype_key = 2";
		String orderBy = "order by symbol";
		
		String value;
		String cmResults[];
		String jnumid;
		
		Boolean from_cm = false;
		
		RelationshipFearByMarkerDomain relationshipDomain;

		// if parameter exists, then add to where-clause
		
		value = searchDomain.getMarkerKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand m._marker_key in (" + value + ")";
		}
		
		value = searchDomain.getMarkerSymbol();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand m.symbol ilike '" + value + "'";
		}
		
		// accession id
		value = searchDomain.getAccID();
		if (value != null && !value.isEmpty()) {
			String mgiid = value.toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(aa.accID) = '" + mgiid.toLowerCase() + "'";
		}

		// clusater_has_member
		if (searchDomain.getClusterHasMember() != null) {

			relationshipDomain = searchDomain.getClusterHasMember().get(0);
			
			cmResults = DateSQLQuery.queryByCreationModification("v1", 
				relationshipDomain.getCreatedBy(), 
				relationshipDomain.getModifiedBy(), 
				relationshipDomain.getCreation_date(), 
				relationshipDomain.getModification_date());
		
			if (cmResults.length > 0) {
				if (cmResults[0].length() > 0 || cmResults[1].length() > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
					from_cm = true;			
				}
			}
			
			value = relationshipDomain.getMarkerKey2();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1._object_key_2 = " + value;
				from_cm = true;			
			}
				
			value = relationshipDomain.getMarkerSymbol2();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1.markersymbol ilike '" + value + "'";
				from_cm = true;			
			}
	
			value = relationshipDomain.getRelationshipTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1._relationshipterm_key = " + value;
				from_cm = true;			
			}
										
			value = relationshipDomain.getRefsKey();
			jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1._Refs_key = " + value;
				from_cm = true;									
			}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
							jnumid = "J:" + jnumid;
					}
					where = where + "\nand v1.jnumid = '" + jnumid + "'";
					from_cm = true;									
			}
			
			value = relationshipDomain.getNote().getNoteChunk();
			if (value != null && !value.isEmpty()) {
				from = from + ",mgi_note n1";
				where = where + "\nand v1._relationship_key = n1._object_key"
						+ "\nand n1._mgitype_key = 40"
						+ "\nand n1._notetype_key = 1042"
						+ "\nand n1.note ilike '" + value + "'";
				from_cm = true;	
			}
			
			if (from_cm == true) {
				from = from + ",mgi_relationship_fearbymarker_view v1";						
				where = where + "\nand m._marker_key = v1._object_key_1 and v1._category_key = " + relationshipDomain.getCategoryKey();			
			}			
		}
		
		cmd = select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimMarkerFearDomain domain = new SlimMarkerFearDomain();
				domain = slimtranslator.translate(markerDAO.get(rs.getInt("_marker_key")));
				markerDAO.clear();				
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
