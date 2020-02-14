package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationTypeDAO;
import org.jax.mgi.mgd.api.model.voc.dao.EvidenceDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;
import org.jax.mgi.mgd.api.model.voc.translator.AlleleVariantAnnotationTranslator;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jax.mgi.mgd.api.model.voc.translator.MarkerFeatureTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AnnotationService extends BaseService<AnnotationDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AnnotationDAO annotationDAO;
	@Inject
	private EvidenceDAO evidenceDAO;
	@Inject
	private AnnotationTypeDAO annotTypeDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private EvidenceService evidenceService;
	
	private AnnotationTranslator translator = new AnnotationTranslator();
	private AlleleVariantAnnotationTranslator alleleVariantTranslator = new AlleleVariantAnnotationTranslator();	
	private MarkerFeatureTypeTranslator markerFeatureTypeTranslator = new MarkerFeatureTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	String mgiTypeKey = "25";
	
	@Transactional
	public SearchResults<AnnotationDomain> create(AnnotationDomain object, User user) {
		SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AnnotationDomain> update(AnnotationDomain object, User user) {
		SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<AnnotationDomain> delete(Integer key, User user) {
		SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AnnotationDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AnnotationDomain domain = new AnnotationDomain();
		if (annotationDAO.get(key) != null) {
			domain = translator.translate(annotationDAO.get(key));
		}
		annotationDAO.clear();
		return domain;		
	}
	
    @Transactional
    public SearchResults<AnnotationDomain> getResults(Integer key) {
        SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
        results.setItem(translator.translate(annotationDAO.get(key)));
		annotationDAO.clear();
        return results;
    }

	@Transactional
	private List<AnnotationDomain> getAnnotationDomainList(String cmd) {
		//
		// get list of annotation domains by using sqlExecutor
		//
		
		// list of results to be returned
		List<AnnotationDomain> results = new ArrayList<AnnotationDomain>();

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AnnotationDomain domain = new AnnotationDomain();
				domain = translator.translate(annotationDAO.get(rs.getInt("_annot_key")));
				annotationDAO.clear();				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
	
	@Transactional	
	public List<MarkerFeatureTypeDomain> getMarkerFeatureTypes(Integer key) {
		// list of marker/feature type domains for given marker
		
		List<MarkerFeatureTypeDomain> results = new ArrayList<MarkerFeatureTypeDomain>();
		
		String cmd = "\nselect t._term_key, t.term, a.*"
				+ "\nfrom VOC_Annot v, VOC_Term t, ACC_Accession a"
				+ "\nwhere v._annottype_key = 1011"
				+ "\nand v._term_key = t._term_key"
				+ "\nand v._term_key = a._object_key"
				+ "\nand a._mgitype_key = 13"
				+ "\nand a._logicaldb_key = 146"
				+ "\nand v._object_key = " + key;		
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerFeatureTypeDomain domain = new MarkerFeatureTypeDomain();
				domain = markerFeatureTypeTranslator.translate(annotationDAO.get(rs.getInt("_annot_key")));
				annotationDAO.clear();				
				results.add(domain);
			}
			sqlExecutor.cleanup();		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
	
	@Transactional	
	public List<AlleleVariantAnnotationDomain> getAlleleVariantAnnotations(Integer key, String annotTypeKey) {
		// list of allele variant type domains for given variant
		
		// if we need to include the voc_evidence info
		// then add the voc_evidence info into the AlleleVariantVocDomain
		// and then add the voc_evidence info into the SQL cmd string
		// and add to ResultSet
		
		List<AlleleVariantAnnotationDomain> results = new ArrayList<AlleleVariantAnnotationDomain>();
		
		String cmd = "\nselect v._annot_key, t._term_key, t.term, a.*"
				+ "\nfrom VOC_Annot v, VOC_Term t, ACC_Accession a"
				+ "\nwhere v._annottype_key = " + annotTypeKey
				+ "\nand v._term_key = t._term_key"
				+ "\nand v._term_key = a._object_key"
				+ "\nand a._mgitype_key = 13"
				+ "\nand a._logicaldb_key = 145"
				+ "\nand v._object_key = " + key;		
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AlleleVariantAnnotationDomain domain = new AlleleVariantAnnotationDomain();
				domain = alleleVariantTranslator.translate(annotationDAO.get(rs.getInt("_annot_key")));
				annotationDAO.clear();				
				results.add(domain);
			}
			sqlExecutor.cleanup();		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}

	@Transactional
	public Boolean process(List<AnnotationDomain> domain, User user) {

		// process annotation associations (create, delete, update)
		
		// Get these two values up front for MP annotations, to call SP later
		String objectKey = domain.get(0).getObjectKey();
		String annotTypeKey = domain.get(0).getAnnotTypeKey();
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAnnotation/nothing to process");
			return modified;
		}
		
		log.info("processAnnotation");
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			String termKey = domain.get(i).getTermKey();		
			String qualifierKey = domain.get(i).getQualifierKey();

			// if the termKey, qualifierKey are ALL null
			// then simply skip (continue) because the pwi will be sending in a json
			// string with some empty annotations due to the set number of empty rows
			// displayed in the module
			// NOTE: The PWI automatically fills in annotTypeKey, objectKey even in the 
			// blank rows
			if (termKey.isEmpty() && qualifierKey.isEmpty()) {
				continue;
			}
			
			log.info("processAnnotation.getProcessStatus(): " + domain.get(i).getProcessStatus());

			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				log.info("processAnnotation create");

				// create new entity object from in-coming domain
				// the Entities class handles the generation of the primary key
				// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

				// voc_annot
				Annotation entity = new Annotation();				
				
				// set default qualifiers				
				log.info("processAnnotation/qualifier");
				if (annotTypeKey.equals("1002") ) {		
					if( qualifierKey == null || qualifierKey.isEmpty()) {
						log.info("setting default genotype/MP qualifier to 'null'");
					    qualifierKey = "2181423";
					}
				}
				else if (annotTypeKey.equals("1020") ) {
					if( qualifierKey == null || qualifierKey.isEmpty()) {
						log.info("setting default genotype/DO qualifier to 'null' " );
					    qualifierKey = "1614158";
					}
				}
				else if (annotTypeKey.equals("1021") ) {
					if( qualifierKey == null || qualifierKey.isEmpty()) {
						log.info("setting default allele/DO qualifier to 'null'" );
					    qualifierKey = "8068250";
					}
				}
				else if (annotTypeKey.equals("1000") ) {
					if( qualifierKey == null || qualifierKey.isEmpty()) {
						log.info("setting default marker/GO qualifier to 'null'" );
					    qualifierKey = "1614156";
					}
				}
				
				entity.setAnnotType(annotTypeDAO.get(Integer.valueOf(annotTypeKey)));				
				entity.set_object_key(Integer.valueOf(domain.get(i).getObjectKey()));
				entity.setTerm(termDAO.get(Integer.valueOf(domain.get(i).getTermKey())));
				entity.setQualifier(termDAO.get(Integer.valueOf(qualifierKey)));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				log.info("AnnotationService persisting Annotation");
				annotationDAO.persist(entity);
		
				// process evidence
				if (domain.get(i).getEvidence() != null) {					
					if (evidenceService.process(String.valueOf(entity.get_annot_key()), domain.get(i).getEvidence(), annotTypeKey, mgiTypeKey, user)) {
						modified = true;
					}
				}
				
				// add is always true
				modified = true;
				log.info("processAnnotation/create processed");				
			}
			
			// for splitting annotations that term and/or qualifier has changed
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_SPLIT)) {
				
				log.info("processAnnotation split");

				// create new annotation
				// set voc_evidence._annot_key to new _annot_key
				// possibly delete voc_annot (trigger handles this)
				
				//  new voc_annot
				Annotation entity = new Annotation();				
				entity.setAnnotType(annotTypeDAO.get(Integer.valueOf(annotTypeKey)));				
				entity.set_object_key(Integer.valueOf(domain.get(i).getObjectKey()));
				entity.setTerm(termDAO.get(Integer.valueOf(domain.get(i).getTermKey())));
				entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				annotationDAO.persist(entity);
				
				// get existing evidence domain
				// set evidence._annot_key = new entity key
				if (domain.get(i).getEvidence() != null) {
					List<EvidenceDomain> evidenceList = domain.get(i).getEvidence();
					for (int j = 0; j < evidenceList.size(); j++) {
						EvidenceDomain evidenceDomain = evidenceList.get(j);
						Evidence evidenceEntity = evidenceDAO.get(Integer.valueOf(evidenceDomain.getAnnotEvidenceKey()));
						evidenceEntity.set_annot_key(entity.get_annot_key());
						evidenceEntity.setModification_date(new Date());
						evidenceEntity.setModifiedBy(user);	
						evidenceDAO.update(evidenceEntity);
					}					 
					annotationDAO.update(entity);
				}
				
				modified = true;
				log.info("processAnnotation/split processed");				
			}			
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAnnotation delete using key: " + domain.get(i).getAnnotKey());
				Annotation entity = annotationDAO.get(Integer.valueOf(domain.get(i).getAnnotKey()));
				annotationDAO.remove(entity);
				modified = true;
				log.info("processAnnotation/delete processed");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
				log.info("processAnnotation update");				
				Annotation entity = annotationDAO.get(Integer.valueOf(domain.get(i).getAnnotKey()));
				
				// process evidence; not every annotation has evidence
				if (domain.get(i).getEvidence() != null) {
					if (evidenceService.process(domain.get(i).getAnnotKey(), domain.get(i).getEvidence(), annotTypeKey, mgiTypeKey, user)) {
						log.info("processAnnotation/evidence: " + domain.get(i).getAnnotKey());
					}
				}

				entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				entity.setTerm(termDAO.get(Integer.valueOf(domain.get(i).getTermKey())));									
				entity.setModification_date(new Date());
				annotationDAO.update(entity);
				modified = true;
				log.info("processAnnotation/changes processed: " + domain.get(i).getAnnotKey());
			}
			else {
				log.info("processAnnotation/no changes processed: " + domain.get(i).getAnnotKey());
			}
		}	
			
		String cmd;
		Query query;
		
		// now merge any duplicate annotations that were created by the API when adding evidence
	    cmd = "select count(*) from VOC_mergeDupAnnotations(" + annotTypeKey + ", " + objectKey + ")";
	    log.info("cmd: " + cmd);
	    query = annotationDAO.createNativeQuery(cmd);
	    query.getResultList();	
	    
	    // determine and order MP Headers 
	    if (annotTypeKey.equals("1002")) { 
	    	cmd = "select count(*) from  VOC_processAnnotHeader(" 
	    		+ user.get_user_key().intValue()  
	    		+ ", " + annotTypeKey 
	    		+ ", " + objectKey+ ")";
	 	    log.info("cmd: " + cmd);
		    query = annotationDAO.createNativeQuery(cmd);
		    query.getResultList();		    
	    }
	    
		log.info("processAnnotation/processing successful");
		return modified;
	}
		
	@Transactional
	public Boolean processMarkerFeatureType(String parentKey, 
			List<MarkerFeatureTypeDomain> domain, 
			String annotTypeKey, 
			String qualifierKey, 
			User user) {
		
		// process marker feature type annotations
		// using MarkerFeatureTypeDomain, create AnnotationDomain and send to "process"
		
		if (domain == null || domain.isEmpty()) {
			log.info("processMarkerFeatureType/nothing to process");
			return false;
		}

		List<AnnotationDomain> annotDomains = new ArrayList<AnnotationDomain>();
		
		// iterate thru the list of rows in the MarkerFeatureTypeDomain
		// to create the AnnotationDomain
		
		for (int i = 0; i < domain.size(); i++) {	
			AnnotationDomain annotDomain = new AnnotationDomain();
			annotDomain.setProcessStatus(domain.get(i).getProcessStatus());
			annotDomain.setAnnotKey(domain.get(i).getAnnotKey());
			annotDomain.setAnnotTypeKey(annotTypeKey);
			annotDomain.setObjectKey(parentKey);
			annotDomain.setTermKey(domain.get(i).getTermKey());
			annotDomain.setQualifierKey(qualifierKey);
			annotDomains.add(annotDomain);
		}
		
		// process AnnotationDomain
		log.info("processMarkerFeatureType/processing");
		return process(annotDomains, user);
	}

	@Transactional
	public Boolean processAlleleVariant(String parentKey, 
			List<AlleleVariantAnnotationDomain> domain, 
			String annotTypeKey, 
			String qualifierKey, 
			User user) {
		
		// process allele variant annotations
		// using AlleleVariantAnnotationDomain, create AnnotationDomain and send to "process"

		if (domain == null || domain.isEmpty()) {
			log.info("processAlleleVariant/nothing to process");
			return false;
		}

		List<AnnotationDomain> annotDomains = new ArrayList<AnnotationDomain>();
		
		// iterate thru the list of rows in the AlleleVariantAnnotationDomain
		// to creating the AnnotationDomain
		
		for (int i = 0; i < domain.size(); i++) {	
			AnnotationDomain annotDomain = new AnnotationDomain();
			annotDomain.setProcessStatus(domain.get(i).getProcessStatus());
			annotDomain.setAnnotKey(domain.get(i).getAnnotKey());
			annotDomain.setAnnotTypeKey(annotTypeKey);
			annotDomain.setObjectKey(parentKey);
			annotDomain.setTermKey(domain.get(i).getTermKey());
			annotDomain.setQualifierKey(qualifierKey);
			annotDomains.add(annotDomain);
		}
		
		// process AnnotationDomain
		log.info("processAlleleVariant/processing");
		return process(annotDomains, user);
	}
}
