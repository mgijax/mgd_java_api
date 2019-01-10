package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationTypeDAO;
import org.jax.mgi.mgd.api.model.voc.dao.EvidenceDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantVocabDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AnnotationService extends BaseService<AnnotationDomain> {

	protected Logger log = Logger.getLogger(AnnotationService.class);

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
		return translator.translate(annotationDAO.get(key));
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
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setAnnotKey(rs.getString("_annot_key"));
				domain.setAnnotTypeKey(rs.getString("_annottype_key"));
				domain.setAnnotType(rs.getString("annottype"));
				domain.setTermKey(rs.getString("_term_key"));
				domain.setTerm(rs.getString("term"));
				domain.setQualifierKey(rs.getString("_qualifier_key"));
				domain.setQualifier(rs.getString("qualifier"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
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
	public List<MarkerFeatureTypeDomain> markerFeatureTypes(Integer key) {
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
				domain.setTermKey(rs.getString("_term_key"));
				domain.setTerm(rs.getString("term"));
				
				SlimAccessionDomain accDomain = new SlimAccessionDomain();				
				List<SlimAccessionDomain> accessions = new ArrayList<SlimAccessionDomain>();
				accDomain.setAccessionKey(rs.getString("_accession_key"));
				accDomain.setLogicaldbKey(rs.getString("_logicaldb_key"));
				accDomain.setObjectKey(rs.getString("_object_key"));
				accDomain.setMgiTypeKey(rs.getString("_mgitype_key"));
				accDomain.setAccID(rs.getString("accid"));
				accDomain.setPrefixPart(rs.getString("prefixPart"));
				accDomain.setNumericPart(rs.getString("numericPart"));
				//accDomain.setIsPrivate(rs.getString("isPrivate"));
				//accDomain.setPreferred(rs.getString("preferred"));
				accessions.add(accDomain);
				domain.setMarkerFeatureTypeIds(accessions);

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
	public List<AlleleVariantVocabDomain> alleleVariantAnnotations(Integer key, String annotTypeKey) {
		// list of allele variant type domains for given variant
		
		// if we need to include the voc_evidence info
		// then add the voc_evidence info into the AlleleVariantVocDomain
		// and then add the voc_evidence info into the SQL cmd string
		// and add to ResultSet
		
		List<AlleleVariantVocabDomain> results = new ArrayList<AlleleVariantVocabDomain>();
		
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
				AlleleVariantVocabDomain domain = new AlleleVariantVocabDomain();
				domain.setAnnotKey(rs.getString("_annot_key"));
				domain.setTermKey(rs.getString("_term_key"));
				domain.setTerm(rs.getString("term"));
				
				SlimAccessionDomain accDomain = new SlimAccessionDomain();				
				List<SlimAccessionDomain> accessions = new ArrayList<SlimAccessionDomain>();
				accDomain.setAccessionKey(rs.getString("_accession_key"));
				accDomain.setLogicaldbKey(rs.getString("_logicaldb_key"));
				accDomain.setObjectKey(rs.getString("_object_key"));
				accDomain.setMgiTypeKey(rs.getString("_mgitype_key"));
				accDomain.setAccID(rs.getString("accid"));
				accDomain.setPrefixPart(rs.getString("prefixPart"));
				accDomain.setNumericPart(rs.getString("numericPart"));
				//accDomain.setIsPrivate(rs.getString("isPrivate"));
				//accDomain.setPreferred(rs.getString("preferred"));
				accessions.add(accDomain);
				domain.setAlleleVariantSOIds(accessions);

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
	public void process(String parentKey, List<AnnotationDomain> domain, String annotTypeKey, User user) {
		// process annotation associations (create, delete, update)
		
		// first pass:  
		// 1.  only works for voc_annot, voc_evidence
		//      voc_evidence_property is not included in this pass
		// 2.  implement create/delete only
	
		if (domain == null || domain.isEmpty()) {
			log.info("processAnnotation/nothing to process");
			return;
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
				entity.setAnnotType(annotTypeDAO.get(Integer.valueOf(domain.get(0).getAnnotKey())));				
				entity.set_object_key(Integer.valueOf(domain.get(i).getObjectKey()));
				entity.setTerm(termDAO.get(Integer.valueOf(domain.get(0).getTermKey())));
				entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(0).getQualifierKey())));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				annotationDAO.persist(entity);
		
				// voc_evidence
				Evidence evidenceEntity = new Evidence();
				evidenceEntity.set_annot_key(entity.get_annot_key());
				evidenceEntity.setEvidenceTerm(termDAO.get(Integer.valueOf(domain.get(0).getEvidence().getEvidenceTermKey())));
				evidenceEntity.setReference(referenceDAO.get(Integer.valueOf(domain.get(0).getEvidence().getRefsKey())));
				evidenceEntity.setInferredFrom(domain.get(0).getEvidence().getInferredFrom());
				evidenceEntity.setCreatedBy(user);
				evidenceEntity.setCreation_date(new Date());
				evidenceEntity.setModifiedBy(user);
				evidenceEntity.setModification_date(new Date());
				evidenceDAO.persist(evidenceEntity);
								
				log.info("processAnnotation/create/returning results");				
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAnnotation delete");
				Annotation entity = annotationDAO.get(Integer.valueOf(domain.get(i).getAnnotKey()));
				annotationDAO.remove(entity);
				log.info("processAnnotation delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				
				// not implementing an update at this time
				// instead, UI should implement as a delete/insert
				
				log.info("processAnnotation update");

				Boolean modified = false;
				Annotation entity = annotationDAO.get(Integer.valueOf(domain.get(i).getAnnotKey()));
				
				if (modified == true) {
					entity.setModification_date(new Date());
					annotationDAO.update(entity);
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
		return;
	}
		
}
