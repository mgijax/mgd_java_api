package org.jax.mgi.mgd.api.model.mld.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.MappingAssayTypeDAO;
import org.jax.mgi.mgd.api.model.mld.domain.MappingAssayTypeDomain;
import org.jax.mgi.mgd.api.model.mld.translator.MappingAssayTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class MappingAssayTypeService extends BaseService<MappingAssayTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MappingAssayTypeDAO assayTypeDAO;

	private MappingAssayTypeTranslator translator = new MappingAssayTypeTranslator();							

	@Transactional
	public SearchResults<MappingAssayTypeDomain> create(MappingAssayTypeDomain domain, User user) {
		SearchResults<MappingAssayTypeDomain> results = new SearchResults<MappingAssayTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<MappingAssayTypeDomain> update(MappingAssayTypeDomain domain, User user) {
		SearchResults<MappingAssayTypeDomain> results = new SearchResults<MappingAssayTypeDomain>();	
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MappingAssayTypeDomain> delete(Integer key, User user) {
		SearchResults<MappingAssayTypeDomain> results = new SearchResults<MappingAssayTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public MappingAssayTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MappingAssayTypeDomain domain = new MappingAssayTypeDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<MappingAssayTypeDomain> getResults(Integer key) {
        SearchResults<MappingAssayTypeDomain> results = new SearchResults<MappingAssayTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<MappingAssayTypeDomain> search(MappingAssayTypeDomain searchDomain) {
		// return AntibodyClassDomain which looks like a vocab/term domain

		List<MappingAssayTypeDomain> results = new ArrayList<MappingAssayTypeDomain>();

		String cmd = "\nselect a._assay_type_key from mld_assay_types a"
				+ "\nwhere a._assay_type_key != -1"
				+ "\nand exists (select 1 from mld_expts e, mld_expt_marker m"
				+ "\nwhere a._assay_type_key = m._assay_type_key"
				+ "\nand m._expt_key = e._expt_key"
				+ "\nand e.exptType in ('TEXT-QTL','TEXT-Physical Mapping','TEXT-Congenic','TEXT-QTL-Candidate Genes','TEXT-Meta Analysis'))"
				+ "\norder by a.description";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MappingAssayTypeDomain domain = new MappingAssayTypeDomain();	
				domain = translator.translate(assayTypeDAO.get(rs.getInt("_assay_type_key")));
				assayTypeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
}
