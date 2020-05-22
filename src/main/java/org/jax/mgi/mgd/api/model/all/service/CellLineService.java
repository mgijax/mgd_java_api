package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDerivationDAO;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.model.all.translator.CellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class CellLineService extends BaseService<CellLineDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private CellLineDAO cellLineDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private ProbeStrainDAO strainDAO;
	@Inject
	private AlleleCellLineDerivationDAO derivationDAO;
	
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
	public Boolean process(String parentKey, List<CellLineDomain> domain, User user) {
		// process cell line (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processCellLine");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processCellLine/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				if (domain.get(i).getCellLineTypeKey().isEmpty()) {
					continue;
				}
				
				log.info("processCellLine/create");
				CellLine entity = new CellLine();									
				entity.setCellLine(domain.get(i).getCellLine());
				entity.setIsMutant(Integer.valueOf(domain.get(i).getIsMutant()));				
				entity.setCellLineType(termDAO.get(Integer.valueOf(domain.get(i).getCellLineTypeKey())));
				entity.setStrain(strainDAO.get(Integer.valueOf(domain.get(i).getStrainKey())));
				entity.setDerivation(derivationDAO.get(Integer.valueOf(domain.get(i).getDerivationKey())));				
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());				
				cellLineDAO.persist(entity);				
				log.info("processCellLine/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processCellLine/delete");
				if (domain.get(i).getCellLineKey() != null && !domain.get(i).getCellLineKey().isEmpty()) {
					CellLine entity = cellLineDAO.get(Integer.valueOf(domain.get(i).getCellLineKey()));
					cellLineDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processCellLine/update");
				CellLine entity = cellLineDAO.get(Integer.valueOf(domain.get(i).getCellLineKey()));			
				entity.setCellLine(domain.get(i).getCellLine());
				entity.setIsMutant(Integer.valueOf(domain.get(i).getIsMutant()));				
				entity.setCellLineType(termDAO.get(Integer.valueOf(domain.get(i).getCellLineTypeKey())));
				entity.setStrain(strainDAO.get(Integer.valueOf(domain.get(i).getStrainKey())));
				entity.setDerivation(derivationDAO.get(Integer.valueOf(domain.get(i).getDerivationKey())));				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				cellLineDAO.update(entity);
				log.info("processCellLine/changes processed: " + domain.get(i).getCellLineKey());				
				modified = true;
			}
			else {
				log.info("processCellLine/no changes processed: " + domain.get(i).getCellLineKey());
			}
		}
		
		log.info("processCellLine/processing successful");
		return modified;
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

	@Transactional
	public List<CellLineDomain> validateParentCellLine(CellLineDomain searchDomain) {

		List<CellLineDomain> results = new ArrayList<CellLineDomain>();
		
		String cmd = "\nselect _cellline_key"
				+ "\nfrom ALL_CellLine"
				+ "\nwhere isMutant = 0"
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
