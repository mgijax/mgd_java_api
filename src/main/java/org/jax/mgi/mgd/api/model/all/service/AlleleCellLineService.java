package org.jax.mgi.mgd.api.model.all.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDAO;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.model.all.translator.AlleleCellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleCellLineService extends BaseService<AlleleCellLineDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleCellLineDAO alleleCellLineDAO;
	@Inject
	private CellLineDAO cellLineDAO;
	@Inject
	private AlleleCellLineDerivationService derivationService;
	
	private AlleleCellLineTranslator translator = new AlleleCellLineTranslator();				

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
		if (alleleCellLineDAO.get(key) != null) {
			domain = translator.translate(alleleCellLineDAO.get(key));
		}
		alleleCellLineDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AlleleCellLineDomain> getResults(Integer key) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setItem(translator.translate(alleleCellLineDAO.get(key)));
		alleleCellLineDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, String alleleTypeKey, String alleleType, List<AlleleCellLineDomain> domain, User user) {
		// process allele cell line (create, delete, update)
		
		Boolean modified = false;

		Boolean isParent = true;
		Boolean isMutant = true;
		Boolean addCellLine = false;
		Boolean addAssociation = true;
        Boolean getDerivation = true;
        
        String mutantCellLine;
        String mutantCellLineKey;
        String creatorKey;
        String vectorKey;
        
		SlimAlleleCellLineDerivationDomain derivationSearch = new SlimAlleleCellLineDerivationDomain();
		List<AlleleCellLineDerivationDomain> derivationResults = new ArrayList<AlleleCellLineDerivationDomain>();

		log.info("processAlleleCellLine");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAlleleCellLine/nothing to process");
			return modified;
		}
        
        // set the allele type and type key
        // set the parent
        // NOTE:  use the PARENT strain (not the Strain of Origin)
        // set the strain
        // set the derivation
        String parentCellLineKey = domain.get(0).getMutantCellLine().getDerivation().getParentCellLineKey();
        String strainKey = domain.get(0).getMutantCellLine().getStrainKey();     		       		
        String strainName = domain.get(0).getMutantCellLine().getStrain();
        String cellLineTypeKey = domain.get(0).getMutantCellLine().getCellLineTypeKey();
		
        // set the isParent

        if (parentCellLineKey.isEmpty()) {
          isParent = false;
        };

        // default cellLineType = Embryonic Stem Cell (3982968)
        if (cellLineTypeKey.isEmpty()) {
          cellLineTypeKey = "3982968";
        };
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
//            mutantCellLine = domain.get(i).getMutantCellLine();
            mutantCellLineKey = domain.get(i).getMutantCellLine().getCellLineKey();
            creatorKey = domain.get(i).getMutantCellLine().getDerivation().getCreatorKey();
            vectorKey = domain.get(i).getMutantCellLine().getDerivation().getVectorKey();
			
            if (mutantCellLineKey.isEmpty()) {
            	isMutant = false;
			}
            
            //
            // check isParent, isMutant
            //

            if (isParent == false && isMutant == false) {
 
            	// not specified
            	//if = "Gene trapped" or "Targeted"
            	if (alleleTypeKey.equals("847121") || alleleTypeKey.equals("847116")) {

		            // select the derivation key that is associated with:
		            //   allele type
		            //   creator = Not Specified (3982966)
		            //   vector = Not Specified (3982979)
		            //   parent cell line = Not Specified (-1)
		            //   strain = Not Specified (-1)
		            //   cell line type
		            //
            		derivationSearch.setAlleleTypeKey(alleleTypeKey);
            		derivationSearch.setVectorKey("3982979");
            		derivationSearch.setParentCellLineKey("-1");
            		derivationSearch.setCreatorKey("3982966");
            		derivationSearch.setStrainKey("-1");
            		derivationSearch.setCellLineTypeKey(cellLineTypeKey);
            		
            		derivationResults = derivationService.validateDerivation(derivationSearch);
            		
            		if (derivationResults.get(0).getDerivationKey().isEmpty()) {
                		log.info("Cannot find Derivation for this Allele Type and Parent = 'Not Specified'");
                		return modified;
                	}

                	mutantCellLine = "Not Specifeid";
                	strainKey = "-1";
                	addCellLine = true;
                	addAssociation = true;
            	}
            	
                // do not default 'not applicable'
                else {
                	addCellLine = false;
                	addAssociation = false;
                }
                	
            }
            
            //
            // end check isParent, isMutant
            //
              
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				if (domain.get(i).getMutantCellLine().getCellLineKey().isEmpty()) {
					continue;
				}
				
				log.info("processAlleleCellLine/create");
				AlleleCellLine entity = new AlleleCellLine();									
				entity.set_allele_key(Integer.valueOf(parentKey));
				entity.setMutantCellLine(cellLineDAO.get(Integer.valueOf(domain.get(i).getMutantCellLine().getCellLineKey())));				
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());				
				alleleCellLineDAO.persist(entity);				
				log.info("processAlleleCellLine/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAlleleCellLine/delete");
				if (domain.get(i).getAssocKey() != null && !domain.get(i).getAssocKey().isEmpty()) {
					AlleleCellLine entity = alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
					alleleCellLineDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAlleleCellLine/update");
				AlleleCellLine entity = alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));			
				entity.setMutantCellLine(cellLineDAO.get(Integer.valueOf(domain.get(i).getMutantCellLine().getCellLineKey())));				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				alleleCellLineDAO.update(entity);
				log.info("processAlleleCellLine/changes processed: " + domain.get(i).getAssocKey());				
				modified = true;
			}
			else {
		        getDerivation = false;
				log.info("processAlleleCellLine/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processAlleleCellLine/processing successful");
		return modified;
	}
   
}
