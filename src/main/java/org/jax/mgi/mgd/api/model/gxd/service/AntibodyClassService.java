package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyClassDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyClassDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyClassTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyClassService extends BaseService<AntibodyClassDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyClassDAO antibodyClassDAO;
	
	private AntibodyClassTranslator translator = new AntibodyClassTranslator();
	private TermTranslator termtranslator = new TermTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AntibodyClassDomain> create(AntibodyClassDomain domain, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AntibodyClassDomain> update(AntibodyClassDomain domain, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AntibodyClassDomain> delete(Integer key, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public AntibodyClassDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyClassDomain domain = new AntibodyClassDomain();
		if (antibodyClassDAO.get(key) != null) {
			domain = translator.translate(antibodyClassDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyClassDomain> getResults(Integer key) {
        SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
        results.setItem(translator.translate(antibodyClassDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyClassDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		String cmd = "select count(*) as objectCount from gxd_AntibodyClass";
		
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
	public List<AntibodyClassDomain> search(AntibodyClassDomain searchDomain) {
		// return AntibodyClassDomain which looks like a vocab/term domain

		List<AntibodyClassDomain> results = new ArrayList<AntibodyClassDomain>();
		AntibodyClassDomain adomain = new AntibodyClassDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_AntibodyClass order by class";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_antibodyclass_key"));
				tdomain.setTerm(rs.getString("class"));
				tdomain.setSequenceNum(String.valueOf(sequenceNum));
				termresults.add(tdomain);
				sequenceNum = sequenceNum + 1;
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		adomain.setTerms(termresults);
		results.add(adomain);
		
		return results;
	}
}
