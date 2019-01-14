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
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.EventReasonDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerHistoryDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerHistoryService extends BaseService<MarkerHistoryDomain> {

	protected static Logger log = Logger.getLogger(MarkerHistoryService.class);

	@Inject
	private MarkerHistoryDAO historyDAO;
	@Inject
	private EventDAO eventDAO;
	@Inject
	private EventReasonDAO eventReasonDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerHistoryDomain> create(MarkerHistoryDomain domain, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MarkerHistoryDomain> update(MarkerHistoryDomain domain, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerHistoryDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Transactional
    public SearchResults<MarkerHistoryDomain> getResults(Integer key) {
		// TODO Auto-generated method stub
    	return null;
    }
    
	@Transactional
	public SearchResults<MarkerHistoryDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional	
	public List<MarkerHistoryDomain> search(Integer key) {

		List<MarkerHistoryDomain> results = new ArrayList<MarkerHistoryDomain>();

		String cmd = "\nselect * from mrk_history_view"
				+ "\nwhere _marker_key = " + key
				+ "\norder by sequencenum";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				MarkerHistoryDomain domain = new MarkerHistoryDomain();
				
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setAssocKey(rs.getString("_assoc_key"));
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSequenceNum(rs.getString("sequencenum"));
				domain.setMarkerEventKey(rs.getString("_marker_event_key"));
				domain.setMarkerEvent(rs.getString("event"));
				domain.setMarkerEventReasonKey(rs.getString("_marker_eventreason_key"));
				domain.setMarkerEventReason(rs.getString("eventreason"));
				domain.setMarkerHistorySymbolKey(rs.getString("_history_key"));
				domain.setMarkerHistorySymbol(rs.getString("history"));
				domain.setMarkerHistoryName(rs.getString("name"));
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setJnumid(rs.getString("jnumid"));
				domain.setShort_citation(rs.getString("short_citation"));
				domain.setEvent_date(rs.getString("event_date"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdBy"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedBy"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
				
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
	public void process(String parentKey, List<MarkerHistoryDomain> domain, User user) {
		// process marker history associations (create, delete, update)
		
		log.info("processHistory");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processHistory/nothing to process");
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processHistory create");
				
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
				Query query = historyDAO.createNativeQuery(cmd);
				query.getResultList();
				
				log.info("processHistory delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processHistory update");

				Boolean modified = false;
				
				//log.info("historyDAO");
				MarkerHistory entity = historyDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));

				//log.info("marker key");
				if (!String.valueOf(entity.get_marker_key()).equals(domain.get(i).getMarkerKey())) {
					entity.set_marker_key(Integer.valueOf(domain.get(i).getMarkerKey()));
					modified = true;
				}
				
				//log.info("event");
				if (!entity.getMarkerEvent().equals(eventDAO.get(Integer.valueOf(domain.get(i).getMarkerEventKey())))) {
					entity.setMarkerEvent(eventDAO.get((Integer.valueOf(domain.get(i).getMarkerEventKey()))));
					modified = true;
				}
				
				//log.info("event reason");
				if (!entity.getMarkerEventReason().equals(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())))) {
					entity.setMarkerEventReason(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())));
					modified = true;
				}
				
				// reference can be null
				//log.info("reference");
				// domain = null, entity = null
				if (domain.get(i).getRefsKey() == null && entity.getReference() == null) {
					//do nothing; don't want nulls");
				}
				// domain = null, entity != null
				else if (domain.get(i).getRefsKey() == null && entity.getReference() != null) {
					//do nothing; don't want nulls");
				}
				// domain != null, entity = null
				else if (domain.get(i).getRefsKey() != null && entity.getReference() == null) {
					//log.info("reference: domain != null, entity = null");
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
					modified = true;					
				}
				// if not entity/null and not domain/empty, then check if equivalent
				else if (entity.getReference().get_refs_key() != Integer.parseInt(domain.get(i).getRefsKey())) {
					//log.info("reference: entity != null");
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
					modified = true;
				}
						
				// name can be null
				//log.info("history name");
				// domain = null, entity = null
				if (domain.get(0).getMarkerHistoryName() == null && entity.getName() == null) {
					// do nothing; don't want nulls
				}
				// domain = null, entity != null				
				else if (domain.get(0).getMarkerHistoryName() == null && entity.getName() != null) {
					// do nothing; don't want nulls
				}
				// domain != null, entity = null
				else if (domain.get(i).getMarkerHistoryName() != null && entity.getName() == null) {
					entity.setName(domain.get(i).getMarkerHistoryName());
					modified = true;
				}
				// if not entity/null and not domain/empty, then check if equivalent
				else if (!entity.getName().equals(domain.get(i).getMarkerHistoryName())) {
					entity.setName(domain.get(i).getMarkerHistoryName());
					modified = true;
				}
					
				log.info("event date");
				if (domain.get(i).getEvent_date() == null && entity.getEvent_date() == null) {
					// do nothing; don't want nulls
				} 
				else if (domain.get(i).getEvent_date() == null && entity.getEvent_date() != null) {
					// do nothing; don't want nulls
				}
				else if (domain.get(i).getEvent_date() != null && entity.getEvent_date() == null) {
					try {
						// convert String to Date
						entity.setEvent_date(new SimpleDateFormat("yyyy-MM-dd").parse(domain.get(i).getEvent_date()));
						modified = true;
					}
					catch (ParseException  e) {
						return;
					}					
				}
				else if (!entity.getEvent_date().toString().equals(domain.get(i).getEvent_date())) {
					try {
						// convert String to Date
						entity.setEvent_date(new SimpleDateFormat("yyyy-MM-dd").parse(domain.get(i).getEvent_date()));
						modified = true;
					}
					catch (ParseException  e) {
						return;
					}
				}
				
				log.info("reference: check if modified");
				if (modified == true) {
					log.info("processHistory modified == true");
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					historyDAO.update(entity);
					log.info("processHistory/changes processed: " + domain.get(i).getAssocKey());
				}
				else {
					log.info("processHistory/no changes processed: " + domain.get(i).getAssocKey());
				}
			}
			else {
				log.info("processHistory/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processHistory/processing successful");
		return;
	}
	
}
