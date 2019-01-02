package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.VariantSequenceDAO;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.translator.VariantSequenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VariantSequenceService extends BaseService<VariantSequenceDomain> {

	protected Logger log = Logger.getLogger(VariantSequenceService.class);

	@Inject
	private VariantSequenceDAO variantSequenceDAO;
	
	// translate an entity to a domain to return in the results
	private VariantSequenceTranslator translator = new VariantSequenceTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VariantSequenceDomain> create(VariantSequenceDomain domain, User user) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<VariantSequenceDomain> update(VariantSequenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public VariantSequenceDomain get(Integer key) {
		return translator.translate(variantSequenceDAO.get(key),1);
	}

    @Transactional
    public SearchResults<VariantSequenceDomain> getResults(Integer key) {
        SearchResults<VariantSequenceDomain> results = new SearchResults<VariantSequenceDomain>();
        results.setItem(translator.translate(variantSequenceDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<VariantSequenceDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VariantSequenceDomain> search() {

		List<VariantSequenceDomain> results = new ArrayList<VariantSequenceDomain>();

		String cmd = "select * from all_variantSequence";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				VariantSequenceDomain domain = new VariantSequenceDomain();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
