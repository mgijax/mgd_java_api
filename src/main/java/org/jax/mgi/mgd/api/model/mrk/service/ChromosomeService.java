package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.ChromosomeDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Chromosome;
import org.jax.mgi.mgd.api.model.mrk.translator.ChromosomeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ChromosomeService extends BaseService<ChromosomeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ChromosomeDAO chromosomeDAO;

	private ChromosomeTranslator translator = new ChromosomeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ChromosomeDomain> create(ChromosomeDomain object, User user) {
		SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ChromosomeDomain> update(ChromosomeDomain object, User user) {
		SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ChromosomeDomain> delete(Integer key, User user) {
		SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ChromosomeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ChromosomeDomain domain = new ChromosomeDomain();
		if (chromosomeDAO.get(key) != null) {
			domain = translator.translate(chromosomeDAO.get(key));
		}
		chromosomeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ChromosomeDomain> getResults(Integer key) {
        SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
        results.setItem(translator.translate(chromosomeDAO.get(key)));
        chromosomeDAO.clear();
        return results;
    }

	@Transactional	
	public List<ChromosomeDomain> search(ChromosomeDomain searchDomain) {

		List<ChromosomeDomain> results = new ArrayList<ChromosomeDomain>();

		String cmd = "select * from mrk_chromosome"
				+ "\nwhere _organism_key = " + searchDomain.getOrganismKey()
				+ "\norder by sequenceNum";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ChromosomeDomain domain = new ChromosomeDomain();
				domain = translator.translate(chromosomeDAO.get(rs.getInt("_chromosome_key")));
				chromosomeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	

	@Transactional
	public Boolean process(String parentKey, List<ChromosomeDomain> domain, User user) {
		// process chromosome/organism (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processChromosome");

		if (domain == null || domain.isEmpty()) {
			log.info("processChromosome/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getChromosome() == null || domain.get(i).getChromosome().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processChromosome/create");
				Chromosome entity = new Chromosome();									
				entity.set_organism_key(Integer.valueOf(parentKey));
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
				entity.setChromosome(domain.get(i).getChromosome());
				entity.setCreatedBy(user);
				entity.setModifiedBy(user);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());				
				chromosomeDAO.persist(entity);				
				log.info("processChromosome/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processChromosome/delete");
				if (domain.get(i).getChromosomeKey() != null && !domain.get(i).getChromosomeKey().isEmpty()) {
					Chromosome entity = chromosomeDAO.get(Integer.valueOf(domain.get(i).getChromosomeKey()));
					chromosomeDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processChromosome/update");
				Chromosome entity = chromosomeDAO.get(Integer.valueOf(domain.get(i).getChromosomeKey()));	
				entity.set_organism_key(Integer.valueOf(parentKey));
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));	
				entity.setChromosome(domain.get(i).getChromosome());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				chromosomeDAO.update(entity);
				log.info("processChromosome/changes processed: " + domain.get(i).getChromosomeKey());				
				modified = true;
			}
			else {
				log.info("processChromosome/no changes processed: " + domain.get(i).getChromosomeKey());
			}           
		}
		
		log.info("processChromosome/processing successful");
		return modified;
	}
	
}
