package org.jax.mgi.mgd.api.model.gxd.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDAO;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AllelePairDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;
import org.jax.mgi.mgd.api.model.gxd.translator.AllelePairTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.RunCommand;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AllelePairService extends BaseService<AllelePairDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AllelePairDAO allelePairDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private AlleleDAO alleleDAO;
	@Inject
	private AlleleCellLineDAO alleleCellLineDAO;
	@Inject
	private TermDAO termDAO;
	
	private AllelePairTranslator translator = new AllelePairTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AllelePairDomain> create(AllelePairDomain domain, User user) {
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<AllelePairDomain> update(AllelePairDomain domain, User user) {
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<AllelePairDomain> delete(Integer key, User user) {
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public AllelePairDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AllelePairDomain domain = new AllelePairDomain();
		if (allelePairDAO.get(key) != null) {
			domain = translator.translate(allelePairDAO.get(key));
		}
		allelePairDAO.clear();
		return domain;
	}

	@Transactional
	public SearchResults<AllelePairDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setItem(translator.translate(allelePairDAO.get(key)));
		allelePairDAO.clear();
		return results;
	}

	@Transactional	
	public List<AllelePairDomain> search(Integer key) {
		// using searchDomain fields, generate SQL command
		
		List<AllelePairDomain> results = new ArrayList<AllelePairDomain>();

		String cmd = "\nselect * from gxd_allelepair"
				+ "\nwhere _allelepair_key = " + key
				+ "\norder by sequencenum";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AllelePairDomain domain = new AllelePairDomain();	
				domain = translator.translate(allelePairDAO.get(rs.getInt("_allelepair_key")));
				allelePairDAO.clear();
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
	public Boolean process(String parentKey, List<AllelePairDomain> domain, User user) {
		// process image pane (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processAllelePair");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processImagePane/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				if(domain.get(i).getMarkerKey().isEmpty()
					|| domain.get(i).getAlleleKey1().isEmpty()) {
					continue;
				}
				
				log.info("processAllelePair/create");

				AllelePair entity = new AllelePair();	
								
				entity.setGenotype(genotypeDAO.get(Integer.valueOf(parentKey)));				
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));				
				entity.setAllele1(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey1())));				
				
				if (domain.get(i).getAlleleKey2() != null && !domain.get(i).getAlleleKey2().isEmpty()) {
					entity.setAllele2(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey2())));				
				}
								
				if (domain.get(i).getCellLineKey1() != null && !domain.get(i).getCellLineKey1().isEmpty()) {
					entity.setCellLine1(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLineKey1())));				
				}
					
				if (domain.get(i).getCellLineKey2() != null && !domain.get(i).getCellLineKey2().isEmpty()) {
					entity.setCellLine2(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLineKey2())));				
				}
				
				// default compound = Not Specified
				if (domain.get(i).getCompoundKey() == null || domain.get(i).getCompoundKey().isEmpty()) {
					domain.get(i).setCompoundKey("847167");
				}
				
				log.info("processAllelePair/create/6");
				
				log.info("pair state: " + domain.get(i).getPairStateKey());
				entity.setPairState(termDAO.get(Integer.valueOf(domain.get(i).getPairStateKey())));
				
				log.info("compound: " + domain.get(i).getCompoundKey());				
				entity.setCompound(termDAO.get(Integer.valueOf(domain.get(i).getCompoundKey())));
				
				log.info("sequencenum: " + domain.get(i).getSequenceNum());								
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				
				// add creation/modification 
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				
				allelePairDAO.persist(entity);				
				log.info("processAllelePair/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAllelePair/delete");
				AllelePair entity = allelePairDAO.get(Integer.valueOf(domain.get(i).getAllelePairKey()));
				allelePairDAO.remove(entity);
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAllelePair/update");
				Boolean isUpdated = false;
				AllelePair entity = allelePairDAO.get(Integer.valueOf(domain.get(i).getAllelePairKey()));

				if (!String.valueOf(entity.getMarker().get_marker_key()).equals(domain.get(i).getMarkerKey())) {
					entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
					isUpdated = true;
				}
				
				if (!String.valueOf(entity.getAllele1().get_allele_key()).equals(domain.get(i).getAlleleKey1())) {
					entity.setAllele1(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey1())));
					isUpdated = true;
				}

				if (entity.getAllele2() == null 
						&& domain.get(i).getAlleleKey2() != null) {
					entity.setAllele2(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey2())));
					isUpdated = true;
				}
				else if (entity.getAllele2() != null 
						&& (domain.get(i).getAlleleKey2() == null || domain.get(i).getAlleleKey2().isEmpty())) {
						entity.setAllele2(null);
						isUpdated = true;
				}
				else if (entity.getAllele2() != null 
						&& domain.get(i).getAlleleKey2() != null
						&& !String.valueOf(entity.getAllele2().get_allele_key()).equals(domain.get(i).getAlleleKey2())) {
					entity.setAllele2(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey2())));
					isUpdated = true;
				}	
				
				if (entity.getCellLine1() == null
						&& domain.get(i).getCellLine1() != null) {
					entity.setCellLine1(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine1())));
					isUpdated = true;
				}
				else if (entity.getCellLine1() != null
						&& (domain.get(i).getCellLine1() == null || domain.get(i).getCellLine1().isEmpty())) {				
					entity.setCellLine1(null);
					isUpdated = true;
				}
				else if (entity.getCellLine1() != null
						&& domain.get(i).getCellLine1() != null
						&& !String.valueOf(entity.getCellLine1().get_cellline_key()).equals(domain.get(i).getCellLine1())) {
					entity.setCellLine1(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine1())));
					isUpdated = true;
				}

				if (entity.getCellLine2() == null
						&& domain.get(i).getCellLine2() != null) {
					entity.setCellLine2(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine2())));
					isUpdated = true;
				}
				else if (entity.getCellLine2() != null
						&& (domain.get(i).getCellLine2() == null || domain.get(i).getCellLine2().isEmpty())) {				
					entity.setCellLine2(null);
					isUpdated = true;
				}
				else if (entity.getCellLine2() != null
						&& domain.get(i).getCellLine2() != null				
						&& !String.valueOf(entity.getCellLine2().get_cellline_key()).equals(domain.get(i).getCellLine2())) {
					entity.setCellLine2(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine2())));
					isUpdated = true;
				}
								
				if (!String.valueOf(entity.getPairState().get_term_key()).equals(domain.get(i).getPairStateKey())) {
					entity.setPairState(termDAO.get(Integer.valueOf(domain.get(i).getPairStateKey())));
					isUpdated = true;
				}
				
				if (!String.valueOf(entity.getCompound().get_term_key()).equals(domain.get(i).getCompoundKey())) {
					entity.setCompound(termDAO.get(Integer.valueOf(domain.get(i).getCompoundKey())));
					isUpdated = true;
				}
				
				if (!entity.getSequenceNum().equals(domain.get(i).getSequenceNum())) {
					entity.setSequenceNum(domain.get(i).getSequenceNum());
					isUpdated = true;					
				}
				
				if (isUpdated) {
					log.info("processAllelePair modified == true");
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					allelePairDAO.update(entity);
					log.info("processAllelePair/changes processed: " + domain.get(i).getAllelePairKey());
					modified = true;
				}
				else {
					log.info("processAllelePair/no changes processed: " + domain.get(i).getAllelePairKey());
				}
			}
			else {
				log.info("processAllelePair/no changes processed: " + domain.get(i).getAllelePairKey());
			}
		}
		
		log.info("processAllelePair/processing successful");
		return modified;
	}

	@Transactional
	public SearchResults<AllelePairDomain> validateAlleleState(List<AllelePairDomain> domain) {
		// validate the Allele State & Alleles
		// returns error string
		
		// return same domain; will setError if necessary
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		
		String pairStateKey;
		String alleleKey1;
		String alleleKey2;
		
		for (int i = 0; i < domain.size(); i++) {
		
			pairStateKey = domain.get(i).getPairStateKey();
			alleleKey1 = domain.get(i).getAlleleKey1();
			alleleKey2 = domain.get(i).getAlleleKey2();
			
			// no values/do nothing
			if (alleleKey1 == null && alleleKey2 == null) {
				continue;
			}
			
			results.setItem(domain.get(i));

	        //if (pairState = "Homozygous" or "Heterozygous") and (alleleKey2 = "" or "NULL")
  			if ((pairStateKey.equals("847138") || pairStateKey.equals("847137")) && alleleKey2 == null ) {
  				results.setError("If Allele State = 'Homozygous' or 'Heterozygous', then Allele 2 must exist.", null, Constants.HTTP_SERVER_ERROR);
  				return results;
  			}

  			//if pairState = "Homozygous" and alleleKey2 != "" and alleleKey1 != alleleKey2
  			if (pairStateKey.equals("847138") && alleleKey2 != null && !alleleKey1.equals(alleleKey2)) {
  				results.setError("If Allele State = 'Homozygous', then Allele 1 must equal Allele 2.", null, Constants.HTTP_SERVER_ERROR);
  				return results;
  			}
  			
  			//if pairState = "Heterozygous" and alleleKey1 = alleleKey2
  			if (pairStateKey.equals("847137") && alleleKey1.equals(alleleKey2)) {
  				results.setError("If Allele State = 'Heterozygous', then Allele 2 must exist but Allele 1 cannot equal Allele 2.", null, Constants.HTTP_SERVER_ERROR);
  				return results;
  			}
 
  			//	    847133 | Hemizygous X-linked
  			//	    847134 | Hemizygous Y-linked
  			//	    847135 | Hemizygous Insertion
  			//	    847136 | Hemizygous Deletion
  			//	    847139 | Indeterminate
  			//	   7107400 | Homoplasmic
  			//	   7107401 | Heteroplasmic 			
  			//and alleleKey2 != "" and != "NULL"
  			if ((pairStateKey.equals("847133") 
  					|| pairStateKey.equals("847134")
  					|| pairStateKey.equals("847135")
  					|| pairStateKey.equals("847136")
  					|| pairStateKey.equals("847139")
  					|| pairStateKey.equals("7107400")
  					|| pairStateKey.equals("7107401")
  					)
  					&& alleleKey2 != null && !alleleKey2.isEmpty()) {
  				results.setError("For this Allele State, only Allele 1 is required.", null, Constants.HTTP_SERVER_ERROR);
  				return results;
  			} 			
			
		}
		
		// setError() is null/empty
		return results;
	}

	@Transactional
	public SearchResults<AllelePairDomain> validateMutantCellLines(AllelePairDomain domain) {
		// validate the Allele/MCL associations  
		// returns SearchResults.error = null if valid = true
		// else return SearchResults.error = error message
	    
		// return same domain; will add "error" messages if necessary
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setItem(domain);				
	
		String error = "";
		Boolean isValidMCL1 = false;
		Boolean isValidMCL2 = false;
		
		String alleleKey1 = domain.getAlleleKey1();
		String alleleKey2 = domain.getAlleleKey2();
		String cellLine1 = domain.getCellLine1();
		String cellLine2 = domain.getCellLine2();
		
		// no values/do nothing
		if (cellLine1.isEmpty() && cellLine2.isEmpty()) {
			return results;
		}
	
		if (alleleKey1.isEmpty() && !cellLine1.isEmpty()) {
			results.setError("Allele 1 is empty/missing", null, Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		if (alleleKey2.isEmpty() && !cellLine2.isEmpty() ) {
			results.setError("Allele 2 is empty/missing", null, Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		String cmd = "\nselect c._cellLine_key, c.cellline"
				+ "\nfrom all_cellLine c, all_allele_cellline a"
				+ "\nwhere c.isMutant = 1"
				+ "\nand c._cellLine_key = a._mutantcellLine_key"
				+ "\nand c.cellline = '" + cellLine1 + "'"
				+ "\nand a._allele_key = " + alleleKey1;
		
		if (cellLine2 != null && !cellLine2.isEmpty()) {
			cmd = cmd + "\nunion" +
				"\nselect c._cellLine_key, c.cellline from all_cellline c, all_allele_cellline a"
				+ "\nwhere c.isMutant = 1"
				+ "\nand c._cellLine_key = a._mutantcellLine_key"
				+ "\nand c.cellline = '" + cellLine2 + "'"
				+ "\nand a._allele_key = " + alleleKey2;				
		}
		else {
			isValidMCL2 = true;
		}
	
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				if (rs.getString("cellline").equals(cellLine1)) {
					isValidMCL1 = true;
				}
				if (rs.getString("cellline").equals(cellLine2)) {
					isValidMCL2 = true;
				}				
			}
			sqlExecutor.cleanup();
						
			if (isValidMCL1 == false) {
				error = "Mutant Cell Line 1 is invalid; ";
			}

			if (isValidMCL2 == false) {
				error = error + "Mutant Cell Line 2 is invalid";
			}
			
			if (isValidMCL1 == false || isValidMCL2 == false) {
				results.setError(error, null, Constants.HTTP_SERVER_ERROR);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		return results;
	}

	@Transactional		
	public Boolean alleleCombinationUtilities(String genotypeKey) throws IOException, InterruptedException {
		// see allcacheload/allelecombinationByGenotype.py
		
		// these swarm variables are in 'app.properties'
    	String utilitiesScript = System.getProperty("swarm.ds.alleleCombinationUtilities");
    	String server = System.getProperty("swarm.ds.dbserver");
        String db = System.getProperty("swarm.ds.dbname");
        String username = System.getProperty("swarm.ds.username");
        String pwd = System.getProperty("swarm.ds.dbpasswordfile");
        
        // input:  genotypeKey

        // output: true/false
        Boolean returnCode = false;
        
		String runCmd = utilitiesScript;
        runCmd = runCmd + " -S" + server;
        runCmd = runCmd + " -D" + db;
        runCmd = runCmd + " -U" + username;
        runCmd = runCmd + " -P" + pwd;
        runCmd = runCmd + " -K" + genotypeKey;
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			returnCode = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);
			returnCode = false;
		}			
		
		return returnCode;
	}
	
}