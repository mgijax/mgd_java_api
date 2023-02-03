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
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFearRegionDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.translator.AlleleFearTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleFearTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.SlimMarkerTranslator;
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
	private MarkerDAO markerDAO;
	
	@Inject
	private RelationshipService relationshipService;
	
	private AlleleFearTranslator translator = new AlleleFearTranslator();
	private SlimAlleleFearTranslator slimtranslator = new SlimAlleleFearTranslator();
	private SlimMarkerTranslator slimmarkertranslator = new SlimMarkerTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "40";

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
		
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		Allele entity = alleleFearDAO.get(Integer.valueOf(domain.getAlleleKey()));
		List<RelationshipDomain> relationshipDomain = new ArrayList<RelationshipDomain>();		
		Boolean modified = false;

		if (domain.getMutationInvolves() != null) {
	    	// Iterate thru incoming allele fear relationship domain
			for (int i = 0; i < domain.getMutationInvolves().size(); i++) {
				
				// if processStatus == "x", then continue; no need to create domain/process anything
				if (domain.getMutationInvolves().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
					continue;
				}
				
				// if no marker, continue
				if (domain.getMutationInvolves().get(i).getMarkerKey().isEmpty()) {
					continue;
				}
				
				RelationshipDomain rdomain = new RelationshipDomain();
			
				rdomain.setProcessStatus(domain.getMutationInvolves().get(i).getProcessStatus());
				rdomain.setRelationshipKey(domain.getMutationInvolves().get(i).getRelationshipKey());
				rdomain.setObjectKey1(domain.getAlleleKey());
				rdomain.setObjectKey2(domain.getMutationInvolves().get(i).getMarkerKey());
				rdomain.setCategoryKey(domain.getMutationInvolves().get(i).getCategoryKey());
				rdomain.setRelationshipTermKey(domain.getMutationInvolves().get(i).getRelationshipTermKey());
				rdomain.setQualifierKey(domain.getMutationInvolves().get(i).getQualifierKey());
				rdomain.setEvidenceKey(domain.getMutationInvolves().get(i).getEvidenceKey());
				rdomain.setRefsKey(domain.getMutationInvolves().get(i).getRefsKey());
				rdomain.setCreatedByKey(domain.getMutationInvolves().get(i).getCreatedByKey());
				rdomain.setModifiedByKey(domain.getMutationInvolves().get(i).getModifiedByKey());
				
				// add notes to this relationship
				rdomain.setNote(domain.getMutationInvolves().get(i).getNote());
				
				// add relationshipDomain to relationshipList
				relationshipDomain.add(rdomain);         
			}
		}

		if (domain.getExpressesComponents() != null) {
	    	// Iterate thru incoming allele Fear relationship domain
			for (int i = 0; i < domain.getExpressesComponents().size(); i++) {
				
				// if processStatus == "x", then continue; no need to create domain/process anything
				if (domain.getExpressesComponents().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
					continue;
				}
				
				RelationshipDomain rdomain = new RelationshipDomain();
			
				rdomain.setProcessStatus(domain.getExpressesComponents().get(i).getProcessStatus());
				rdomain.setRelationshipKey(domain.getExpressesComponents().get(i).getRelationshipKey());
				rdomain.setObjectKey1(domain.getAlleleKey());
				rdomain.setObjectKey2(domain.getExpressesComponents().get(i).getMarkerKey());				
				rdomain.setCategoryKey(domain.getExpressesComponents().get(i).getCategoryKey());
				rdomain.setRelationshipTermKey(domain.getExpressesComponents().get(i).getRelationshipTermKey());
				rdomain.setQualifierKey(domain.getExpressesComponents().get(i).getQualifierKey());
				rdomain.setEvidenceKey(domain.getExpressesComponents().get(i).getEvidenceKey());
				rdomain.setRefsKey(domain.getExpressesComponents().get(i).getRefsKey());
				
				rdomain.setCreatedByKey(domain.getExpressesComponents().get(i).getCreatedByKey());
				rdomain.setModifiedByKey(domain.getExpressesComponents().get(i).getModifiedByKey());
				
				// add notes to this relationship
				rdomain.setNote(domain.getExpressesComponents().get(i).getNote());
				
//				// add properties to this relationship
//				rdomain.setProperties(domain.getExpressesComponents().get(i).getProperties());

				// add relationshipDomain to relationshipList
				relationshipDomain.add(rdomain);         
			}
		}
		
		if (domain.getDriverComponents() != null) {
	    	// Iterate thru incoming allele fear relationship domain
			for (int i = 0; i < domain.getDriverComponents().size(); i++) {
				
				// if processStatus == "x", then continue; no need to create domain/process anything
				if (domain.getDriverComponents().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
					continue;
				}
				
				// if no marker, continue
				if (domain.getDriverComponents().get(i).getMarkerKey().isEmpty()) {
					continue;
				}
				
				RelationshipDomain rdomain = new RelationshipDomain();
			
				rdomain.setProcessStatus(domain.getDriverComponents().get(i).getProcessStatus());
				rdomain.setRelationshipKey(domain.getDriverComponents().get(i).getRelationshipKey());
				rdomain.setObjectKey1(domain.getAlleleKey());
				rdomain.setObjectKey2(domain.getDriverComponents().get(i).getMarkerKey());
				rdomain.setCategoryKey(domain.getDriverComponents().get(i).getCategoryKey());
				rdomain.setRelationshipTermKey(domain.getDriverComponents().get(i).getRelationshipTermKey());
				rdomain.setQualifierKey(domain.getDriverComponents().get(i).getQualifierKey());
				rdomain.setEvidenceKey(domain.getDriverComponents().get(i).getEvidenceKey());
				rdomain.setRefsKey(domain.getDriverComponents().get(i).getRefsKey());
				rdomain.setCreatedByKey(domain.getDriverComponents().get(i).getCreatedByKey());
				rdomain.setModifiedByKey(domain.getDriverComponents().get(i).getModifiedByKey());
				
				// add notes to this relationship
				rdomain.setNote(domain.getDriverComponents().get(i).getNote());
				
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
			log.info("processAlleleFear/changes processed: " + domain.getAlleleKey());
		}
		else {
			log.info("processAlleleFear/no changes processed: " + domain.getAlleleKey());
		}
				
		log.info("repackage incoming domain as results");		
		results.setItem(translator.translate(entity));		
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
		String cmd = "select count(distinct _object_key_1) as objectCount from mgi_relationship_fear_view";
		
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
		String select = "select distinct a._allele_key, a.symbol";
		String from = "from all_allele a, acc_accession aa";		
		String where = "where a.isWildType = 0 and a._allele_key = aa._object_key and aa._mgitype_key = 11";
		String orderBy = "order by symbol";
		
		String value;
		String cmResults[];
		String jnumid;
		
		Boolean from_mi = false;
		Boolean from_ec = false;
		Boolean from_dc = false;
		
		RelationshipFearDomain relationshipDomain;

		// if parameter exists, then add to where-clause
		
		value = searchDomain.getAlleleKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a._allele_key in (" + value + ")";
		}
		
		value = searchDomain.getAlleleSymbol();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a.symbol ilike '" + value + "'";
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

		// mutation involves
		if (searchDomain.getMutationInvolves() != null) {

			relationshipDomain = searchDomain.getMutationInvolves().get(0);
			
			cmResults = DateSQLQuery.queryByCreationModification("v1", 
				relationshipDomain.getCreatedBy(), 
				relationshipDomain.getModifiedBy(), 
				relationshipDomain.getCreation_date(), 
				relationshipDomain.getModification_date());
		
			if (cmResults.length > 0) {
				if (cmResults[0].length() > 0 || cmResults[1].length() > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
					from_mi = true;			
				}
			}
			
			value = relationshipDomain.getMarkerKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1._object_key_2 = " + value;
				from_mi = true;			
			}
				
			value = relationshipDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1.markersymbol ilike '" + value + "'";
				from_mi = true;			
			}
	
			value = relationshipDomain.getRelationshipTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1._relationshipterm_key = " + value;
				from_mi = true;			
			}
										
			value = relationshipDomain.getRefsKey();
			jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v1._Refs_key = " + value;
				from_mi = true;									
			}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
							jnumid = "J:" + jnumid;
					}
					where = where + "\nand v1.jnumid = '" + jnumid + "'";
					from_mi = true;									
			}
			
			value = relationshipDomain.getNote().getNoteChunk();
			if (value != null && !value.isEmpty()) {
				from = from + ",mgi_note n1";
				where = where + "\nand v1._relationship_key = n1._object_key"
						+ "\nand n1._mgitype_key = 40"
						+ "\nand n1._notetype_key = 1042"
						+ "\nand n1.note ilike '" + value + "'";
				from_mi = true;	
			}
			
			if (from_mi == true) {
				from = from + ",mgi_relationship_fear_view v1";						
				where = where + "\nand a._allele_key = v1._object_key_1 and v1._category_key = " + relationshipDomain.getCategoryKey();			
			}			
		}
		
		// expresses components
		if (searchDomain.getExpressesComponents() != null) {
		
			relationshipDomain = searchDomain.getExpressesComponents().get(0);

			cmResults = DateSQLQuery.queryByCreationModification("v2", 
					relationshipDomain.getCreatedBy(), 
					relationshipDomain.getModifiedBy(), 
					relationshipDomain.getCreation_date(), 
					relationshipDomain.getModification_date());
			
			if (cmResults.length > 0) {
				if (cmResults[0].length() > 0 || cmResults[1].length() > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
					from_ec = true;			
				}
			}
				
			value = relationshipDomain.getOrganismKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v2._organism_key = " + value;
				from_ec = true;								
			}
			
			value = relationshipDomain.getMarkerKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v2._object_key_2 = " + value;
				from_ec = true;								
			}
					
			value = relationshipDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v2.markersymbol ilike '" + value + "'";
				from_ec = true;								
			}
		
			value = relationshipDomain.getRelationshipTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v2._relationshipterm_key = " + value;
				from_ec = true;							
			}
											
			value = relationshipDomain.getRefsKey();
			jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v2._Refs_key = " + value;
				from_ec = true;									
			}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
							jnumid = "J:" + jnumid;
					}
					where = where + "\nand v2.jnumid = '" + jnumid + "'";
					from_ec = true;									
			}
			
			value = relationshipDomain.getNote().getNoteChunk();
			if (value != null && !value.isEmpty()) {
				from = from + ",mgi_note n2";
				where = where + "\nand v2._relationship_key = n2._object_key"
						+ "\nand n2._mgitype_key = 40"
						+ "\nand n2._notetype_key = 1042"
						+ "\nand n2.note ilike '" + value + "'";
				from_ec = true;	
			}
			
			if (from_ec == true) {				
				from = from + ",mgi_relationship_fear_view v2";		
				where = where + "\nand a._allele_key = v2._object_key_1 and v2._category_key = " + relationshipDomain.getCategoryKey();						
			}	
		}
		
		// driver components
		if (searchDomain.getDriverComponents() != null) {

			relationshipDomain = searchDomain.getDriverComponents().get(0);
			
			cmResults = DateSQLQuery.queryByCreationModification("v3", 
				relationshipDomain.getCreatedBy(), 
				relationshipDomain.getModifiedBy(), 
				relationshipDomain.getCreation_date(), 
				relationshipDomain.getModification_date());
		
			if (cmResults.length > 0) {
				if (cmResults[0].length() > 0 || cmResults[1].length() > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
					from_dc = true;			
				}
			}
			
			value = relationshipDomain.getOrganismKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v3._organism_key = " + value;
				from_dc = true;								
			}
		
			value = relationshipDomain.getMarkerKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v3._object_key_2 = " + value;
				from_dc = true;			
			}
				
			value = relationshipDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v3.markersymbol ilike '" + value + "'";
				from_dc = true;			
			}
	
			// always allele_to_driver_gene
