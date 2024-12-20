package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceCitationCacheDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.RelationshipCategoryDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.RelationshipDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.RelationshipFearDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearByAlleleDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipFearByAlleleTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class RelationshipService extends BaseService<RelationshipDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private RelationshipDAO relationshipDAO;
	@Inject
	private RelationshipFearDAO relationshipFearDAO;
	@Inject
	private RelationshipCategoryDAO categoryDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private ReferenceCitationCacheDAO referenceDAO;
	@Inject
	private RelationshipPropertyService relationshipPropertyService;
	@Inject
	private NoteService noteService;
	
	private RelationshipTranslator translator = new RelationshipTranslator();

	@Transactional
	public SearchResults<RelationshipDomain> create(RelationshipDomain object, User user) {
		SearchResults<RelationshipDomain> results = new SearchResults<RelationshipDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<RelationshipDomain> update(RelationshipDomain object, User user) {
		SearchResults<RelationshipDomain> results = new SearchResults<RelationshipDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<RelationshipDomain> delete(Integer key, User user) {
		SearchResults<RelationshipDomain> results = new SearchResults<RelationshipDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public RelationshipDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		RelationshipDomain domain = new RelationshipDomain();
		if (relationshipDAO.get(key) != null) {
			domain = translator.translate(relationshipDAO.get(key));
		}
		relationshipDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<RelationshipDomain> getResults(Integer key) {
        SearchResults<RelationshipDomain> results = new SearchResults<RelationshipDomain>();
        results.setItem(translator.translate(relationshipDAO.get(key)));
        relationshipDAO.clear();
        return results;
    }
    
	@Transactional	
	public List<RelationshipDomain> getMarkerTSS(Integer key) {
		// return all tss-marker relationships by specified marker key
		// 1008 | tss_to_gene
		
		RelationshipTranslator translator = new RelationshipTranslator();
		List<RelationshipDomain> results = new ArrayList<RelationshipDomain>();
		
		String cmd = "select _relationship_key from mgi_relationship_markertss_view "
				+ "\nwhere _object_key_1 = " + key
				+ "\nor _object_key_2 = " + key;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				RelationshipDomain domain = new RelationshipDomain();
				domain = translator.translate(relationshipDAO.get(rs.getInt("_relationship_key")));
				relationshipDAO.clear();
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
	public List<RelationshipFearByAlleleDomain> getAlleleFear(Integer key) {
		// return all allele-marker relationships by specified allele key
		// see MGI_Relationship_FEAR_View:
		// 			1003 | mutation_involves
		// 			1004 | expresses_component
		//			1006 | driver_component
		
		RelationshipFearByAlleleTranslator translator = new RelationshipFearByAlleleTranslator();
		List<RelationshipFearByAlleleDomain> results = new ArrayList<RelationshipFearByAlleleDomain>();
		
		String cmd = "select distinct _relationship_key from mgi_relationship_fear_view "
				+ "\nwhere _object_key_1 = " + key;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				RelationshipFearByAlleleDomain domain = new RelationshipFearByAlleleDomain();
				domain = translator.translate(relationshipFearDAO.get(rs.getInt("_relationship_key")));
				relationshipFearDAO.clear();
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
	public Boolean process(List<RelationshipDomain> domain, String mgiTypeKey, User user) {
		// process relationships (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processRelationships/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
				log.info("processRelationships create");				
				
				// if reference is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getRefsKey().isEmpty()) {
					continue;
				}
				
				Relationship entity = new Relationship();			
		        entity.setCategory(categoryDAO.get(Integer.valueOf(domain.get(i).getCategoryKey())));
		        entity.set_object_key_1(Integer.valueOf(domain.get(i).getObjectKey1()));
		        entity.set_object_key_2(Integer.valueOf(domain.get(i).getObjectKey2()));
		        entity.setRelationshipTerm(termDAO.get(Integer.valueOf(domain.get(i).getRelationshipTermKey())));
		        entity.setQualifierTerm(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
		        entity.setEvidenceTerm(termDAO.get(Integer.valueOf(domain.get(i).getEvidenceKey())));
		        entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));				
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);
		        entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				relationshipDAO.persist(entity);
				
				if (domain.get(i).getProperties() != null) {
					relationshipPropertyService.process(domain.get(i).getProperties(), String.valueOf(entity.get_relationship_key()), user);
				}
				
				noteService.process(String.valueOf(entity.get_relationship_key()), domain.get(i).getNote(), mgiTypeKey, user);
				
				modified = true;
				log.info("processRelationships create successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processRelationships delete");
				Relationship entity = relationshipDAO.get(Integer.valueOf(domain.get(i).getRelationshipKey()));
				relationshipDAO.remove(entity);
				modified = true;
				log.info("processRelationships delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
				log.info("processRelationships update");			
				Relationship entity = relationshipDAO.get(Integer.valueOf(domain.get(i).getRelationshipKey()));
				entity.setCategory(categoryDAO.get(Integer.valueOf(domain.get(i).getCategoryKey())));
				entity.set_object_key_1(Integer.valueOf(domain.get(i).getObjectKey1()));
				entity.set_object_key_2(Integer.valueOf(domain.get(i).getObjectKey2()));
				entity.setRelationshipTerm(termDAO.get(Integer.valueOf(domain.get(i).getRelationshipTermKey())));
				entity.setQualifierTerm(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				entity.setEvidenceTerm(termDAO.get(Integer.valueOf(domain.get(i).getEvidenceKey())));
				entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				relationshipDAO.update(entity);
				
				if (domain.get(i).getProperties() != null) {
					relationshipPropertyService.process(domain.get(i).getProperties(), domain.get(i).getRelationshipKey(), user);
				}
				
				noteService.process(domain.get(i).getRelationshipKey(), domain.get(i).getNote(), mgiTypeKey, user);
				
				modified = true;
				log.info("processRelationships/changes processed: " + domain.get(i).getRelationshipKey());
			}
			else {
				log.info("processRelationships/no changes processed: " + domain.get(i).getRelationshipKey());
			}
		}
		
		log.info("processRelationships/processing successful");
		return modified;
	}
	
}
