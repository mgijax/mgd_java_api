package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceCitationCacheDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.EventReasonDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerHistoryDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerHistoryTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerHistoryService extends BaseService<MarkerHistoryDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MarkerHistoryDAO historyDAO;
	@Inject
	private EventDAO eventDAO;
	@Inject
	private EventReasonDAO eventReasonDAO;
	@Inject
	private ReferenceCitationCacheDAO referenceDAO;
	@Inject
	private MarkerDAO markerDAO;

	private MarkerHistoryTranslator translator = new MarkerHistoryTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerHistoryDomain> create(MarkerHistoryDomain domain, User user) {
		SearchResults<MarkerHistoryDomain> results = new SearchResults<MarkerHistoryDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MarkerHistoryDomain> update(MarkerHistoryDomain domain, User user) {
		SearchResults<MarkerHistoryDomain> results = new SearchResults<MarkerHistoryDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public MarkerHistoryDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MarkerHistoryDomain domain = new MarkerHistoryDomain();
		if (historyDAO.get(key) != null) {
			domain = translator.translate(historyDAO.get(key));
		}
		historyDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MarkerHistoryDomain> getResults(Integer key) {
        SearchResults<MarkerHistoryDomain> results = new SearchResults<MarkerHistoryDomain>();
        results.setItem(translator.translate(historyDAO.get(key)));
        historyDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<MarkerHistoryDomain> delete(Integer key, User user) {
		SearchResults<MarkerHistoryDomain> results = new SearchResults<MarkerHistoryDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<MarkerHistoryDomain> search(Integer key) {

		List<MarkerHistoryDomain> results = new ArrayList<MarkerHistoryDomain>();

		String cmd = "\nselect _assoc_key, sequencenum from mrk_history_view"
				+ "\nwhere _marker_key = " + key
				+ "\norder by sequencenum";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerHistoryDomain domain = new MarkerHistoryDomain();	
				domain = translator.translate(historyDAO.get(rs.getInt("_assoc_key")));
				historyDAO.clear();
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
	public Boolean process(String parentKey, List<MarkerHistoryDomain> domain, User user) {
		// process marker history associations (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processHistory");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processHistory/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				// if symbol key is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getMarkerHistorySymbolKey().isEmpty()) {
					continue;
				}
				
				log.info("processHistory create");
				
				if (domain.get(i).getMarkerEventReasonKey().isEmpty()) {
					domain.get(i).setMarkerEventReasonKey("-1");
				}
				
				cmd = "select count(*) from MRK_insertHistory ("
							+ user.get_user_key().intValue()
							+ "," + parentKey
							+ "," + domain.get(i).getMarkerHistorySymbolKey()
							+ "," + domain.get(i).getRefsKey()
							+ "," + domain.get(i).getMarkerEventKey()
							+ "," + domain.get(i).getMarkerEventReasonKey()
							+ ",'" + domain.get(i).getMarkerHistoryName() + "'"
							+ ")";
				log.info("cmd: " + cmd);
				Query query = historyDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processHistory delete");
				
				MarkerHistory entity = historyDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				historyDAO.remove(entity);
				
				// reset the sequence numbers
				cmd = "select count(*) from MGI_resetSequenceNum('MRK_History'" 
						+ "," + parentKey
						+ "," + user.get_user_key().intValue()
						+ ")";
				log.info("cmd: " + cmd);		
				Query query = historyDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processHistory update");
								
				MarkerHistory entity = historyDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));

				entity.setMarkerHistory(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerHistorySymbolKey())));
				entity.setMarkerEvent(eventDAO.get((Integer.valueOf(domain.get(i).getMarkerEventKey()))));
				entity.setMarkerEventReason(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())));
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
				
				// can be null
				
				if (domain.get(i).getRefsKey() == null || domain.get(i).getRefsKey().isEmpty()) {
					entity.setReference(null);
				}
				else {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				}
						
				if (domain.get(i).getMarkerHistoryName() == null || domain.get(i).getMarkerHistoryName().isEmpty()) {
					entity.setName(null);
				}
				else {
					entity.setName(domain.get(i).getMarkerHistoryName());
				}

				if (domain.get(i).getEvent_date() == null || domain.get(i).getEvent_date().isEmpty()) {
					entity.setEvent_date(null);
				}
				else {
					try {
						// convert String to Date
						entity.setEvent_date(new SimpleDateFormat("yyyy-MM-dd").parse(domain.get(i).getEvent_date()));
					}
					catch (ParseException  e) {
						return false;
					}					
				}				
				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				historyDAO.update(entity);
				modified = true;
				log.info("processHistory/changes processed: " + domain.get(i).getAssocKey());
			}
			else {
				log.info("processHistory/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processHistory/processing successful");
		return modified;
	}
	
}
