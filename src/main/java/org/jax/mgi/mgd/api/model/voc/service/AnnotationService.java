package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
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
				domain.setQulalifier(rs.getString("qualifier"));
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
	
	public List<AnnotationDomain> markerMCV(Integer key) {

		List<AnnotationDomain> results = new ArrayList<AnnotationDomain>();

		String cmd = "\nselect * from VOC_Annot_View "
				+ "\nwhere _annottype_key = 1011"
				+ "\nand _logicaldb_key = 146"
				+ "\nand _object_key = " + key;
		log.info(cmd);

		// request data, and parse results
		try {
			results = getAnnotationDomainList(cmd);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
		
}
