package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
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

	//
	// get list of annotation domains by using sqlExecutor
	//
		
	private List<AnnotationDomain> getAnnotationDomainList(String cmd) {

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
	
	public List<MarkerFeatureTypeDomain> markerFeatureTypes(Integer key) {

		List<MarkerFeatureTypeDomain> results = new ArrayList<MarkerFeatureTypeDomain>();
		
		String cmd = "\nselect t._term_key, t.term, a.*"
				+ "\nfrom VOC_Annot v, VOC_Term t, ACC_Accession a"
				+ "\nwhere v._annottype_key = 1011"
				+ "\nand v._term_key = t._term_key"
				+ "\nand v._object_key = a._object_key"
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
				List<SlimAccessionDomain> accession = new ArrayList<SlimAccessionDomain>();				
				accDomain.setAccessionKey(rs.getString("accessionKey"));
				accDomain.setLogicaldbKey(rs.getString("_logicaldb_key"));
				accDomain.setObjectKey(rs.getString("_object_key"));
				accDomain.setMgiTypeKey(rs.getString("_mgitype_key"));
				accDomain.setAccID(rs.getString("accid"));
				accDomain.setPrefixPart(rs.getString("prefixPart"));
				accDomain.setNumericPart(rs.getString("numericPart"));
				//accDomain.setIsPrivate(rs.getString("isPrivate"));
				//accDomain.setPreferred(rs.getString("preferred"));		
				domain.setMarkerFeatureTypeIds(accession);

				results.add(domain);
			}
			sqlExecutor.cleanup();		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
		
}