//			value = relationshipDomain.getRelationshipTermKey();
//			if (value != null && !value.isEmpty()) {
//				where = where + "\nand v3._relationshipterm_key = " + value;
//				from_dc = true;			
//			}
										
			value = relationshipDomain.getRefsKey();
			jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v3._Refs_key = " + value;
				from_dc = true;									
			}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
							jnumid = "J:" + jnumid;
					}
					where = where + "\nand v3.jnumid = '" + jnumid + "'";
					from_mi = true;									
			}
			
			value = relationshipDomain.getNote().getNoteChunk();
			if (value != null && !value.isEmpty()) {
				from = from + ",mgi_note n3";
				where = where + "\nand v3._relationship_key = n3._object_key"
						+ "\nand n3._mgitype_key = 40"
						+ "\nand n3._notetype_key = 1042"
						+ "\nand n3.note ilike '" + value + "'";
				from_dc = true;	
			}
			
			// exclude allele = Recombinase
			if (from_dc == true) {
				from = from + ",mgi_relationship_fear_view v3";						
				where = where + "\nand a._allele_key = v3._object_key_1 and v3._category_key = " + relationshipDomain.getCategoryKey();	
				where = where +
						"\nand not exists (select 1 from voc_annot vr" +
						        "\nwhere v3._object_key_1 = vr._object_key" +
						        "\nand vr._annottype_key = 1014" +
						        "\nand vr._term_key = 11025588)";
			}
		}
		
		cmd = select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimAlleleFearDomain domain = new SlimAlleleFearDomain();
				domain = slimtranslator.translate(alleleFearDAO.get(rs.getInt("_allele_key")));
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

	@Transactional	
	public List<SlimMarkerDomain> getMarkerByRegion(SlimAlleleFearRegionDomain searchDomain) {
		// returns SlimMarkerDomain (list of Markers) which match SQL criteria/parameters
		//
		// criteria/parameters:
		//
		// allele
		// chromosome
		//
		// with overlapping coordinates SQL logic
		// db startCoordinate <= endCoordinate parameter
		// db endCoordinate >= startCoordiate parameter
		//
		// relationship type
		// relationship category = MI/mutation involves (1003)
		//
		// allele does not contain an existing MI relationship with marker/relationship type
		//
		// mrk_mcv_cache contains:
		//		protein coding gene
		//		non-coding RNA gene
		//		unclassified gene
		//		gene segment
		//		pseudogenic region
		//
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();
		
		String cmd = "\nselect m._marker_key, m.chromosome, mm.symbol" +
				"\nfrom mrk_location_cache m, mrk_marker mm, mrk_mcv_cache c" +
				"\nwhere m._marker_key = mm._marker_key" +
				"\nand m._marker_key = c._marker_key" +
				"\nand c._mcvterm_key in (6238171, 6238162,6238161,7288448,6238184)" + 
				"\nand m.chromosome = '" + searchDomain.getChromosome() + "'" + 
				"\nand m.startCoordinate <= " + searchDomain.getEndCoordinate() +
				"\nand m.endCoordinate >= " + searchDomain.getStartCoordinate() +
				"\nand not exists (select 1 from mgi_relationship p" +
				"\nwhere p._category_key in (1003)" +
				"\nand p._object_key_1 = " + searchDomain.getAlleleKey() +
				"\nand m._marker_key = p._object_key_2" +				
				"\nand p._relationshipterm_key = " + searchDomain.getRelationshipTermKey() + ")" +
				"\norder by m.chromosome, mm.symbol";
		log.info("cmd: " + cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimMarkerDomain domain = new SlimMarkerDomain();
				domain = slimmarkertranslator.translate(markerDAO.get(rs.getInt("_marker_key")));
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
