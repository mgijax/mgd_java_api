package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GXDLabelDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDLabelDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDLabel;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GXDLabelService extends BaseService<GXDLabelDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GXDLabelDAO gxdLabelDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<GXDLabelDomain> create(GXDLabelDomain domain, User user) {
		SearchResults<GXDLabelDomain> results = new SearchResults<GXDLabelDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<GXDLabelDomain> update(GXDLabelDomain domain, User user) {
		SearchResults<GXDLabelDomain> results = new SearchResults<GXDLabelDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processGXDLabel/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processGXDLabel/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processGXDLabel/nothing to create");
					continue;
				}
				
				GXDLabel entity = new GXDLabel();	
				entity.setLabel(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				gxdLabelDAO.persist(entity);				
				log.info("processGXDLabel/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGXDLabel delete");
				GXDLabel entity = gxdLabelDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				gxdLabelDAO.remove(entity);
				log.info("processGXDLabel/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGXDLabel update");
				GXDLabel entity = gxdLabelDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setLabel(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				gxdLabelDAO.update(entity);
				log.info("processGXDLabel/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processGXDLabel/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processGXDLabel/update/returning results");
		results.setItems(search(domain));
		log.info("processGXDLabel/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<GXDLabelDomain> delete(Integer key, User user) {
		SearchResults<GXDLabelDomain> results = new SearchResults<GXDLabelDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public GXDLabelDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GXDLabelDomain domain = new GXDLabelDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<GXDLabelDomain> getResults(Integer key) {
        SearchResults<GXDLabelDomain> results = new SearchResults<GXDLabelDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<GXDLabelDomain> search(GXDLabelDomain searchDomain) {
		// return GXDLabelDomain which looks like a vocab/term domain

		List<GXDLabelDomain> results = new ArrayList<GXDLabelDomain>();
		GXDLabelDomain adomain = new GXDLabelDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_label order by label";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_label_key"));
				tdomain.setTerm(rs.getString("label"));
				tdomain.setIsObsolete("0");
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
