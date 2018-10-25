package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.OrganismSearchForm;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jboss.logging.Logger;

@RequestScoped
public class OrganismService extends BaseService<OrganismDomain> {

	protected Logger log = Logger.getLogger(OrganismService.class);

	@Inject
	private OrganismDAO organismDAO;

	private OrganismTranslator translator = new OrganismTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Override
	public OrganismDomain create(OrganismDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain update(OrganismDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain get(Integer key) {
		return translator.translate(organismDAO.get(key),3);
	}

	@Override
	public OrganismDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OrganismDomain> search(OrganismSearchForm searchForm) {

		// list of results to be returned
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select * from mgi_organism";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain organismDomain = new OrganismDomain();
				organismDomain.set_organism_key(rs.getInt("_organism_key"));
				organismDomain.setCommonname(rs.getString("commonname"));
				organismDomain.setLatinname(rs.getString("latinname"));
				organismDomain.setCreation_date(rs.getDate("creation_date"));
				organismDomain.setModification_date(rs.getDate("modification_date"));
				results.add(organismDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
}
