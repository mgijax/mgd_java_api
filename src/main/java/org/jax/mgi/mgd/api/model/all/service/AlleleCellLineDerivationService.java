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
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLineDerivation;
import org.jax.mgi.mgd.api.model.all.translator.AlleleCellLineDerivationTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleCellLineDerivationService extends BaseService<AlleleCellLineDerivationDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleCellLineDerivationDAO derivationDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private CellLineDAO cellLineDAO;
	
	private AlleleCellLineDerivationTranslator translator = new AlleleCellLineDerivationTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<AlleleCellLineDerivationDomain> create(AlleleCellLineDerivationDomain domain, User user) {
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDerivationDomain> update(AlleleCellLineDerivationDomain domain, User user) {
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDerivationDomain> delete(Integer key, User user) {
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AlleleCellLineDerivationDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AlleleCellLineDerivationDomain domain = new AlleleCellLineDerivationDomain();
		if (derivationDAO.get(key) != null) {
			domain = translator.translate(derivationDAO.get(key));
		}
		derivationDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AlleleCellLineDerivationDomain> getResults(Integer key) {
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		results.setItem(translator.translate(derivationDAO.get(key)));
		derivationDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, AlleleCellLineDerivationDomain domain, User user) {
		// process allele cell line derivation (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processAlleleCellLineDerivation");
		
		if (domain == null) {
			log.info("processAlleleCellLineDerivation/nothing to process");
			return modified;
		}
		
		if (domain.getVectorKey().isEmpty()) {
			log.info("processAlleleCellLineDerivation/nothing to process");
			return modified;
		}					
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
					
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processAlleleCellLineDerivation/create");
			AlleleCellLineDerivation entity = new AlleleCellLineDerivation();									
			entity.setName(domain.getName());
			entity.setDescription(domain.getDescription());
			entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
			entity.setVectorType(termDAO.get(Integer.valueOf(domain.getVectorTypeKey())));							
			//entity.setParentCellLine(cellLineDAO.get(Integer.valueOf(domain.getParentCellLineKey())));				
			entity.setCreatedBy(user);
			entity.setCreation_date(new Date());
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());				
			derivationDAO.persist(entity);				
			log.info("processAlleleCellLineDerivation/create/returning results");	
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processAlleleCellLineDerivation/delete");
			if (domain.getDerivationKey() != null && !domain.getDerivationKey().isEmpty()) {
				AlleleCellLineDerivation entity = derivationDAO.get(Integer.valueOf(domain.getDerivationKey()));
				derivationDAO.remove(entity);
				modified = true;
			}
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processAlleleCellLineDerivation/update");
			AlleleCellLineDerivation entity = derivationDAO.get(Integer.valueOf(domain.getDerivationKey()));			
			entity.setModification_date(new Date());
			entity.setName(domain.getName());
			entity.setDescription(domain.getDescription());
			entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
			entity.setVectorType(termDAO.get(Integer.valueOf(domain.getVectorTypeKey())));
			//entity.setParentCellLine(cellLineDAO.get(Integer.valueOf(domain.getParentCellLineKey())));	
			entity.setModifiedBy(user);
			derivationDAO.update(entity);
			log.info("processAlleleCellLineDerivation/changes processed: " + domain.getDerivationKey());				
			modified = true;
		}
		else {
			log.info("processAlleleCellLineDerivation/no changes processed: " + domain.getDerivationKey());
		}
		
		log.info("processAlleleCellLineDerivation/processing successful");
		return modified;
	}
 
	@Transactional
	public List<AlleleCellLineDerivationDomain> validateDerivation(SlimAlleleCellLineDerivationDomain searchDomain) {
		
		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();	

		// derivation type key == allele type key
		
		String cmd = "\nselect d._Derivation_key"
		   + "\nfrom ALL_CellLine_Derivation d, ALL_CellLine c"
		   + "\nwhere d._DerivationType_key = " + searchDomain.getAlleleTypeKey()
		   + "\nand d._Creator_key = " + searchDomain.getCreatorKey()
		   + "\nand d._Vector_key = " + searchDomain.getVectorKey()
		   + "\nand d._ParentCellLine_key = " + searchDomain.getParentCellLineKey()
		   + "\nand d._ParentCellLine_key = c._CellLine_key"
		   + "\nand c._Strain_key = " + searchDomain.getStrainKey()
		   + "\nand c._CellLine_Type_key = " + searchDomain.getCellLineTypeKey()
		   + "\nand c.isMutant = 0";
		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				AlleleCellLineDerivationDomain domain = new AlleleCellLineDerivationDomain();
				domain = translator.translate(derivationDAO.get(rs.getInt("_derivation_key")));				
				derivationDAO.clear();
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
