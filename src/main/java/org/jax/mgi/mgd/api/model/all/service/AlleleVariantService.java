package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.dao.AlleleVariantDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;
import org.jax.mgi.mgd.api.model.all.translator.AlleleVariantTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleVariantService extends BaseService<AlleleVariantDomain> {

	protected Logger log = Logger.getLogger(AlleleVariantService.class);

	@Inject
	private AlleleVariantDAO variantDAO;

	@Inject 
	AlleleDAO alleleDAO;
	
	@Inject
	private ProbeStrainDAO strainDAO;
	
	// translate an entity to a domain to return in the results
	private AlleleVariantTranslator translator = new AlleleVariantTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AlleleVariantDomain> create(AlleleVariantDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		AlleleVariant entity = new AlleleVariant();
		
		// AlleleVariant key will be auto-sequence
		
		// set the Allele
		entity.setAllele(alleleDAO.get(Integer.valueOf(domain.getAllele().getAlleleKey())));
		
		// this is the source variant, so _sourceVariant_key will be null
		
		// set the Strain
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrain().getStrainKey())));
		
		// here I assume that either the default (false) or true is being
		// sent in. Or should the create method always set this to false?
		entity.setIsReviewed(Integer.valueOf(domain.getIsReviewed()).intValue());
		
		// description can be null - do we need to check that or can we just 
		// do the following?
		entity.setDescription(domain.getDescription());
		
		// creation/modification date/by
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// insert into the database
		variantDAO.persist(entity);

		// return entity translated to domain, set in results
		// results has other info too
		log.info("AlleleVariant/create/returning results");
		results.setItem(translator.translate(entity,0));
		return results;
	}

	@Transactional
	public SearchResults<AlleleVariantDomain> update(AlleleVariantDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AlleleVariantDomain get(Integer key) {
		return translator.translate(variantDAO.get(key),1);
	}

    @Transactional
    public SearchResults<AlleleVariantDomain> getResults(Integer key) {
        SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
        results.setItem(translator.translate(variantDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<AlleleVariantDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AlleleVariantDomain> search() {

		List<AlleleVariantDomain> results = new ArrayList<AlleleVariantDomain>();

		String cmd = "select * from all_variant";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AlleleVariantDomain domain = new AlleleVariantDomain();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
