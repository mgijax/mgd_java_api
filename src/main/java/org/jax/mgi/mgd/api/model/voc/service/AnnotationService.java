package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationTypeDAO;
import org.jax.mgi.mgd.api.model.voc.dao.EvidenceDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
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
	private ReferenceDAO referenceDAO;

	private AnnotationTranslator translator = new AnnotationTranslator();
	private AlleleVariantAnnotationTranslator alleleVariantTranslator = new AlleleVariantAnnotationTranslator();	
	private MarkerFeatureTypeTranslator markerFeatureTypeTranslator = new MarkerFeatureTypeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<AnnotationDomain> create(AnnotationDomain object, User user) {
		return null;
	}

	@Transactional
	public SearchResults<AnnotationDomain> update(AnnotationDomain object, User user) {
		return null;
	}

	@Transactional
	public AnnotationDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AnnotationDomain domain = new AnnotationDomain();
		if (annotationDAO.get(key) != null) {
			domain = translator.translate(annotationDAO.get(key),1);
		}
		return domain;		
	}
	
    @Transactional
    public SearchResults<AnnotationDomain> getResults(Integer key) {
        SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
        results.setItem(translator.translate(annotationDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<AnnotationDomain> delete(Integer key, User user) {
		return null;
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
				domain = translator.translate(annotationDAO.get(rs.getInt("_annot_key")),1);
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
				domain = markerFeatureTypeTranslator.translate(annotationDAO.get(rs.getInt("_annot_key")),1);
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
	public List<AlleleVariantAnnotationDomain> alleleVariantAnnotations(Integer key, String annotTypeKey) {
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
				domain = alleleVariantTranslator.translate(annotationDAO.get(rs.getInt("_annot_key")),1);
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
		
		// first pass:  
		// 1.  only works for voc_annot, voc_evidence
		//      voc_evidence_property is not included in this pass
		// 2.  update is modifying _term_key only
	
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAnnotation/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				log.info("processAnnotation create");

				// create new entity object from in-coming domain
				// the Entities class handles the generation of the primary key
				// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

				// voc_annot
				Annotation entity = new Annotation();	
				entity.setAnnotType(annotTypeDAO.get(Integer.valueOf(domain.get(i).getAnnotTypeKey())));				
				entity.set_object_key(Integer.valueOf(domain.get(i).getObjectKey()));
				entity.setTerm(termDAO.get(Integer.valueOf(domain.get(i).getTermKey())));
				entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				annotationDAO.persist(entity);
		
				// not all annotation types have evidence records
				// voc_evidence
				if (domain.get(i).getEvidence() != null) {
					Evidence evidenceEntity = new Evidence();
					evidenceEntity.set_annot_key(entity.get_annot_key());
					evidenceEntity.setEvidenceTerm(termDAO.get(Integer.valueOf(domain.get(i).getEvidence().getEvidenceTermKey())));
					evidenceEntity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getEvidence().getRefsKey())));
					evidenceEntity.setInferredFrom(domain.get(i).getEvidence().getInferredFrom());
					evidenceEntity.setCreatedBy(user);
					evidenceEntity.setCreation_date(new Date());
					evidenceEntity.setModifiedBy(user);
					evidenceEntity.setModification_date(new Date());
					evidenceDAO.persist(evidenceEntity);
				}
				
				modified = true;
				log.info("processAnnotation/create/returning results");				
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAnnotation delete");
				Annotation entity = annotationDAO.get(Integer.valueOf(domain.get(i).getAnnotKey()));
				annotationDAO.remove(entity);
				modified = true;
				log.info("processAnnotation delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				
				// implemented update for:  term only
				
				log.info("processAnnotation update");

				Boolean isUpdated = false;
				Annotation entity = annotationDAO.get(Integer.valueOf(domain.get(i).getAnnotKey()));
				
				if (!String.valueOf(entity.getTerm().get_term_key()).equals(domain.get(i).getTermKey())) {
					
					entity.setTerm(termDAO.get(Integer.valueOf(domain.get(i).getTermKey())));
					
					// null any of the entity fields that are not updated
					// that is, any read-only fields
					entity.setMarkerFeatureTypeIds(null);	
					entity.setAlleleVariantSOIds(null);
					
					isUpdated = true;
				}
				
				// if any modifications made, then update DAO
				if (isUpdated) {
					entity.setModification_date(new Date());
					annotationDAO.persist(entity);
					modified = true;
					log.info("processAnnotation/changes processed: " + domain.get(i).getAnnotKey());
				}
				else {
					log.info("processAnnotation/no changes processed: " + domain.get(i).getAnnotKey());
				}
			}
			else {
				log.info("processAnnotation/no changes processed: " + domain.get(i).getAnnotKey());
			}
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
		
		// iterate thru the list of rows in the MarkerFeatureTypeDomain
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