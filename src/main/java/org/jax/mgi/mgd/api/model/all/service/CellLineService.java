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
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.model.all.translator.CellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
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
	@Inject
	private AlleleCellLineDerivationService derivationService;
	
	private CellLineTranslator translator = new CellLineTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<CellLineDomain> create(CellLineDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		CellLine entity = new CellLine();
		
		entity.setCellLine(domain.getCellLine());
		entity.setIsMutant(Integer.valueOf(domain.getIsMutant()));
		entity.setCellLineType(termDAO.get(Integer.valueOf(domain.getCellLineTypeKey())));
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainKey())));
		
		if (domain.getDerivation() != null) {
			entity.setDerivation(derivationDAO.get(Integer.valueOf(domain.getDerivation().getDerivationKey())));	
		}
		else {
			entity.setDerivation(null);
		}

		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		cellLineDAO.persist(entity);
	
		// return entity translated to domain
		log.info("processCellLine/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<CellLineDomain> update(CellLineDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		CellLine entity = cellLineDAO.get(Integer.valueOf(domain.getCellLineKey()));
		
		log.info("processCellLine/update");

		entity.setCellLine(domain.getCellLine());
		entity.setIsMutant(Integer.valueOf(domain.getIsMutant()));
		entity.setCellLineType(termDAO.get(Integer.valueOf(domain.getCellLineTypeKey())));
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainKey())));
		
		if (domain.getDerivation() != null) {
			entity.setDerivation(derivationDAO.get(Integer.valueOf(domain.getDerivation().getDerivationKey())));	
		}
		else {
			entity.setDerivation(null);
		}
		
		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		cellLineDAO.update(entity);
		log.info("processCellLine/changes processed: " + domain.getCellLineKey());
			
		// return entity translated to domain
		log.info("processCellLine/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processCellLine/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<CellLineDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		CellLine entity = cellLineDAO.get(key);
		results.setItem(translator.translate(cellLineDAO.get(key)));
		cellLineDAO.remove(entity);
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

    @Transactional
    public String alleleCreate(AlleleDomain domain, User user) {
		// potential new cell line to be used for allele/cellline association
    	
        //
		// for 1st row only...
        // check isParent, isMutant to see if a new cell line needs to be created
        //

		SlimAlleleCellLineDerivationDomain derivationSearch = new SlimAlleleCellLineDerivationDomain();
		List<AlleleCellLineDerivationDomain> derivationResults = new ArrayList<AlleleCellLineDerivationDomain>();
    	CellLineDomain cellLineDomain = new CellLineDomain();

        // set the isParent
        // default cellLineType = Embryonic Stem Cell (3982968)		
		Boolean isParent = true;
		Boolean isMutant = true;

		String cellLineTypeKey = domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineTypeKey();
		if (cellLineTypeKey.isEmpty()) {
          isParent = false;
          cellLineTypeKey = "3982968";          
        };

        if (domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineKey().isEmpty()) {
        	isMutant = false;
		}
        
        if (isParent == false && isMutant == false) {

        	log.info("alleleCreate/isParent == false && isMutant == false");

        	// not specified
        	//if = "Gene trapped" or "Targeted"
        	if (domain.getAlleleTypeKey().equals("847121") || domain.getAlleleTypeKey().equals("847116")) {

            	log.info("alleleCreate: " + domain.getAlleleTypeKey());

	            // select the derivation key that is associated with:
	            //   allele type
	            //   creator = Not Specified (3982966)
	            //   vector = Not Specified (4311225)           		
	            //   vector type = Not Specified (3982979)
	            //   parent cell line = Not Specified (-1)
	            //   strain = Not Specified (-1)
	            //   cell line type
	            //
            	
        		derivationSearch.setAlleleTypeKey(domain.getAlleleTypeKey());
        		derivationSearch.setVectorKey("4311225");
        		derivationSearch.setVectorTypeKey("3982979");
        		derivationSearch.setParentCellLineKey("-1");
        		derivationSearch.setCreatorKey("3982966");
        		derivationSearch.setStrainKey("-1");
        		derivationSearch.setCellLineTypeKey(cellLineTypeKey);
        		
        		derivationResults = derivationService.validateDerivation(derivationSearch);
        		
        		if (!derivationResults.get(0).getDerivationKey().isEmpty()) {       		
	        		log.info("alleleCreate/validated derivation: " + derivationResults.get(0).getDerivationKey());       		
	        		log.info("alleleCreate/create new cell line");
	        		cellLineDomain.setCellLine("Not Specified");
	        		cellLineDomain.setStrainKey("-1");
	        		cellLineDomain.setCellLineTypeKey(cellLineTypeKey);
	        		cellLineDomain.setDerivation(derivationResults.get(0));				
	        		cellLineDomain.setIsMutant("1");
	        		SearchResults<CellLineDomain> cellLineResults = new SearchResults<CellLineDomain>();
	        		log.info("processAlleleCellLine/calling cellLineService.create()");				
	        		cellLineResults = create(cellLineDomain, user);
	                return(cellLineResults.items.get(0).getCellLineKey());	        		
        		}
        	}         	
        }
        
        //
        // end check isParent, isMutant to see if a new cell line needs to be created
        //
        
		return(null);
    }
    	
}
