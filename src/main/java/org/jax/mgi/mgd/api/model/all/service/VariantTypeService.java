package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleVariantDAO;
import org.jax.mgi.mgd.api.model.all.dao.VariantTypeDAO;
import org.jax.mgi.mgd.api.model.all.domain.VariantTypeDomain;
import org.jax.mgi.mgd.api.model.all.translator.VariantTypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VariantTypeService extends BaseService<VariantTypeDomain> {

	protected Logger log = Logger.getLogger(VariantTypeService.class);

	@Inject
	private VariantTypeDAO variantTypeDAO;
	
	// translate an entity to a domain to return in the results
	private VariantTypeTranslator translator = new VariantTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VariantTypeDomain> create(VariantTypeDomain domain, User user) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<VariantTypeDomain> update(VariantTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public VariantTypeDomain get(Integer key) {
		return translator.translate(variantTypeDAO.get(key),1);
	}

    @Transactional
    public SearchResults<VariantTypeDomain> getResults(Integer key) {
        SearchResults<VariantTypeDomain> results = new SearchResults<VariantTypeDomain>();
        results.setItem(translator.translate(variantTypeDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<VariantTypeDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VariantTypeDomain> search() {

		List<VariantTypeDomain> results = new ArrayList<VariantTypeDomain>();

		String cmd = "select * from all_varianttype";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				VariantTypeDomain domain = new VariantTypeDomain();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
