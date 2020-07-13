package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDerivationDAO;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimCellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.model.all.translator.CellLineTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimCellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
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
	@Inject
	private AccessionService accessionService;
	
	private CellLineTranslator translator = new CellLineTranslator();	
	private SlimCellLineTranslator slimtranslator = new SlimCellLineTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	//private String mgiTypeKey = "28";
	private String mgiTypeName = "ES Cell Line";
	

	@Transactional
	public SearchResults<CellLineDomain> create(CellLineDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		CellLine entity = new CellLine();
		
		log.info("processCellLine/create");

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
	
		// process accession ids
		if (domain.getEditAccessionIds() != null && !domain.getEditAccessionIds().isEmpty()) {
			accessionService.process(String.valueOf(entity.get_cellline_key()), domain.getEditAccessionIds(), mgiTypeName, user);
		}
		
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
		
		// process accession ids
		if (domain.getEditAccessionIds() != null && !domain.getEditAccessionIds().isEmpty()) {
			accessionService.process(domain.getCellLineKey(), domain.getEditAccessionIds(), mgiTypeName, user);
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
	public SearchResults<CellLineDomain> getMutantCellLineCount() {
		// return the object count from the database
		
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		String cmd = "select count(*) as objectCount from all_cellline where isMutant = 1";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional	
	public SearchResults<CellLineDomain> getParentCellLineCount() {
		// return the object count from the database
		
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		String cmd = "select count(*) as objectCount from all_cellline where isMutant = 0";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
		
    @Transactional
    public SearchResults<CellLineDomain> createMutantCellLine (String alleleTypeKey, CellLineDomain domain, User user) {
		// potential new mutant cell line for allele/cellline association
    	// depends on isParent, isMutation settings (see below)
    	// applies only to domain.getMutatnCellLineAssocs().get(0)
    	
    	SearchResults<CellLineDomain> cellLineResults = new SearchResults<CellLineDomain>();

    	if (domain.getProcessStatus().equals(Constants.PROCESS_NOTDIRTY) || domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
	    	log.info("createMutantCellLine/do nothing/returning cellLineResults");
	    	log.info("cellLineResults: " + cellLineResults);
	    	return(cellLineResults);
		}

    	String cellLineNS = "Not Specified";
    	String anyNS = "-1";
    	String isMutantTrue = "1";
		String cellLineTypeKey = domain.getDerivation().getParentCellLine().getCellLineTypeKey();
		
		SlimAlleleCellLineDerivationDomain derivationSearch = new SlimAlleleCellLineDerivationDomain();
		List<AlleleCellLineDerivationDomain> derivationResults = new ArrayList<AlleleCellLineDerivationDomain>();
    	CellLineDomain cellLineDomain = new CellLineDomain();
    	
		Boolean isParent = true;
		Boolean isMutant = true;
		
        // default cellLineType = Embryonic Stem Cell (3982968)		
		if (cellLineTypeKey.isEmpty()) {
          isParent = false;
          cellLineTypeKey = "3982968";          
        };

        // if domain cell line has not been selected...
        if (domain.getCellLineKey().isEmpty()) {
        	isMutant = false;
		}
 
    	log.info("createMutantCellLine/isParent/isMutant: " + isParent + "," + isMutant);       
       
        if ((isParent == false && isMutant == false)
        		&& (alleleTypeKey.equals("847121") || alleleTypeKey.equals("847116"))) {

        	log.info("createMutantCellLine/isParent == false && isMutant == false, Gene trapped or Targeted");

	        // select the derivation key that is associated with:
	        //   allele type = from domain
	        //   creator = Not Specified (3982966)
	        //   vector = Not Specified (4311225)           		
	        //   vector type = Not Specified (3982979)
	        //   parent cell line = Not Specified (-1)
	        //   strain = Not Specified (-1)
	        //   cell line type
	        //
            	
        	derivationSearch.setAlleleTypeKey(alleleTypeKey);
        	derivationSearch.setVectorKey("4311225");
        	derivationSearch.setVectorTypeKey("3982979");
        	derivationSearch.setParentCellLineKey(anyNS);
        	derivationSearch.setCreatorKey("3982966");
        	derivationSearch.setStrainKey(anyNS);
        	derivationSearch.setCellLineTypeKey(cellLineTypeKey);
        		
        	derivationResults = derivationService.validateDerivation(derivationSearch);
        		
        	if (!derivationResults.isEmpty()) {       		
	        	log.info("createMutantCellLine/validated derivation: " + derivationResults.get(0).getDerivationKey());       		
	        	cellLineDomain.setCellLine(cellLineNS);
	        	cellLineDomain.setStrainKey("-1");
	        	cellLineDomain.setCellLineTypeKey(cellLineTypeKey);
	        	cellLineDomain.setDerivation(derivationResults.get(0));				
	        	cellLineDomain.setIsMutant("1");
	        	log.info("createMutantCellLine/create new cell line");
	        	cellLineResults = create(cellLineDomain, user);
        	}
        	else {
        		cellLineResults.error = "Cannot find Derivation for this Allele Type and Parent";
        	}
        }
        else if (isParent == true && isMutant == false) {
 
        	log.info("createMutantCellLine/isParent == true && isMutant == false");

	        //
	        // select the derivation key that is associated with:
	        //   allele type = from domain
	        //   creator = Not Specified (3982966)
	        //   vector = Not Specified (4311225)           		
	        //   vector type = Not Specified (3982979)
	        //   parent cell line = from domain
	        //   strain = from domain
	        //   cell line type
	        // 
            	
        	derivationSearch.setAlleleTypeKey(alleleTypeKey);
        	derivationSearch.setVectorKey("4311225");
        	derivationSearch.setVectorTypeKey("3982979");
        	derivationSearch.setParentCellLineKey(domain.getDerivation().getParentCellLine().getCellLineKey());
        	derivationSearch.setCreatorKey("3982966");
        	derivationSearch.setStrainKey(domain.getDerivation().getParentCellLine().getStrainKey());
        	derivationSearch.setCellLineTypeKey(cellLineTypeKey);
        		
        	derivationResults = derivationService.validateDerivation(derivationSearch);
	
        	if (!derivationResults.isEmpty()) {       		
	        	log.info("createMutantCellLine/validated derivation: " + derivationResults.get(0).getDerivationKey());       		
	        	cellLineDomain.setCellLine(cellLineNS);
	        	cellLineDomain.setStrainKey(anyNS);
	        	cellLineDomain.setCellLineTypeKey(cellLineTypeKey);
	        	cellLineDomain.setDerivation(derivationResults.get(0));				
	        	cellLineDomain.setIsMutant(isMutantTrue);
	        	log.info("createMutantCellLine/create new cell line");
	        	cellLineResults = create(cellLineDomain, user);
        	}
        	else {
        		cellLineResults.error = "Cannot find Derivation for this Allele Type and Parent";
        	}       	
        }
        else if (isParent == true && isMutant == true && domain.getCellLine().equals(cellLineNS)) {
 
        	log.info("createMutantCellLine/isParent == true && isMutant == true && cellline = Not Specified");
        	
	        //
	        // select the derivation key that is associated with:
	        //   allele type = from domain
	        //   creator = from domain
	        //   vector = from domain           		
	        //   vector type = from domain
	        //   parent cell line = from domain
	        //   strain = from domain
	        //   cell line type = from domain
	        // 
            	
        	derivationSearch.setAlleleTypeKey(alleleTypeKey);
        	derivationSearch.setVectorKey(domain.getDerivation().getVectorKey());
        	derivationSearch.setVectorTypeKey(domain.getDerivation().getVectorTypeKey());
        	derivationSearch.setParentCellLineKey(domain.getDerivation().getParentCellLine().getCellLineKey());
        	derivationSearch.setCreatorKey(domain.getDerivation().getCreatorKey());
        	derivationSearch.setStrainKey(domain.getDerivation().getParentCellLine().getStrainKey());
        	derivationSearch.setCellLineTypeKey(domain.getDerivation().getParentCellLine().getCellLineTypeKey());
        		
        	derivationResults = derivationService.validateDerivation(derivationSearch);
	
        	if (!derivationResults.isEmpty()) {       		
	        	log.info("createMutantCellLine/validated derivation: " + derivationResults.get(0).getDerivationKey());       		
	        	cellLineDomain.setCellLine(cellLineNS);
	        	cellLineDomain.setStrainKey(domain.getDerivation().getParentCellLine().getStrainKey());
	        	cellLineDomain.setCellLineTypeKey(domain.getDerivation().getParentCellLine().getCellLineTypeKey());
	        	cellLineDomain.setDerivation(derivationResults.get(0));				
	        	cellLineDomain.setIsMutant(isMutantTrue);
	        	cellLineResults = create(cellLineDomain, user);
	        	log.info("createMutantCellLine/create new cell line");	        	
        	}
        	else {
        		cellLineResults.error = "Cannot find Derivation for this Allele Type and Parent";
        	}       	
        }
              
        return(cellLineResults);	        		
    } 

	@Transactional
	public List<SlimCellLineDomain> searchMutantCellLines(CellLineDomain searchDomain) {

		List<SlimCellLineDomain> results = new ArrayList<SlimCellLineDomain>();

		String cmd = "";
		String select = "select distinct c._cellline_key, c.cellLine";
		String from = "from all_cellline_view c";
		String where = "where c.isMutant = 1";
		String orderBy = "order by c.cellLine";		
		Boolean from_allele = false;
		Boolean from_accession = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("c", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getCellLine() != null && !searchDomain.getCellLine().isEmpty()) {
			where = where + "\nand c.cellLine ilike '" + searchDomain.getCellLine() + "'" ;
		}

		if (searchDomain.getCellLineTypeKey() != null && !searchDomain.getCellLineTypeKey().isEmpty()) {
			where = where + "\nand c._cellline_type_key = " + searchDomain.getCellLineTypeKey();
		}

		if (searchDomain.getStrainKey() != null && !searchDomain.getStrainKey().isEmpty()) {
			where = where + "\nand c._strain_key = " + searchDomain.getStrainKey();
		}
		
		if (searchDomain.getDerivation() != null) {
			if (searchDomain.getDerivation().getCreatorKey() != null && !searchDomain.getDerivation().getCreatorKey().isEmpty()) {
				where = where + "\nand c._creator_key = " + searchDomain.getDerivation().getCreatorKey();
			}
			
			if (searchDomain.getDerivation().getDerivationTypeKey() != null && !searchDomain.getDerivation().getDerivationTypeKey().isEmpty()) {
				where = where + "\nand c._derivationtype_key = " + searchDomain.getDerivation().getDerivationTypeKey();
			}
			
			if (searchDomain.getDerivation().getVectorKey() != null && !searchDomain.getDerivation().getVectorKey().isEmpty()) {
				where = where + "\nand c._vector_key = " + searchDomain.getDerivation().getVectorKey();
			}		
			else if (searchDomain.getDerivation().getVector() != null && !searchDomain.getDerivation().getVector().isEmpty()) {
				where = where + "\nand c.vector ilike '" + searchDomain.getDerivation().getVector() + "'";
			}
			
			if (searchDomain.getDerivation().getVectorTypeKey() != null && !searchDomain.getDerivation().getVectorTypeKey().isEmpty()) {
				where = where + "\nand c._vectortype_key = " + searchDomain.getDerivation().getVectorTypeKey();
			}
			
			if (searchDomain.getDerivation().getParentCellLine() != null) {
				if (searchDomain.getDerivation().getParentCellLine().getCellLineKey() != null && !searchDomain.getDerivation().getParentCellLine().getCellLineKey().isEmpty()) {
					where = where + "\nand c.parentCellLine_key = " + searchDomain.getDerivation().getParentCellLine().getCellLineKey();
				}
				else if (searchDomain.getDerivation().getParentCellLine().getCellLine() != null && !searchDomain.getDerivation().getParentCellLine().getCellLine().isEmpty()) {
					where = where + "\nand c.parentCellLine ilike '" + searchDomain.getDerivation().getParentCellLine().getCellLine() + "'";
				}
				
				if (searchDomain.getDerivation().getParentCellLine().getStrainKey() != null && !searchDomain.getDerivation().getParentCellLine().getStrainKey().isEmpty()) {
					where = where + "\nand c.parentCellLineStrain_key = " + searchDomain.getDerivation().getParentCellLine().getStrainKey();
				}
				else if (searchDomain.getDerivation().getParentCellLine().getStrain() != null && !searchDomain.getDerivation().getParentCellLine().getStrain().isEmpty()) {
					where = where + "\nand c.parentcelllinestrain ilike '" + searchDomain.getDerivation().getParentCellLine().getStrain() + "'";
				}
				
				if (searchDomain.getDerivation().getParentCellLine().getCellLineTypeKey() != null && !searchDomain.getDerivation().getParentCellLine().getCellLineTypeKey().isEmpty()) {
					where = where + "\nand c._cellline_type_key = " + searchDomain.getDerivation().getParentCellLine().getCellLineTypeKey();
				}				
			}
		}		
		
		if (searchDomain.getAlleleSymbols() != null && !searchDomain.getAlleleSymbols().isEmpty()) {
			where = where + "\nand a.symbol ilike '" + searchDomain.getAlleleSymbols() + "'";
			from_allele = true;
		}
		
		if (searchDomain.getEditAccessionIds() != null && !searchDomain.getEditAccessionIds().isEmpty()) {
			if (searchDomain.getEditAccessionIds().get(0).getAccID() != null && !searchDomain.getEditAccessionIds().get(0).getAccID().isEmpty()) {	
				where = where + "\nand acc.accID ilike '" + searchDomain.getEditAccessionIds().get(0).getAccID() + "'";
				from_accession = true;
			}			
		}
		
		if (from_allele == true) {
			from = from + ",all_allele_cellline_view a";
			where = where + "\nand a._MutantCellLine_key = c._CellLine_key";
		}
		
		if (from_accession == true) {
			from = from + ",all_cellline_acc_view av";
			where = where + "\nand a._MutantCellLine_key = av._object_key"
					+ "\nand av._mgitype_key = 28";
		}
		
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n";
		log.info(cmd);
		
		try {			
			ResultSet rs = sqlExecutor.executeProto(cmd);			
			while (rs.next()) {
				SlimCellLineDomain domain = new SlimCellLineDomain();
				domain = slimtranslator.translate(cellLineDAO.get(rs.getInt("_cellline_key")));	
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
	public List<CellLineDomain> searchParentCellLines() {

		List<CellLineDomain> results = new ArrayList<CellLineDomain>();
		
		String cmd = "\nselect _CellLine_key from ALL_CellLine where isMutant = 0 order by cellLine";

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
	public List<CellLineDomain> validateMutantCellLine(CellLineDomain searchDomain) {

		List<CellLineDomain> results = new ArrayList<CellLineDomain>();
		
		String cmd = "\nselect _cellline_key"
				+ "\nfrom ALL_CellLine"
				+ "\nwhere isMutant = 1"
				+ "\nand cellLine ilike '" + searchDomain.getCellLine() + "'";

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
				+ "\nand cellLine ilike " + searchDomain.getCellLine() + "'";

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
