package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeStrainService extends BaseService<ProbeStrainDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeStrainDAO probeStrainDAO;
	@Inject
	private TermDAO termDAO;
	
	private ProbeStrainTranslator translator = new ProbeStrainTranslator();
	private SlimProbeStrainTranslator slimtranslator = new SlimProbeStrainTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeStrainDomain> create(ProbeStrainDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		ProbeStrain entity = new ProbeStrain();
		
		log.info("processStrain/create");
		
		entity.setSpecies(termDAO.get(Integer.valueOf(domain.getSpeciesKey())));			
		entity.setStrainType(termDAO.get(Integer.valueOf(domain.getStrainTypeKey())));
		entity.setStrain(domain.getStrain());
		entity.setStandard(Integer.valueOf(domain.getStandard()));
		entity.setIsPrivate(Integer.valueOf(domain.getIsPrivate()));
		entity.setGeneticBackground(Integer.valueOf(domain.getGeneticBackground()));
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		probeStrainDAO.persist(entity);
		
		// return entity translated to domain
		log.info("processStrain/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<ProbeStrainDomain> update(ProbeStrainDomain object, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeStrainDomain> delete(Integer key, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeStrainDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeStrainDomain domain = new ProbeStrainDomain();
		if (probeStrainDAO.get(key) != null) {
			domain = translator.translate(probeStrainDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeStrainDomain> getResults(Integer key) {
        SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
        results.setItem(translator.translate(probeStrainDAO.get(key)));
        return results;
    }

	@Transactional
	public List<SlimProbeStrainDomain> validateStrain(SlimProbeStrainDomain searchDomain) {
		// validate the Strain by strain symbol
   
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		String cmd = "";
		String select = "select s._strain_key";
		String from = "from prb_strain s";
		String where = "where s._Strain_key is not null";
		
		Boolean from_accession = false;
		
		if (searchDomain.getStrain() != null && !searchDomain.getStrain().isEmpty()) {
			where = where + "\nand strain ilike '" + searchDomain.getStrain() + "'" ;
		}
		
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(acc.accID) = '" + mgiid.toLowerCase() + "'";	
			from_accession = true;
		}

		if (from_accession == true) {
			from = from + ", prb_strain_acc_view acc";
			where = where + "\nand s._strain_key = acc._object_key" 
				+ "\nand acc._mgitype_key = 10"
				+ "\nand acc._logicaldb_key = 1"
				+ "\nand acc.preferred = 1";		
		}
		
		cmd = "\n" + select + "\n" + from + "\n" + where;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimProbeStrainDomain slimdomain = new SlimProbeStrainDomain();
				slimdomain = slimtranslator.translate(probeStrainDAO.get(rs.getInt("_strain_key")));				
				probeStrainDAO.clear();
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
