package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventReasonDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.EventReasonDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.EventReasonTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class EventReasonService extends BaseService<EventReasonDomain> {

	protected Logger log = Logger.getLogger(EventReasonService.class);

	@Inject
	private EventReasonDAO eventReasonDAO;

	private EventReasonTranslator translator = new EventReasonTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<EventReasonDomain> create(EventReasonDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<EventReasonDomain> update(EventReasonDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public EventReasonDomain get(Integer key) {
		return translator.translate(eventReasonDAO.get(key),1);
	}

    @Transactional
    public SearchResults<EventReasonDomain> getResults(Integer key) {
        SearchResults<EventReasonDomain> results = new SearchResults<EventReasonDomain>();
        results.setItem(translator.translate(eventReasonDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<EventReasonDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EventReasonDomain> search() {

		// list of results to be returned
		List<EventReasonDomain> results = new ArrayList<EventReasonDomain>();

		String cmd = "select * from mrk_eventreason";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				EventReasonDomain domain = new EventReasonDomain();
				domain.setMarkerEventReasonKey(rs.getInt("_marker_eventreason_key"));
				domain.setEventReason(rs.getString("eventreason"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
	
}
