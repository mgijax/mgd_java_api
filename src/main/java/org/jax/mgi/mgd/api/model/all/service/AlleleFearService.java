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
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.translator.AlleleFearTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleFearTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipPropertyDomain;
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
				
				// add properties to this relationship
				rdomain.setProperties(domain.getExpressesComponents().get(i).getProperties());

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
		String alleleFrom = "from all_allele a, acc_accession aa";		
		String alleleWhere = "where a._allele_key = aa._object_key and aa._mgitype_key = 11";
		String from = "";
		String where = "";
		String orderBy = ") order by symbol";
		
		String value;
		String cmResults[];
		String jnumid;
		
		Boolean from_mi = false;
		Boolean from_ec = false;
		Boolean from_property = false;
		
		RelationshipFearDomain relationshipDomain;

		// if parameter exists, then add to where-clause
		
		value = searchDomain.getAlleleKey();
		if (value != null && !value.isEmpty()) {
			alleleWhere = alleleWhere + "\nand a._allele_key in (" + value + ")";
		}
		
		value = searchDomain.getAlleleDisplay();
		if (value != null && !value.isEmpty() && value.contains("%")) {
			alleleWhere = alleleWhere + "\nand a.symbol ilike '" + value + "'";
		}
		
		// accession id
		value = searchDomain.getAccID();
		if (value != null && !value.isEmpty()) {
			String mgiid = value.toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			alleleWhere = alleleWhere + "\nand lower(aa.accID) = '" + mgiid.toLowerCase() + "'";
		}

		// mutation involves
		
		if (searchDomain.getMutationInvolves() != null) {

			relationshipDomain = searchDomain.getMutationInvolves().get(0);
			from = "";
			where = "";
			
			cmResults = DateSQLQuery.queryByCreationModification("v", 
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
				where = where + "\nand v._object_key_2 = " + value;
				from_mi = true;			
			}
				
			value = relationshipDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v.markersymbol ilike '" + value + "'";
				from_mi = true;			
			}
	
			value = relationshipDomain.getRelationshipTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._relationshipterm_key = " + value;
				from_mi = true;			
			}
										
			value = relationshipDomain.getRefsKey();
			jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._Refs_key = " + value;
				from_mi = true;									
			}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
							jnumid = "J:" + jnumid;
					}
					where = where + "\nand v.jnumid = '" + jnumid + "'";
					from_mi = true;									
			}
			
			value = relationshipDomain.getNote().getNoteChunk();
			if (value != null && !value.isEmpty()) {
				from = from + ",mgi_note n";
				where = where + "\nand v._relationship_key = n._object_key"
						+ "\nand n._mgitype_key = 40"
						+ "\nand n._notetype_key = 1042"
						+ "\nand n.note ilike '" + value + "'";
				from_mi = true;	
			}
			
			// save search cmd for mutation involves
			if (from_mi == true) {
				from = alleleFrom + ",mgi_relationship_fear_view v" + from;						
				where = alleleWhere + "\nand a._allele_key = v._object_key_1 and v._category_key = " + relationshipDomain.getCategoryKey() + where;			
				cmd = "\n" + select + "\n" + from +"\n" + where;
			}
		}
		
		// expresses components
		
		if (searchDomain.getExpressesComponents() != null) {
		
			relationshipDomain = searchDomain.getExpressesComponents().get(0);
			from = "";
			where = "";

			cmResults = DateSQLQuery.queryByCreationModification("v", 
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
				
			value = relationshipDomain.getMarkerKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._object_key_2 = " + value;
				from_ec = true;								
			}
					
			value = relationshipDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v.markersymbol ilike '" + value + "'";
				from_ec = true;								
			}
		
			value = relationshipDomain.getRelationshipTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._relationshipterm_key = " + value;
				from_ec = true;							
			}
											
			value = relationshipDomain.getRefsKey();
			jnumid = relationshipDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand v._Refs_key = " + value;
				from_ec = true;									
			}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
							jnumid = "J:" + jnumid;
					}
					where = where + "\nand v.jnumid = '" + jnumid + "'";
					from_ec = true;									
			}	
			
			// only expresses component contains properties
			if (relationshipDomain.getProperties() != null) {
					
				value = relationshipDomain.getProperties().get(0).getPropertyNameKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p._propertyname_key = " + value;
					from_ec = true;
					from_property = true;
				}
	
				value = relationshipDomain.getProperties().get(0).getValue();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p.value ilike '" + value + "'";
					from_ec = true;
					from_property = true;
				}
			}
			
			value = relationshipDomain.getNote().getNoteChunk();
			if (value != null && !value.isEmpty()) {
				from = from + ",mgi_note n";
				where = where + "\nand v._relationship_key = n._object_key"
						+ "\nand n._mgitype_key = 40"
						+ "\nand n._notetype_key = 1042"
						+ "\nand n.note ilike '" + value + "'";
				from_ec = true;	
			}
			
			if (from_ec == true || from_property == true) {
				from = alleleFrom + ",mgi_relationship_fear_view v" + from;		
				where = alleleWhere + "\nand a._allele_key = v._object_key_1 and v._category_key = " + relationshipDomain.getCategoryKey() + where;							
			}			
		}
		
		if (from_property == true) {
			from = from + ", mgi_relationship_property p";
			where = where + "\nand v._relationship_key = p._relationship_key";
		}
		
		log.info("from_mi:" + from_mi);
		log.info("from_ec:" + from_ec);
		
		// if searching both tables, that add "union" + expresses component part
		if (from_mi == true && from_ec == true) {
			cmd = cmd + "\nunion\n" + select + "\n" + from + "\n" + where;
		}
		else if (from_ec == true) {
			cmd = select + "\n" + from + "\n" + where;			
		}
		else if (from_mi == false){
			cmd = select + "\n" + alleleFrom + "\n" + alleleWhere;
		}
		
		cmd = "\n(" + cmd + "\n" + orderBy;
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
	public List<RelationshipPropertyDomain> searchPropertyAccId(RelationshipPropertyDomain searchDomain) {
		// using propertyName/propertyNameKey Acc ID, search & return 
		
		List<RelationshipPropertyDomain> results = new ArrayList<RelationshipPropertyDomain>();

		//  12948292 | Non-mouse_NCBI_Gene_ID
		//	100655557 | Non-mouse_HGNC_Gene_ID
		//	100655558 | Non-mouse_RGD_Gene_ID
		//	100655559 | Non-mouse_ZFIN_Gene_ID
	
		String ncbi = "12948292";
		String hgnc = "100655557";
		String rgd = "100655558";
		String zfin = "100655559";
		String organism = "12948290";
		String symbol = "12948291";
		
		String ldbKey1;
		String ldbKey2;
		String organismKey;
		
		// NCBI is used by > 1 organism; but the NCBI ids are unique
		// a search by NCBI id should still return at most one symbol
		if (searchDomain.getPropertyNameKey().equals(ncbi)) {
			ldbKey1 = "55";
			ldbKey2 = "47,64,172";
			organismKey = "2,10,11,13,40,63,84,94,95";
		}
		// a search by HGNC id may also return an NCBI id
		else if (searchDomain.getPropertyNameKey().equals(hgnc)) {
			ldbKey1 = "64";
			ldbKey2 = "55";
			organismKey = "2";
		}	
		// a search by RGD id may also return an NCBI id
		else if (searchDomain.getPropertyNameKey().equals(rgd)) {
			ldbKey1 = "47";
			ldbKey2 = "55";
			organismKey = "40";
		}
		// a search by ZFIN id may also return an NCBI id
		else if (searchDomain.getPropertyNameKey().equals(zfin)) {
			ldbKey1 = "172";
			ldbKey2 = "55";			
			organismKey = "84";
		}		
		else {
			return results;
		}
		
		String cmd = "\nselect m.symbol, " + symbol + " as propertyName, 1 as orderBy"
				+ "\nfrom acc_accession a, mrk_marker m"
				+ "\nwhere a.accid = '" + searchDomain.getValue() + "'"
				+ "\nand a._logicaldb_key = " + ldbKey1 + ")"					
				+ "\nand a._object_key = m._marker_key"
				+ "\nand m._organism_key in (" + organismKey + ")"
				+ "\nunion"
				+ "\nselect o.commonname as value, " + organism + ", 2 as orderBy"
				+ "\nfrom acc_accession a, mrk_marker m, mgi_organism o"
				+ "\nwhere a.accid = '" + searchDomain.getValue() + "'"
				+ "\nand a._logicaldb_key = " + ldbKey1
				+ "\nd a._object_key = m._marker_key"
				+ "\nand m._organism_key = o._organism_key"
				+ "\nand m._organism_key in (" + organismKey + ")"						
				+ "\nunion"
				+ "\nselect aa.accid as value, aa._logicaldb_key, 3 as orderBy"
				+ "\nfrom acc_accession a, acc_accession aa"
				+ "\nwhere a.accid = '" + searchDomain.getValue() + ","
				+ "\nand a._logicaldb_key = " + ldbKey1
				+ "\nand a._object_key = aa._object_key"
				+ "\nand aa._logicaldb_key = " + ldbKey2
				+ "\norder by orderBy";

		log.info("cmd: " + cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				RelationshipPropertyDomain domain = new RelationshipPropertyDomain();
				domain.setProcessStatus(Constants.PROCESS_CREATE);
				domain.setRelationshipKey(searchDomain.getRelationshipKey());
				domain.setValue(rs.getString("value"));
				
				if (rs.getInt("propertyNameKey") == 47) {
					domain.setPropertyNameKey(rgd);
				}
				else if (rs.getInt("propertyNameKey") == 55) {
					domain.setPropertyNameKey(ncbi);
				}
				else if (rs.getInt("propertyNameKey") == 64) {
					domain.setPropertyNameKey(hgnc);
				}
				else if (rs.getInt("propertyNameKey") == 172) {
					domain.setPropertyNameKey(zfin);
				}				
				else {
					domain.setPropertyNameKey(String.valueOf(rs.getInt("propertyNameKey")));	
				}
				
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
