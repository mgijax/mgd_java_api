package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleCellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleCellLineService extends BaseService<AlleleCellLineDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleCellLineDAO cellLineDAO;
	
	private AlleleCellLineTranslator translator = new AlleleCellLineTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<AlleleCellLineDomain> create(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDomain> update(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDomain> delete(Integer key, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AlleleCellLineDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AlleleCellLineDomain domain = new AlleleCellLineDomain();
		if (cellLineDAO.get(key) != null) {
			domain = translator.translate(cellLineDAO.get(key));
		}
		cellLineDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AlleleCellLineDomain> getResults(Integer key) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setItem(translator.translate(cellLineDAO.get(key)));
		cellLineDAO.clear();
		return results;
    }

	@Transactional
	public List<AlleleCellLineDomain> validateMutantCellLine(AlleleCellLineDomain searchDomain) {

		List<AlleleCellLineDomain> results = new ArrayList<AlleleCellLineDomain>();
		
		String cmd = "\nselect _cellline_key"
				+ "\nfrom ALL_CellLine"
				+ "\nwhere isMutant = 1"
				+ "\nand lower(cellLine) = lower('" + searchDomain.getMutantCellLine() + "')";

		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				AlleleCellLineDomain domain = new AlleleCellLineDomain();
				domain = translator.translate(cellLineDAO.get(rs.getInt("_cellline_key")));	
				cellLineDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
