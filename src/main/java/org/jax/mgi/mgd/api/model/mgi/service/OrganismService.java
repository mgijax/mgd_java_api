package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class OrganismService extends BaseService<OrganismDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private OrganismDAO organismDAO;

	private OrganismTranslator translator = new OrganismTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<OrganismDomain> create(OrganismDomain object, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<OrganismDomain> update(OrganismDomain object, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<OrganismDomain> delete(Integer key, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public OrganismDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		OrganismDomain domain = new OrganismDomain();
		if (organismDAO.get(key) != null) {
			domain = translator.translate(organismDAO.get(key));
		}
		organismDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<OrganismDomain> getResults(Integer key) {
        SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
        results.setItem(translator.translate(organismDAO.get(key)));
        organismDAO.clear();
        return results;
    }

	@Transactional	
	public List<OrganismDomain> search() {

		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select * from mgi_organism order by commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}
	
	@Transactional	
	public List<OrganismDomain> searchMarker() {
		// for marker module
		// exclude: Other, Not Applicable, Not Loaded
		
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key"
				+ "\nfrom mgi_organism"
				+ "\nwhere _organism_key not in (74,75,77)"
				+ "\norder by commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}

	@Transactional	
	public List<OrganismDomain> searchDriverGene() {
		// for allele module/driver note
		// include: see mgi_organism_allele_view
		
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key from mgi_organism_allele_view order by commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
	@Transactional	
	public List<OrganismDomain> searchAntigen() {
		// for antigen module organism pick list
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key, commonname, 0 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname = 'mouse, laboratory'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 1 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname = 'human'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 2 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname = 'Not Specified'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 3 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname not in ('mouse, laboratory', 'Not Specified', 'human')\n" + 
				"order by org, commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}
	@Transactional	
	public List<OrganismDomain> searchAntibody() {
		// for antigen module organism pick list
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd ="select s.*\n" + 
				"from MGI_Organism s, MGI_Organism_MGIType t\n" + 
				"where s._Organism_key = t._Organism_key\n" +
				"and t._MGIType_key = 6";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}
	
}
