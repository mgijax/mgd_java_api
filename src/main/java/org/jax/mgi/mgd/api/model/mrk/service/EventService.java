package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.EventDomain;
import org.jax.mgi.mgd.api.model.mrk.search.EventSearchForm;
import org.jax.mgi.mgd.api.model.mrk.translator.EventTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jboss.logging.Logger;

@RequestScoped
public class EventService extends BaseService<EventDomain> {

	protected Logger log = Logger.getLogger(EventService.class);

	@Inject
	private EventDAO eventDAO;

	private EventTranslator translator = new EventTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<EventDomain> create(EventDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<EventDomain> update(EventDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public EventDomain get(Integer key) {
		return translator.translate(eventDAO.get(key),1);
	}

        @Transactional
        public SearchResults<EventDomain> getResults(Integer key) {
                SearchResults<EventDomain> results = new SearchResults<EventDomain>();
                results.setItem(translator.translate(eventDAO.get(key)));
                return results;
        }

	@Transactional
	public SearchResults<EventDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EventDomain> search(EventSearchForm searchForm) {

		// list of results to be returned
		List<EventDomain> results = new ArrayList<EventDomain>();

		String cmd = "select * from mrk_event";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				EventDomain eventDomain = new EventDomain();
				eventDomain.setMarkerEventKey(rs.getInt("_marker_event_key"));
				eventDomain.setEvent(rs.getString("event"));
				eventDomain.setCreation_date(rs.getDate("creation_date"));
				eventDomain.setModification_date(rs.getDate("modification_date"));
				results.add(eventDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
	
}
