package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeAliasDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAliasDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAlias;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeAliasTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeAliasService extends BaseService<ProbeAliasDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeAliasDAO aliasDAO;

	private ProbeAliasTranslator translator = new ProbeAliasTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeAliasDomain> create(ProbeAliasDomain domain, User user) {
		SearchResults<ProbeAliasDomain> results = new SearchResults<ProbeAliasDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeAliasDomain> update(ProbeAliasDomain domain, User user) {
		SearchResults<ProbeAliasDomain> results = new SearchResults<ProbeAliasDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ProbeAliasDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeAliasDomain domain = new ProbeAliasDomain();
		if (aliasDAO.get(key) != null) {
			domain = translator.translate(aliasDAO.get(key));
		}
		aliasDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ProbeAliasDomain> getResults(Integer key) {
        SearchResults<ProbeAliasDomain> results = new SearchResults<ProbeAliasDomain>();
        results.setItem(translator.translate(aliasDAO.get(key)));
        aliasDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ProbeAliasDomain> delete(Integer key, User user) {
		SearchResults<ProbeAliasDomain> results = new SearchResults<ProbeAliasDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ProbeAliasDomain> search(Integer key) {

		List<ProbeAliasDomain> results = new ArrayList<ProbeAliasDomain>();

		String cmd = "\nselect _alias_key from prb_alias where _probe_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ProbeAliasDomain domain = new ProbeAliasDomain();	
				domain = translator.translate(aliasDAO.get(rs.getInt("_alias_key")));
				aliasDAO.clear();
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
	public Boolean process(String parentKey, ProbeAliasDomain domain, User user) {
		// process probe alias (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processProbeAlias");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if (domain == null || domain.getAlias().isEmpty()) {
				log.info("processProbeAlias/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processProbeAlias create");
			ProbeAlias entity = new ProbeAlias();
			entity.set_reference_key(Integer.valueOf(domain.getReferenceKey()));
			entity.setAlias(domain.getAlias());
			entity.setCreatedBy(user);
			entity.setModifiedBy(user);
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			aliasDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processProbeAlias delete");				
			ProbeAlias entity = aliasDAO.get(Integer.valueOf(domain.getAliasKey()));
			aliasDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processProbeAlias update");								
			ProbeAlias entity = aliasDAO.get(Integer.valueOf(domain.getAliasKey()));
			entity.set_reference_key(Integer.valueOf(domain.getReferenceKey()));			
			entity.setAlias(domain.getAlias());
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());
			aliasDAO.update(entity);
			modified = true;
			log.info("processProbeAlias/changes processed: " + domain.getAliasKey());
		}
		else {
			log.info("processProbeAlias/no changes processed: " + domain.getAliasKey());
		}
		
		log.info("processProbeAlias/processing successful");
		return modified;
	}
	
}
