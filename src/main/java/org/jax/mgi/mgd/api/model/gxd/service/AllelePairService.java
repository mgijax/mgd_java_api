package org.jax.mgi.mgd.api.model.gxd.service;

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
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;
import org.jax.mgi.mgd.api.model.gxd.translator.AllelePairTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AllelePairService extends BaseService<AllelePairDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AllelePairDAO allelePairDAO;
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
		return domain;
	}

	@Transactional
	public SearchResults<AllelePairDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setItem(translator.translate(allelePairDAO.get(key)));
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
				log.info("processAllelePair create");
				AllelePair entity = new AllelePair();	
				entity.set_allelepair_key(Integer.valueOf(parentKey));
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

				entity.setPairState(termDAO.get(Integer.valueOf(domain.get(i).getPairStateKey())));
				entity.setCompound(termDAO.get(Integer.valueOf(domain.get(i).getCompoundKey())));
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				
				allelePairDAO.persist(entity);				
				modified = true;
				log.info("processAllelePair/create/returning results");					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAllelePair delete");
				AllelePair entity = allelePairDAO.get(Integer.valueOf(domain.get(i).getAllelePairKey()));
				allelePairDAO.remove(entity);
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAllelePair update");
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

				if (entity.getAllele2() == null && 
						(domain.get(i).getAlleleKey2() != null || !domain.get(i).getAlleleKey2().isEmpty())) {
					entity.setAllele2(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey2())));
					isUpdated = true;
				}
				else if (entity.getAllele2() != null && 
						(domain.get(i).getAlleleKey2() == null || domain.get(i).getAlleleKey2().isEmpty())) {				
					entity.setAllele2(null);
					isUpdated = true;
				}
				else if (!String.valueOf(entity.getAllele2().get_allele_key()).equals(domain.get(i).getAlleleKey2())) {
					entity.setAllele2(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey2())));
					isUpdated = true;
				}	
				
				if (entity.getCellLine1() == null && 
						(domain.get(i).getCellLine1() != null || !domain.get(i).getCellLine1().isEmpty())) {
					entity.setCellLine1(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine1())));
					isUpdated = true;
				}
				else if (entity.getCellLine1() != null && 
						(domain.get(i).getCellLine1() == null || domain.get(i).getCellLine1().isEmpty())) {				
					entity.setCellLine1(null);
					isUpdated = true;
				}
				else if (!String.valueOf(entity.getCellLine1().get_cellline_key()).equals(domain.get(i).getCellLine1())) {
					entity.setCellLine1(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine1())));
					isUpdated = true;
				}
				
				if (entity.getCellLine2() == null && 
						(domain.get(i).getCellLine2() != null || !domain.get(i).getCellLine2().isEmpty())) {
					entity.setCellLine2(alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getCellLine2())));
					isUpdated = true;
				}
				else if (entity.getCellLine2() != null && 
						(domain.get(i).getCellLine2() == null || domain.get(i).getCellLine2().isEmpty())) {				
					entity.setCellLine2(null);
					isUpdated = true;
				}
				else if (!String.valueOf(entity.getCellLine2().get_cellline_key()).equals(domain.get(i).getCellLine2())) {
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
				
				if (!String.valueOf(entity.getSequenceNum()).equals(domain.get(i).getSequenceNum())) {
					entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
					isUpdated = true;					
				}
				if (isUpdated) {
					log.info("processAllelePair modified == true");
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					allelePairDAO.update(entity);
					modified = true;
					log.info("processAllelePair/changes processed: " + domain.get(i).getAllelePairKey());
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
	public SearchResults<AllelePairDomain> validateAlleleState(AllelePairDomain domain) {
		// validate the Allele State & Alleles
		// returns SearchResults.error = null if valid = true
		// else return SearchResults.error = error message
	    
		// Homozygous            Allele1 = Allele 2
	    //
	    // Heterozygote          Allele1 is NOT = Allele 2
	    //
		// Hemizygous X linked   Allele2 is null 
	    //	                     Marker chromosome = "X"
	    //	                     Compound = Not Applicable
	    //
		// Hemizygous Y linked   Allele2 is null
	    //	                     and Marker chromosome  "Y" 
	    //	                     and Compound = Not Applicable
	    //
		// Hemizygous Insertion  Allele2 is null
	    //
		// Hemizygous Deletion   Allele2 is null
	    //	                     Compound = Top or Bottom
	    //
		// Indeterminate         Allele2 is null
	    //	                     Compound = Not Applicable
	    //
		// Homoplasmic           no allele pair state error checking
	    // Heteroplasmic         no allele pair state error checking
	    //
		
		// return same domain; will add "error" messages if necessary
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setItem(domain);				

		Boolean isValid = false;
		String error = "";
		
		String pairState = domain.getPairState();
		String alleleKey1 = domain.getAlleleKey1();
		String alleleKey2 = domain.getAlleleKey2();
		String markerChr = domain.getMarkerChromosome();
		String compound = domain.getCompound();
		
		// no values/do nothing
		if (alleleKey1 == null && alleleKey2 == null) {
			return results;
		}
		
		if (pairState.equals("Homoplasmic")
			|| pairState.equals("Heteroplasmic")) {
			isValid = true;
		}
		
		if (pairState.equals("Homozygous")) {
			if (alleleKey2 != null 
				&& !alleleKey2.isEmpty() 
				&& alleleKey1.equals(alleleKey2)) {
				isValid = true;
			}
			else {
				error = "Homozgous: Allele1 must equal Allele 2";
			}
		}
		
		if (pairState.equals("Heterozygote")) {
			if (alleleKey2 != null 
				&& !alleleKey2.isEmpty()
				&& !alleleKey1.equals(alleleKey2)) {
				isValid = true;
			} else {
				error = "Heretorzygote: Allele2 must NOT be null and Allele1 must NOT equal Allele 2";
			}
		}
		
		if (pairState.equals("Hemizygous X-linked")) {
			if (alleleKey2 == null 
				&& markerChr.equals("X")
				&& compound.equals("Not Applicable")) {
				isValid = true;
			}
			else {
				error = "Hemizygous X-linked: Allele2 must be null, Marker chromosome must be (X), and Compound must be (Not Applicable)";
			}
		}
		
		if (pairState.equals("Hemizygous Y-linked")) {
			if (alleleKey2 == null 
				&& markerChr.equals("Y")
				&& compound.equals("Not Applicable")) {
				isValid = true;
			}
			else {
				error = "Hemizygous Y-linked: Allele2 must be null, Marker chromosome must be (Y), and Compound must be (Not Applicable)";
			}
		}
		
		if (pairState.equals("Hemizygous Insertion")) {
			if (alleleKey2 == null) {
				isValid = true;
			} else {
				error = "Hemizygous Insertion: Allele2 must be null";
			}
		}

		if (pairState.equals("Hemizygous Deletion")) {
			if (alleleKey2 == null && !compound.equals("Not Applicable")) {
				isValid = true;
			} else {
				error = "Hemizygous Deletion: Allele2 must be null, and Compound must be in (Top, Bottem)";
			}
		}
		
		if (pairState.equals("Indeterminate")) {
			if (alleleKey2 == null && compound.equals("Not Applicable")				) {
				isValid = true;
			} else {
				error = "Indeterminate: Allele2 must be null, and Compound must be (Not Applicable)";
			}
		}
		
		if (isValid == false) {
			results.setError("Validate Allele State failed", error, Constants.HTTP_SERVER_ERROR);	
		}
		
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
		String cellLineKey1 = domain.getCellLineKey1();
		String cellLineKey2 = domain.getCellLineKey2();

		// no values/do nothing
		if (cellLineKey1 == null && cellLineKey2 == null) {
			return results;
		}
	
		String cmd = "\nselect c._cellLine_key "
				+ "\nfrom all_cellLine c, all_allele_cellline a"
				+ "\nwhere c.isMutant = 1"
				+ "\nand c._cellLine_key = a._mutantcellLine_key"
				+ "\nand c._cellLine_key = " + cellLineKey1
				+ "\nand a._allele_key = " + alleleKey1;
		
		if (cellLineKey2 != null && !cellLineKey2.isEmpty()) {
			cmd = cmd + "\nunion" +
				"\nselect c._cellLine_key from all_cellline c, all_allele_cellline a"
				+ "\nwhere c.isMutant = 1"
				+ "\nand c._cellLine_key = a._mutantcellLine_key"
				+ "\nand c._cellLine_key = " + cellLineKey2
				+ "\nand a._allele_key = " + alleleKey2;				
		}
	
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				if (rs.getString("_cellline_key").equals(cellLineKey1)) {
					isValidMCL1 = true;
				}
				if (rs.getString("_cellline_key").equals(cellLineKey2)) {
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
				results.setError("Validate Mutant Cell Line failed", error, Constants.HTTP_SERVER_ERROR);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		return results;
	}
		
}