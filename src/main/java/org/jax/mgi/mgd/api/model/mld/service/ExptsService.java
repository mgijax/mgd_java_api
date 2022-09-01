package org.jax.mgi.mgd.api.model.mld.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceCitationCacheDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.ExptsDAO;
import org.jax.mgi.mgd.api.model.mld.domain.ExptsDomain;
import org.jax.mgi.mgd.api.model.mld.domain.SlimExptsDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;
import org.jax.mgi.mgd.api.model.mld.translator.ExptsTranslator;
import org.jax.mgi.mgd.api.model.mld.translator.SlimExptsTranslator;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ExptsService extends BaseService<ExptsDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ExptsDAO exptsDAO;
	@Inject
	private ReferenceCitationCacheDAO referenceDAO;
	@Inject
	private MappingNoteService mappingNoteService;
	@Inject
	private ExptNoteService exptNoteService;
	@Inject
	private ExptMarkerService markerService;
	
	private ExptsTranslator translator = new ExptsTranslator();	
	private SlimExptsTranslator slimtranslator = new SlimExptsTranslator();		
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String exptTypes = "('TEXT-QTL', 'TEXT-Physical Mapping', 'TEXT-Congenic', 'TEXT-QTL-Candidate Genes', 'TEXT-Meta Analysis')"; 
	private String exptTypesByGet = "('TEXT-QTL', 'TEXT-Physical Mapping', 'TEXT-Congenic', 'TEXT-QTL-Candidate Genes', 'TEXT-Meta Analysis', 'TEXT', 'TEXT-Genetic Cross')"; 

	//private String mgiTypeKey = "4";
	
	@Transactional
	public SearchResults<ExptsDomain> create(ExptsDomain domain, User user) {	
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		Expts entity = new Expts();

		log.info("processExpt/create");		
		
		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		entity.setExptType(domain.getExptType());
		entity.setTag(1);
		entity.setChromosome(domain.getChromosome());
		entity.setCreation_date(new Date());
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		exptsDAO.persist(entity);

		// process mapping notes
		mappingNoteService.process(domain.getRefsKey(), domain.getReferenceNote(), user);
		
		// process experiment notes
		exptNoteService.process(String.valueOf(entity.get_expt_key()), domain.getExptNote(), user);
		
		// markers
		markerService.process(String.valueOf(entity.get_expt_key()), domain.getMarkers(), user);
		
		// return entity translated to domain
		log.info("processExpt/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<ExptsDomain> update(ExptsDomain domain, User user) {
		// update existing entity object from in-coming domain
		
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		Expts entity = exptsDAO.get(Integer.valueOf(domain.getExptKey()));
		Boolean modified = true;
		
		log.info("processExpt/update");				

		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));		
		entity.setExptType(domain.getExptType());
		entity.setTag(1);
		entity.setChromosome(domain.getChromosome());
		
		// process mapping notes
		if (mappingNoteService.process(domain.getRefsKey(), domain.getReferenceNote(), user)) {
			modified = true;
		}

		// process experiment notes
		if (exptNoteService.process(domain.getExptKey(), domain.getExptNote(), user)) {
			modified = true;
		}

		// process markers
		if (markerService.process(domain.getExptKey(), domain.getMarkers(), user)) {
			modified = true;
		}
		
		// finish update
		if (modified) {		
			entity.setModification_date(new Date());
			exptsDAO.update(entity);
			log.info("processExpt/changes processed: " + domain.getExptKey());		
		}
		
		// 
		// return entity translated to domain
		log.info("processExpt/update/returning results");
		results.setItem(translator.translate(entity));		
		log.info("processExpt/update/returned results succsssful");
		return results;			
	}

	@Transactional
	public SearchResults<ExptsDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		Expts entity = exptsDAO.get(key);
		results.setItem(translator.translate(exptsDAO.get(key)));
		exptsDAO.remove(entity);
		return results;
	}

	@Transactional
	public ExptsDomain get(Integer key) {
		// get the DAO/entity and translate -> domain

		ExptsDomain domain = new ExptsDomain();

		if (exptsDAO.get(key) != null) {
			domain = translator.translate(exptsDAO.get(key));		
		}
		
		return domain;
	}

    @Transactional
    public SearchResults<ExptsDomain> getResults(Integer key) {
        SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
        results.setItem(translator.translate(exptsDAO.get(key)));
        return results;
    }

	@Transactional	
	public SearchResults<ExptsDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ExptsDomain> results = new SearchResults<ExptsDomain>();
		String cmd = "select count(*) as objectCount from mld_expts where exptType in " + exptTypes;
		
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
	public List<SlimExptsDomain> search(ExptsDomain searchDomain) {

		List<SlimExptsDomain> results = new ArrayList<SlimExptsDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct e._expt_key, e.jnum, e.expttype, e.chromosome";
		String from = "from mld_expt_view e";
		String where = "where e.exptType in " + exptTypes;
		String orderBy = "order by e.jnum";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		Boolean from_accession = false;
		Boolean from_rnote = false;
		Boolean from_enote = false;
		Boolean from_marker = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("e", null, null, searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand e._refs_key = " + searchDomain.getRefsKey();
		}
		else if (searchDomain.getShort_citation() != null && !searchDomain.getShort_citation().isEmpty()) {
			where = where + "\nand e.short_citation ilike '" + searchDomain.getShort_citation().replace("'",  "''")  + "'";
		}
		
		if (searchDomain.getExptType() != null && !searchDomain.getExptType().isEmpty()) {
			where = where + "\nand e.exptType = '" + searchDomain.getExptType() + "'";
		}
		
		if (searchDomain.getChromosome() != null && !searchDomain.getChromosome().isEmpty()) {
			where = where + "\nand e.chromosome = '" + searchDomain.getChromosome() + "'";
		}
		
		// reference note
		if (searchDomain.getReferenceNote().getNote() != null && !searchDomain.getReferenceNote().getNote().isEmpty()) {
			where = where + "\nand rnote.note ilike '" + searchDomain.getReferenceNote().getNote() + "'";
			from_rnote = true;
		}
		
		// expt note
		if (searchDomain.getExptNote().getNote() != null && !searchDomain.getExptNote().getNote().isEmpty()) {
			where = where + "\nand enote.note ilike '" + searchDomain.getExptNote().getNote() + "'";
			from_enote = true;			
		}

		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {
			String value = searchDomain.getAccID().toUpperCase();
			if (!value.startsWith("MGI:")) {
				where = where + "\nand acc.numericPart = '" + value + "'";
			}
			else {
				where = where + "\nand acc.accID = '" + value + "'";
			}
			from_accession = true;
		}
		
		// markers
		if (searchDomain.getMarkers() != null && !searchDomain.getMarkers().isEmpty()) {
			
			if (searchDomain.getMarkers().get(0).getMarkerKey() != null && !searchDomain.getMarkers().get(0).getMarkerKey().isEmpty()) {
				where = where + "\nand m._Marker_key = " + searchDomain.getMarkers().get(0).getMarkerKey();
				from_marker = true;	
			}
			if (searchDomain.getMarkers().get(0).getMarkerSymbol() != null && !searchDomain.getMarkers().get(0).getMarkerSymbol().isEmpty()) {
				where = where + "\nand m.symbol ilike '" + searchDomain.getMarkers().get(0).getMarkerSymbol() + "'";
				from_marker = true;	
			}			
			if (searchDomain.getMarkers().get(0).getAlleleKey() != null && !searchDomain.getMarkers().get(0).getAlleleKey().isEmpty()) {
				where = where + "\nand m._Allele_key = " + searchDomain.getMarkers().get(0).getAlleleKey();
				from_marker = true;	
			}
			if (searchDomain.getMarkers().get(0).getAlleleSymbol() != null && !searchDomain.getMarkers().get(0).getAlleleSymbol().isEmpty()) {
				where = where + "\nand m.allele ilike '" + searchDomain.getMarkers().get(0).getAlleleSymbol() + "'";
				from_marker = true;	
			}			
			if (searchDomain.getMarkers().get(0).getAssayTypeKey() != null && !searchDomain.getMarkers().get(0).getAssayTypeKey().isEmpty()) {
				where = where + "\nand m._assay_type_key = " + searchDomain.getMarkers().get(0).getAssayTypeKey();
				from_marker = true;	
			}	
			if (searchDomain.getMarkers().get(0).getDescription() != null && !searchDomain.getMarkers().get(0).getDescription().isEmpty()) {
				where = where + "\nand m.description ilike '" + searchDomain.getMarkers().get(0).getDescription() + "'";
				from_marker = true;	
			}			
		}
		
		// building from...
		
		if (from_accession == true) {
			from = from + ", acc_accession acc";
			where = where + "\nand acc._mgitype_key = 4 and e._expt_key = acc._object_key and acc.prefixPart = 'MGI:'";
		}
		if (from_rnote == true) {
			from = from + ", mld_notes rnote";
			where = where + "\nand e._refs_key = rnote._refs_key";
		}
		if (from_enote == true) {
			from = from + ", mld_expt_notes enote";
			where = where + "\nand e._expt_key = enote._expt_key";
		}
		if (from_marker == true) {
			from = from + ", mld_expt_marker_view m";
			where = where + "\nand e._expt_key = m._expt_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimExptsDomain domain = new SlimExptsDomain();
				domain = slimtranslator.translate(exptsDAO.get(rs.getInt("_expt_key")));				
				exptsDAO.clear();
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
	public List<SlimExptsDomain> getExptsByMarker(String accid) {
		// return list of assay domains by marker acc id

		List<SlimExptsDomain> results = new ArrayList<SlimExptsDomain>();
		
		String cmd = "\nselect distinct e._expt_key, e.expttype, c.numericpart" + 
				"\nfrom mrk_marker m, acc_accession aa, mld_expts e, mld_expt_marker em, bib_citation_cache c" + 
				"\nwhere m._marker_key = aa._object_key" + 
				"\nand aa._mgitype_key = 2" +
				"\nand aa.accid = '" + accid + "'" +
				"\nand m._marker_key = em._marker_key" +
				"\nand em._expt_key = e._expt_key" +
				"\nand e._refs_key = c._refs_key" +
				"\nand e.exptType in " + exptTypesByGet + 
				"" +
				"\norder by expttype, numericpart";
		
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimExptsDomain domain = new SlimExptsDomain();
				domain = slimtranslator.translate(exptsDAO.get(rs.getInt("_expt_key")));
				exptsDAO.clear();
				results.add(domain);
				exptsDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return results;
	}

	@Transactional	
	public List<SlimExptsDomain> getExptsByRef(String jnumid) {
		// return list of assay domains by reference jnum id

		List<SlimExptsDomain> results = new ArrayList<SlimExptsDomain>();
		
		String cmd = "\nselect distinct e._expt_key, e.expttype, aa.numericpart" + 
				"\nfrom bib_citation_cache aa, mld_expts e" + 
				"\nwhere aa.jnumid = '" + jnumid + "'" +
				"\nand aa._refs_key = e._refs_key" +
				"\nand e.exptType in " + exptTypesByGet + 
				"\norder by expttype, numericpart";
		
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimExptsDomain domain = new SlimExptsDomain();
				domain = slimtranslator.translate(exptsDAO.get(rs.getInt("_expt_key")));
				exptsDAO.clear();
				results.add(domain);
				exptsDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return results;
	}
	
}
