package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimOrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.SlimOrganismTranslator;
import org.jax.mgi.mgd.api.model.mrk.service.ChromosomeService;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class OrganismService extends BaseService<OrganismDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private OrganismMGITypeService organismMGITypeService;
	@Inject
	private ChromosomeService chromosomeService;
	
	private OrganismTranslator translator = new OrganismTranslator();
	private SlimOrganismTranslator slimtranslator = new SlimOrganismTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<OrganismDomain> create(OrganismDomain domain, User user) {	
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		Organism entity = new Organism();
		
		log.info("processOrganism/create");		
		
		entity.setCommonname(domain.getCommonname());
		entity.setLatinname(domain.getLatinname());
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
	
		log.info("processOrganism/createF");		
		
		// execute persist/insert/send to database
		organismDAO.persist(entity);
		
		// process mgitypes
		organismMGITypeService.process(String.valueOf(entity.get_organism_key()), domain.getMgiTypes(), user);

		// process chromosomes
		chromosomeService.process(domain.getOrganismKey(), domain.getChromosomes(), user);
		
		// return entity translated to domain
		log.info("processOrganism/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<OrganismDomain> update(OrganismDomain domain, User user) {
		// update existing entity object from in-coming domain
		
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		Organism entity = organismDAO.get(Integer.valueOf(domain.getOrganismKey()));
		Boolean modified = true;
		
		log.info("processOrganism/update");
				
		entity.setCommonname(domain.getCommonname());
		entity.setLatinname(domain.getLatinname());

		// process mgitypes
		if (organismMGITypeService.process(domain.getOrganismKey(), domain.getMgiTypes(), user)) {
			modified = true;			
		}
		
		// process chromosomes
		if (chromosomeService.process(domain.getOrganismKey(), domain.getChromosomes(), user)) {
			modified = true;			
		}
		
		// finish update
		if (modified) {		
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			organismDAO.update(entity);
			log.info("processOrganism/changes processed: " + domain.getOrganismKey());		
		}
		
		// return entity translated to domain
		log.info("processOrganism/update/returning results");
		results.setItem(translator.translate(entity));		
		log.info("processOrganism/update/returned results succsssful");
		return results;			
	}

	@Transactional
	public SearchResults<OrganismDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		Organism entity = organismDAO.get(key);
		results.setItem(translator.translate(organismDAO.get(key)));
		organismDAO.remove(entity);
		return results;
	}

	@Transactional
	public OrganismDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		OrganismDomain domain = new OrganismDomain();
		if (organismDAO.get(key) != null) {
			domain = translator.translate(organismDAO.get(key));
		}
		organismDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<OrganismDomain> getResults(Integer key) {
        SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
        results.setItem(translator.translate(organismDAO.get(key)));
        organismDAO.clear();
        return results;
    }

	@Transactional	
	public SearchResults<OrganismDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		String cmd = "select count(*) as objectCount from mgi_organism";
		
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
	public List<OrganismDomain> search(OrganismDomain searchDomain) {

		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "";
		String select = "select distinct o._organism_key, o.commonname";
		String from = "from mgi_organism o";
		String where = "where o._organism_key is not null";
		String orderBy = "\norder by o.commonname";
		Boolean from_mgitype = false;
		Boolean from_chromosome = false;
		
		if (searchDomain.getCommonname() != null && !searchDomain.getCommonname().isEmpty()) {
			where = where + "\nand o.commonname ilike '" + searchDomain.getCommonname() + "'";
		}
		
		if (searchDomain.getLatinname() != null && !searchDomain.getLatinname().isEmpty()) {
			where = where + "\nand o.latinname ilike '" + searchDomain.getLatinname() + "'";
		}

		if (searchDomain.getMgiTypes() != null) {		
			if (searchDomain.getMgiTypes().get(0).getMgiTypeKey() != null && !searchDomain.getMgiTypes().get(0).getMgiTypeKey().isEmpty()) {
				where = where + "\nand m._mgitype_key = " + searchDomain.getMgiTypes().get(0).getMgiTypeKey();
				from_mgitype = true;
			}			
		}

		if (searchDomain.getChromosomes() != null) {			
			if (searchDomain.getChromosomes().get(0).getChromosome() != null && !searchDomain.getChromosomes().get(0).getChromosome().isEmpty()) {
				where = where + "\nand c.chromosome ilike '" + searchDomain.getChromosomes().get(0).getChromosome() + "'";
				from_chromosome = true;
			}			
		}
		
		if (from_mgitype == true) {
			from = from + ", mgi_organism_mgitype_view m";
			where = where + "\nand o._organism_key = m._organism_key";
		}

		if (from_chromosome == true) {
			from = from + ", mrk_chromosome c";
			where = where + "\nand o._organism_key = c._organism_key";
		}
		
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}

	@Transactional	
	public List<OrganismDomain> searchDriverGene() {
		// for allele module/driver note
		// include: see mgi_organism_allele_view
		
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key from mgi_organism_allele_view order by commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	

	@Transactional	
	public List<OrganismDomain> searchMarker() {
		// for marker module
		// include: see mgi_organism_marker_view
		
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key from mgi_organism_marker_view order by commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}
	
	@Transactional	
	public List<OrganismDomain> searchAntigen() {
		// for antigen module organism pick list
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key, commonname, 0 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname = 'mouse, laboratory'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 1 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname = 'human'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 2 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname = 'Not Specified'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 3 as org\n" + 
				"from mgi_organism_antigen_view\n" + 
				"where commonname not in ('mouse, laboratory', 'Not Specified', 'human')\n" + 
				"order by org, commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}
	
	@Transactional	
	public List<OrganismDomain> searchAntibody() {
		// for antigen module organism pick list
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

//        1 | mouse, laboratory
//       76 | Not Specified
//       79 | rabbit
//       74 | Not Applicable  : do not include
		
		String cmd ="select s.*, 1 as orderBy" + 
				"\nfrom MGI_Organism s, MGI_Organism_MGIType t" +
				"\nwhere s._Organism_key = t._Organism_key and t._MGIType_key = 6" +
				"\nand s._Organism_key in (1,76,79)" +
				"\nunion" +
				"\nselect s.*, 2 as orderBy" + 
				"\nfrom MGI_Organism s, MGI_Organism_MGIType t" +
				"\nwhere s._Organism_key = t._Organism_key and t._MGIType_key = 6" +
				"\nand s._Organism_key not in (1,76,79,74)" +
				"\norder by orderBy, commonname";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}

	@Transactional	
	public List<OrganismDomain> searchProbe() {
		// for probe module
		// include: see mgi_organism_probe_view
		
		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select _organism_key, commonname, 0 as org\n" + 
				"from mgi_organism_probe_view\n" + 
				"where commonname = 'mouse, laboratory'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 2 as org\n" + 
				"from mgi_organism_probe_view\n" + 
				"where commonname = 'Not Specified'\n" + 
				"union\n" + 
				"select _organism_key, commonname, 3 as org\n" + 
				"from mgi_organism_probe_view\n" + 
				"where commonname not in ('mouse, laboratory', 'Not Specified')\n" + 
				"order by org, commonname";	
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}

	@Transactional	
	public List<SlimOrganismDomain> searchGXDHTSample() {
		// for gxd ht sample module organism pick list
		List<SlimOrganismDomain> results = new ArrayList<SlimOrganismDomain>();

		String cmd ="select s.*\n" + 
				"from MGI_Organism s, MGI_Organism_MGIType t\n" + 
				"where s._Organism_key = t._Organism_key\n" +
				"and t._MGIType_key = 43";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimOrganismDomain domain = new SlimOrganismDomain();
				domain = slimtranslator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}
	
	@Transactional	
	public List<SlimOrganismDomain> searchAlleleRelationship() {
		// for allele relationship module organism pick list
		List<SlimOrganismDomain> results = new ArrayList<SlimOrganismDomain>();

		String cmd ="select s.*\n" + 
				"from MGI_Organism s, MGI_Organism_MGIType t\n" + 
				"where s._Organism_key = t._Organism_key\n" +
				"and t._MGIType_key = 40";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimOrganismDomain domain = new SlimOrganismDomain();
				domain = slimtranslator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
}
