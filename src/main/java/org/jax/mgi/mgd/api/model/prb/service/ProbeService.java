package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeDomain;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeService extends BaseService<ProbeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeDAO probeDAO;

	private ProbeTranslator translator = new ProbeTranslator();
	private SlimProbeTranslator slimtranslator = new SlimProbeTranslator();

	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeDomain> create(ProbeDomain object, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeDomain> update(ProbeDomain object, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeDomain domain = new ProbeDomain();
		if (probeDAO.get(key) != null) {
			domain = translator.translate(probeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeDomain> getResults(Integer key) {
        SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
        results.setItem(translator.translate(probeDAO.get(key)));
        return results;
    }

	@Transactional	
	public SearchResults<ProbeDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		String cmd = "select count(*) as objectCount from prb_probe";
		
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
	public List<SlimProbeDomain> search(ProbeDomain searchDomain) {

		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from prb_probe a";
		String where = "where a._probe_key is not null";
		String orderBy = "order by a.name";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
		Boolean from_accession = false;
		Boolean from_source = false;
		Boolean from_strain = false;
		Boolean from_tissue = false;
		Boolean from_cellline = false;
		Boolean from_marker = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getSegmentTypeKey() != null && !searchDomain.getSegmentTypeKey().isEmpty()) {
			where = where + "\nand a._segmenttype_key = " + searchDomain.getSegmentTypeKey();
		}

		if (searchDomain.getVectorTypeKey() != null && !searchDomain.getVectorTypeKey().isEmpty()) {
			where = where + "\nand a._vector_key = " + searchDomain.getVectorTypeKey();
		}
		
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand a.name ilike '" + searchDomain.getName() + "'" ;
		}

		if (searchDomain.getRegionCovered() != null && !searchDomain.getRegionCovered().isEmpty()) {
			where = where + "\nand a.regioncovered ilike '" + searchDomain.getRegionCovered() + "'" ;
		}

		if (searchDomain.getPrimer1sequence() != null && !searchDomain.getPrimer1sequence().isEmpty()) {
			where = where + "\nand a.primer1sequence ilike '" + searchDomain.getPrimer1sequence() + "'" ;
		}

		if (searchDomain.getPrimer2sequence() != null && !searchDomain.getPrimer2sequence().isEmpty()) {
			where = where + "\nand a.primer2sequence ilike '" + searchDomain.getPrimer2sequence() + "'" ;
		}

		if (searchDomain.getInsertSite() != null && !searchDomain.getInsertSite().isEmpty()) {
			where = where + "\nand a.insertsite ilike '" + searchDomain.getInsertSite() + "'" ;
		}

		if (searchDomain.getInsertSize() != null && !searchDomain.getInsertSize().isEmpty()) {
			where = where + "\nand a.insertsize ilike '" + searchDomain.getInsertSize() + "'" ;
		}

		if (searchDomain.getProductSize() != null && !searchDomain.getProductSize().isEmpty()) {
			where = where + "\nand a.productsize ilike '" + searchDomain.getProductSize() + "'" ;
		}
		
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			from_accession = true;
		}	
		
		// probe source
		if (searchDomain.getProbeSource().getSourceKey() != null && !searchDomain.getProbeSource().getSourceKey().isEmpty()) {
			where = where + "\nand a._source_key = " + searchDomain.getProbeSource().getSourceKey();			
		}
		else {

			if (searchDomain.getProbeSource().getStrainKey() != null && !searchDomain.getProbeSource().getStrainKey().isEmpty()) {
				where = where + "\nand s._strain_key = " + searchDomain.getProbeSource().getStrainKey();
				from_source = true;				
			}
			else if (searchDomain.getProbeSource().getStrain() != null && !searchDomain.getProbeSource().getStrain().isEmpty()) {
				where = where + "\nand ss.strain ilike '" + searchDomain.getProbeSource().getStrain() + "'";
				from_source = true;
				from_strain = true;					
			}

			if (searchDomain.getProbeSource().getTissueKey() != null && !searchDomain.getProbeSource().getTissueKey().isEmpty()) {
				where = where + "\nand s._tissue_key = " + searchDomain.getProbeSource().getTissueKey();
				from_source = true;				
			}
			else if (searchDomain.getProbeSource().getTissue() != null && !searchDomain.getProbeSource().getTissue().isEmpty()) {
				where = where + "\nand st.tissue ilike '" + searchDomain.getProbeSource().getTissue() + "'";
				from_source = true;
				from_tissue = true;					
			}
				
			if (searchDomain.getProbeSource().getCellLineKey() != null && !searchDomain.getProbeSource().getCellLineKey().isEmpty()) {
				where = where + "\nand s._cellline_key = " + searchDomain.getProbeSource().getCellLineKey();
				from_source = true;				
			}
			else if (searchDomain.getProbeSource().getCellLine() != null && !searchDomain.getProbeSource().getCellLine().isEmpty()) {
				where = where + "\nand sc.term ilike '" + searchDomain.getProbeSource().getCellLine() + "'";
				from_source = true;
				from_cellline = true;					
			}		

			if (searchDomain.getProbeSource().getOrganismKey() != null && !searchDomain.getProbeSource().getOrganismKey().isEmpty()) {
				where = where + "\nand s._organism_key = " + searchDomain.getProbeSource().getOrganismKey();
				from_source = true;
			}
				
			if (searchDomain.getProbeSource().getDescription() != null && !searchDomain.getProbeSource().getDescription().isEmpty()) {
				where = where + "\nand s.description ilike '" + searchDomain.getProbeSource().getDescription() + "'" ;
				from_source = true;					
			}	

			if (searchDomain.getProbeSource().getGenderKey() != null && !searchDomain.getProbeSource().getGenderKey().isEmpty()) {
				where = where + "\nand s._gender_key = " + searchDomain.getProbeSource().getGenderKey();
				from_source = true;					
			}
			
			String agePrefix = "";
			String ageStage = "";
			if (searchDomain.getProbeSource().getAgePrefix() != null && !searchDomain.getProbeSource().getAgePrefix().isEmpty()) {
				agePrefix = searchDomain.getProbeSource().getAgePrefix() + "%";
			}
			if (searchDomain.getProbeSource().getAgeStage() != null && !searchDomain.getProbeSource().getAgeStage().isEmpty()) {
				ageStage = "%" + searchDomain.getProbeSource().getAgeStage() + "%";
			}
			if (agePrefix.length() > 0 || ageStage.length() > 0) {
				where = where + "\nand s.age ilike '" + agePrefix + ageStage + "'";
				from_source = true;									
			}
		}
		
		// markers
		if (searchDomain.getMarkers() != null) {
			
			if (searchDomain.getMarkers().get(0).getMarkerKey() != null && !searchDomain.getMarkers().get(0).getMarkerKey().isEmpty()) {
				where = where + "\nand m._marker_key = " + searchDomain.getMarkers().get(0).getMarkerKey();
				from_marker = true;
			}
			
			if (searchDomain.getMarkers().get(0).getRefsKey() != null && !searchDomain.getMarkers().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand m._refs_key = " + searchDomain.getMarkers().get(0).getRefsKey();
				from_marker = true;
			}
			
			if (searchDomain.getMarkers().get(0).getRelationship() != null && !searchDomain.getMarkers().get(0).getRelationship().isEmpty()) {
				if (searchDomain.getMarkers().get(0).getRelationship().isEmpty()) {
					where = where + "\nand m.relationship is null";
				}
				else {
					where = where + "\nand m.relationship = '" + searchDomain.getMarkers().get(0).getRelationship() + "'";
				}
				from_marker = true;
			}			
		}
	
		if (from_accession == true) {
			from = from + ", prb_probe_acc_view acc";
			where = where + "\nand a._assay_key = acc._object_key"; 
		}
		
		if (from_source == true) {
			from = from + ", prb_source s";
			where = where + "\nand a._source_key = s._source_key";
		}

		if (from_strain == true) {
			from = from + ", prb_strain ss";
			where = where + "\nand a._source_key = s._source_key"
					+ "\nand s._strain_key = ss._strain_key";
		}
	
		if (from_tissue == true) {
			from = from + ", prb_tissue st";
			where = where + "\nand a._source_key = s._source_key"
					+ "\nand s._tissue_key = st._tissue_key";
		}

		if (from_cellline == true) {
			from = from + ", voc_term sc";
			where = where + "\nand a._source_key = s._source_key"
					+ "\nand s._cellline_key = sc._term_key"
					+ "\nand sc._vocab_key = 18";
		}
		
		if (from_marker == true) {
			from = from + ", prb_marker m";
			where = where + "\nand a._probe_key = m._probe_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimProbeDomain domain = new SlimProbeDomain();
				domain = slimtranslator.translate(probeDAO.get(rs.getInt("_probe_key")));				
				probeDAO.clear();
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
