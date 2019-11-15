package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.EventDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.EventTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class EventService extends BaseService<EventDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private EventDAO eventDAO;

	private EventTranslator translator = new EventTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<EventDomain> create(EventDomain object, User user) {
		SearchResults<EventDomain> results = new SearchResults<EventDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<EventDomain> update(EventDomain object, User user) {
		SearchResults<EventDomain> results = new SearchResults<EventDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<EventDomain> delete(Integer key, User user) {
		SearchResults<EventDomain> results = new SearchResults<EventDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public EventDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		EventDomain domain = new EventDomain();
		if (eventDAO.get(key) != null) {
			domain = translator.translate(eventDAO.get(key));
		}
		eventDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<EventDomain> getResults(Integer key) {
        SearchResults<EventDomain> results = new SearchResults<EventDomain>();
        results.setItem(translator.translate(eventDAO.get(key)));
        eventDAO.clear();
        return results;
    }

	@Transactional	
	public List<EventDomain> search() {

		List<EventDomain> results = new ArrayList<EventDomain>();

		String cmd = "select * from mrk_event order by event";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				EventDomain domain = new EventDomain();
				domain = translator.translate(eventDAO.get(rs.getInt("_marker_event_key")));
				eventDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
