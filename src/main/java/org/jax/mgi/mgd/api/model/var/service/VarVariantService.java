package org.jax.mgi.mgd.api.model.var.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventReasonDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.EventReasonDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.EventReasonTranslator;
import org.jax.mgi.mgd.api.model.var.dao.VarVariantDAO;
import org.jax.mgi.mgd.api.model.var.domain.VarVariantDomain;
import org.jax.mgi.mgd.api.model.var.translator.VarVariantTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VarVariantService extends BaseService<VarVariantDomain> {

	protected Logger log = Logger.getLogger(VarVariantService.class);

	@Inject
	private VarVariantDAO variantDAO;

	private VarVariantTranslator translator = new VarVariantTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VarVariantDomain> create(VarVariantDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<VarVariantDomain> update(VarVariantDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public VarVariantDomain get(Integer key) {
		return translator.translate(variantDAO.get(key),1);
	}

    @Transactional
    public SearchResults<VarVariantDomain> getResults(Integer key) {
        SearchResults<VarVariantDomain> results = new SearchResults<VarVariantDomain>();
        results.setItem(translator.translate(variantDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<VarVariantDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VarVariantDomain> search() {

		List<VarVariantDomain> results = new ArrayList<VarVariantDomain>();

		String cmd = "select * from var_variant";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				VarVariantDomain domain = new VarVariantDomain();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
