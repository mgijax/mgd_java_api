package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.translator.CellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class CellLineService extends BaseService<CellLineDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private CellLineDAO cellLineDAO;
	
	private CellLineTranslator translator = new CellLineTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<CellLineDomain> create(CellLineDomain domain, User user) {
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<CellLineDomain> update(CellLineDomain domain, User user) {
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<CellLineDomain> delete(Integer key, User user) {
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public CellLineDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		CellLineDomain domain = new CellLineDomain();
		if (cellLineDAO.get(key) != null) {
			domain = translator.translate(cellLineDAO.get(key));
		}
		cellLineDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<CellLineDomain> getResults(Integer key) {
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		results.setItem(translator.translate(cellLineDAO.get(key)));
		cellLineDAO.clear();
		return results;
    }

	@Transactional
	public List<CellLineDomain> validateMutantCellLine(CellLineDomain searchDomain) {

		List<CellLineDomain> results = new ArrayList<CellLineDomain>();
		
		String cmd = "\nselect _cellline_key"
				+ "\nfrom ALL_CellLine"
				+ "\nwhere isMutant = 1"
				+ "\nand lower(cellLine) = lower('" + searchDomain.getCellLine() + "')";

		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				CellLineDomain domain = new CellLineDomain();
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
