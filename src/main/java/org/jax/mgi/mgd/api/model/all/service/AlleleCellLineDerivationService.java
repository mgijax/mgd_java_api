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
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
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
	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private NoteService noteService;
	
	private AlleleCellLineDerivationTranslator translator = new AlleleCellLineDerivationTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	String mgiTypeKey = "36";
	
	@Transactional
	public SearchResults<AlleleCellLineDerivationDomain> create(AlleleCellLineDerivationDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		AlleleCellLineDerivation entity = new AlleleCellLineDerivation();
		
		log.info("processDerivation/create");
		
		entity.setName(domain.getName());
		entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
		entity.setVectorType(termDAO.get(Integer.valueOf(domain.getVectorTypeKey())));							
		entity.setDerivationType(termDAO.get(Integer.valueOf(domain.getDerivationTypeKey())));							
		entity.setCreator(termDAO.get(Integer.valueOf(domain.getCreatorKey())));							

		// can never change parent cell line from the derivation
		entity.setParentCellLine(cellLineDAO.get(entity.getParentCellLine().get_cellline_key()));
		
		if (domain.getDescription() != null && !domain.getDescription().isEmpty()) {
			entity.setDescription(domain.getDescription());
		}
		else {
			entity.setDescription(null);
		}
		
		if (domain.getRefsKey() != null && !domain.getRefsKey().isEmpty()) {
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));				
		}
		else {
			entity.setReference(null);
		}
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		derivationDAO.persist(entity);
	
		// process all notes		
		noteService.process(String.valueOf(entity.get_derivation_key()), domain.getGeneralNote(), mgiTypeKey, domain.getGeneralNote().getNoteTypeKey(), user);
			
		// return entity translated to domain
		log.info("processDerivation/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDerivationDomain> update(AlleleCellLineDerivationDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		AlleleCellLineDerivation entity = derivationDAO.get(Integer.valueOf(domain.getDerivationKey()));
		Boolean modified = true;

		log.info("processDerivation/update");

		entity.setName(domain.getName());
		entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
		entity.setVectorType(termDAO.get(Integer.valueOf(domain.getVectorTypeKey())));
		entity.setDerivationType(termDAO.get(Integer.valueOf(domain.getDerivationTypeKey())));							
		entity.setCreator(termDAO.get(Integer.valueOf(domain.getCreatorKey())));
		
		// can never change parent cell line from the derivation
		entity.setParentCellLine(cellLineDAO.get(entity.getParentCellLine().get_cellline_key()));	

		if (domain.getDescription() != null && !domain.getDescription().isEmpty()) {
			entity.setDescription(domain.getDescription());
		}
		else {
			entity.setDescription(null);
		}
		
		if (domain.getRefsKey() != null && !domain.getRefsKey().isEmpty()) {
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));				
		}
		else {
			entity.setReference(null);
		}
		
		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		
		// process all notes
		if (noteService.process(domain.getDerivationKey(), domain.getGeneralNote(), mgiTypeKey, domain.getGeneralNote().getNoteTypeKey(), user)) {
			modified = true;
		}
		
		if (modified) {
			derivationDAO.update(entity);
			log.info("processDerivation/changes processed: " + domain.getDerivationKey());
		}
		
		// return entity translated to domain
		log.info("processDerivation/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processDerivation/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDerivationDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		AlleleCellLineDerivation entity = derivationDAO.get(key);
		results.setItem(translator.translate(derivationDAO.get(key)));
		derivationDAO.remove(entity);
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
	public SearchResults<AlleleCellLineDerivationDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		String cmd = "select count(*) as objectCount from all_cellline_derivation";
		
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
	public List<AlleleCellLineDerivationDomain> search(AlleleCellLineDerivationDomain searchDomain) {

		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._derivation_key, a.name";
		String from = "from all_cellline_derivation_view a";
		String where = "where a._derivation_key is not null";
		String orderBy = "order by a.name";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		Boolean from_generalNote = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getDerivationTypeKey() != null && !searchDomain.getDerivationTypeKey().isEmpty()) {
			where = where + "\nand a._derivationtype_key = " + searchDomain.getDerivationTypeKey();
		}
		
		if (searchDomain.getCreatorKey() != null && !searchDomain.getCreatorKey().isEmpty()) {
			where = where + "\nand a._creator_key = " + searchDomain.getCreatorKey();
		}			

		if (searchDomain.getVectorKey() != null && !searchDomain.getVectorKey().isEmpty()) {
			where = where + "\nand a._vector_key = " + searchDomain.getVectorKey();
		}
		else if (searchDomain.getVector() != null && !searchDomain.getVector().isEmpty()) {
			where = where + "\nand a.vector ilike '" + searchDomain.getVector() + "'";
		}
		
		if (searchDomain.getVectorTypeKey() != null && !searchDomain.getVectorTypeKey().isEmpty()) {
			where = where + "\nand a._vectortype_key = " + searchDomain.getVectorTypeKey();
		}
				
		// parent cell lines
		if (searchDomain.getParentCellLine() != null) {			
			if (searchDomain.getParentCellLine().getCellLineKey() != null && !searchDomain.getParentCellLine().getCellLineKey().isEmpty()) {
				where = where + "\nand a.parentcellline_key = " + searchDomain.getParentCellLine().getCellLineKey();
			}
			else if (searchDomain.getParentCellLine().getCellLine() != null && !searchDomain.getParentCellLine().getCellLine().isEmpty()) {
				where = where + "\nand a.parentcellline ilike '" + searchDomain.getParentCellLine().getCellLine() + "'";
			}			
			if (searchDomain.getParentCellLine().getCellLineTypeKey() != null && !searchDomain.getParentCellLine().getCellLineTypeKey().isEmpty()) {
				where = where + "\nand a.parentcelllinetype_key = " + searchDomain.getParentCellLine().getCellLineTypeKey();
			}
			if (searchDomain.getParentCellLine().getStrainKey() != null && !searchDomain.getParentCellLine().getStrainKey().isEmpty()) {
				where = where + "\nand a.parentcelllinestrain_key = " + searchDomain.getParentCellLine().getStrainKey();
			}
			else if (searchDomain.getParentCellLine().getStrain() != null && !searchDomain.getParentCellLine().getStrain().isEmpty()) {
				where = where + "\nand a.parentcelllinestrain ilike '" + searchDomain.getParentCellLine().getStrain() + "'";
			}		
		}				
		
		// reference
		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand a._Refs_key = " + searchDomain.getRefsKey();
		}
		if (searchDomain.getShort_citation() != null && !searchDomain.getShort_citation().isEmpty()) {
			value = searchDomain.getShort_citation().replace("'",  "''");
			where = where + "\nand a.short_citation ilike '" + value + "'";
		}
		if (searchDomain.getJnumid() != null && !searchDomain.getJnumid().isEmpty()) {
			where = where + "\nand a.jnumid ilike '" + searchDomain.getJnumid() + "'";
		}
		
		// notes
		if (searchDomain.getGeneralNote() != null && !searchDomain.getGeneralNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getGeneralNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note1.note ilike '" + value + "'" ;
			from_generalNote = true;
		}
		
		if (from_generalNote == true) {
			from = from + ", mgi_note_derivation_view note1";
			where = where + "\nand a._derivation_key = note1._object_key";
			where = where + "\nand note1._notetype_key = 1033";			
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n";
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
	
	@Transactional
	public List<AlleleCellLineDerivationDomain> searchMCLSet() {
		// search a mutant cell line set of derivations
		// exclude those where create = "Not Specified", "Not Applicable"
		
		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();	
		
		String cmd = "\nselect _Derivation_key"
		   + "\nfrom ALL_CellLine_Derivation"
		   + "\nwhere _Creator_key not in (3982966, 3982967)"
		   + "\norder by name";

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

	@Transactional
	public List<AlleleCellLineDerivationDomain> searchDuplicateByName(AlleleCellLineDerivationDomain searchDomain) {
		// search existing derivation by derivation name
		
		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();	
		
		String cmd = "\nselect _Derivation_key"
		   + "\nfrom ALL_CellLine_Derivation"
		   + "\nwhere name ilike '" + searchDomain.getName() + "'";
		
		if (searchDomain.getDerivationKey() != null && !searchDomain.getDerivationKey().isEmpty()) {
		   cmd = cmd + "\nand _derivation_key != " + searchDomain.getDerivationKey();
		}
		
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
	
	@Transactional
	public List<AlleleCellLineDerivationDomain> validateDerivation(SlimAlleleCellLineDerivationDomain searchDomain) {
		
		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();	

		// derivation type key == allele type key
		
		String cmd = "\nselect d._Derivation_key"
		   + "\nfrom ALL_CellLine_Derivation d, ALL_CellLine c"
		   + "\nwhere d._DerivationType_key = " + searchDomain.getAlleleTypeKey()
		   + "\nand d._Creator_key = " + searchDomain.getCreatorKey()
		   + "\nand d._Vector_key = " + searchDomain.getVectorKey()
		   + "\nand d._VectorType_key = " + searchDomain.getVectorTypeKey()		   
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
