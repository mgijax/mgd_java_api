package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleVariantDAO;
import org.jax.mgi.mgd.api.model.all.dao.VariantEffectDAO;
import org.jax.mgi.mgd.api.model.all.domain.VariantEffectDomain;
import org.jax.mgi.mgd.api.model.all.translator.VariantEffectTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VariantEffectService extends BaseService<VariantEffectDomain> {

	protected Logger log = Logger.getLogger(VariantEffectService.class);

	@Inject
	private VariantEffectDAO variantEffectDAO;
	
	// translate an entity to a domain to return in the results
	private VariantEffectTranslator translator = new VariantEffectTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VariantEffectDomain> create(VariantEffectDomain domain, User user) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<VariantEffectDomain> update(VariantEffectDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public VariantEffectDomain get(Integer key) {
		return translator.translate(variantEffectDAO.get(key),1);
	}

    @Transactional
    public SearchResults<VariantEffectDomain> getResults(Integer key) {
        SearchResults<VariantEffectDomain> results = new SearchResults<VariantEffectDomain>();
        results.setItem(translator.translate(variantEffectDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<VariantEffectDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VariantEffectDomain> search() {

		List<VariantEffectDomain> results = new ArrayList<VariantEffectDomain>();

		String cmd = "select * from all_variantEffect";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				VariantEffectDomain domain = new VariantEffectDomain();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
