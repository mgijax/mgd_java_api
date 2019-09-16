package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeStrainService extends BaseService<ProbeStrainDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeStrainDAO probeStrainDAO;

	private ProbeStrainTranslator translator = new ProbeStrainTranslator();
	private SlimProbeStrainTranslator slimtranslator = new SlimProbeStrainTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeStrainDomain> create(ProbeStrainDomain object, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
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

		String cmd = "select s._strain_key "
				+ "\nfrom prb_strain s"
				+ "\nwhere s.strain = '" + searchDomain.getStrain() + "'";
		
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
