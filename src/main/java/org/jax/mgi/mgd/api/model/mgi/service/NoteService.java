package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.NoteDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.NoteSearchForm;
import org.jax.mgi.mgd.api.model.mgi.search.OrganismSearchForm;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class NoteService extends BaseService<NoteDomain> {

	protected Logger log = Logger.getLogger(NoteService.class);

	@Inject
	private NoteDAO noteDAO;

	private NoteTranslator translator = new NoteTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Override
	public NoteDomain create(NoteDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteDomain update(NoteDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteDomain get(Integer key) {
		return translator.translate(noteDAO.get(key),3);
	}

	@Override
	public NoteDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<NoteDomain> search(NoteSearchForm searchForm) {

		// list of results to be returned
		//List<NoteDomain> results = new ArrayList<NoteDomain>();

		//String cmd = "select * from mgi_note";
		//log.info(cmd);

		// request data, and parse results
		//try {
		//	ResultSet rs = sqlExecutor.executeProto(cmd);
		//	while (rs.next()) {
		//		NoteDomain noteDomain = new NoteDomain();
		//		noteDomain.set_object_key(rs.getInt("_object_key"));
		//		noteDomain.set_mgitype_key(rs.getInt("_mgitype_key"));
				//noteDomain.setCreation_date(rs.getDate("creation_date"));
				//noteDomain.setModification_date(rs.getDate("modification_date"));
		//		results.add(noteDomain);
		//	}
		//	sqlExecutor.cleanup();
		//}
		//catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		//return results;
		return null;
	}

}
