package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.NoteTypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class NoteTypeService extends BaseService<NoteTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private NoteTypeDAO noteTypeDAO;

	private NoteTypeTranslator translator = new NoteTypeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<NoteTypeDomain> create(NoteTypeDomain object, User user) {
		SearchResults<NoteTypeDomain> results = new SearchResults<NoteTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<NoteTypeDomain> update(NoteTypeDomain object, User user) {
		SearchResults<NoteTypeDomain> results = new SearchResults<NoteTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<NoteTypeDomain> delete(Integer key, User user) {
		SearchResults<NoteTypeDomain> results = new SearchResults<NoteTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public NoteTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		NoteTypeDomain domain = new NoteTypeDomain();
		if (noteTypeDAO.get(key) != null) {
			domain = translator.translate(noteTypeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<NoteTypeDomain> getResults(Integer key) {
        SearchResults<NoteTypeDomain> results = new SearchResults<NoteTypeDomain>();
        results.setItem(translator.translate(noteTypeDAO.get(key)));
        return results;
    }

	@Transactional	
	public SearchResults<NoteTypeDomain> search(NoteTypeDomain searchDomain) {

		SearchResults<NoteTypeDomain> results = new SearchResults<NoteTypeDomain>();

		String cmd = "select * from mgi_notetype";
		String where = "where _mgitype_key is not null";
		String orderBy = "order by _mgitype_key, notetype";
	
		if (searchDomain.getMgiTypeKey() != null && !searchDomain.getMgiTypeKey().isEmpty()) {
			where = where + "\nand _mgitype_key = " + searchDomain.getMgiTypeKey();
		}
		
		cmd = "\n" + cmd + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		try {
			List<NoteTypeDomain> noteTypeList = new ArrayList<NoteTypeDomain>();
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				NoteTypeDomain domain = new NoteTypeDomain();
				domain = translator.translate(noteTypeDAO.get(rs.getInt("_notetype_key")));
				noteTypeDAO.clear();
				noteTypeList.add(domain);
			}
			results.setItems(noteTypeList);			
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
