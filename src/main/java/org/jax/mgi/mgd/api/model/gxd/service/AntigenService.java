package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntigenDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antigen;
import org.jax.mgi.mgd.api.model.gxd.translator.AntigenTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAntigenTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeSourceDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeSourceService;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntigenService extends BaseService<AntigenDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntigenDAO antigenDAO;
	@Inject
	private ProbeSourceDAO sourceDAO;
	@Inject
	private ProbeSourceService sourceService;
		
	private AntigenTranslator translator = new AntigenTranslator();
	
	// for Search
	private SlimAntigenTranslator slimtranslator = new SlimAntigenTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AntigenDomain> create(AntigenDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		Antigen entity = new Antigen();
		
		log.info("processAntigen/create");
		
		// may not be null
		entity.setAntigenName(domain.getAntigenName());
		
		// may be null
		if(domain.getRegionCovered() ==  null || domain.getRegionCovered().isEmpty()) {
			entity.setRegionCovered(null);
		}
		else {
			entity.setRegionCovered(domain.getRegionCovered());
		}
		
		// may be null
		if(domain.getAntigenNote() == null || domain.getAntigenNote().isEmpty()) {
			entity.setAntigenNote(null);
		}
		else {
			entity.setAntigenNote(domain.getAntigenNote());
		}
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
			
		// add antigen source
		log.info("processAntigen/sourceService.update()");
		SearchResults<ProbeSourceDomain> sourceResults = new SearchResults<ProbeSourceDomain>();
		sourceResults = sourceService.create(domain.getProbeSource(), user);
		entity.setProbeSource(sourceDAO.get(Integer.valueOf(sourceResults.items.get(0).getSourceKey())));
		
		// execute persist/insert/send to database
		antigenDAO.persist(entity);
		
		// return entity translated to domain
		log.info("processAntigen/create/returning results");
		results.setItem(translator.translate(entity));
		return results;		
	}

	@Transactional
	public SearchResults<AntigenDomain> update(AntigenDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		Antigen entity = antigenDAO.get(Integer.valueOf(domain.getAntigenKey()));
		Boolean modified = false;
		
		log.info("processAntigen/update");
		
		// may not be null
		if(entity.getAntigenName() != domain.getAntigenName()) {
			entity.setAntigenName(domain.getAntigenName());
			modified = true;
		}
		
		// may be null
		if(domain.getRegionCovered() ==  null || domain.getRegionCovered().isEmpty()) {
			entity.setRegionCovered(null);
		}
		else {
			if (entity.getRegionCovered() != domain.getRegionCovered()) {
				entity.setRegionCovered(domain.getRegionCovered());
				modified = true;
			}
		}
		
		// may be null
		if(domain.getAntigenNote() == null || domain.getAntigenNote().isEmpty()) {
			entity.setAntigenNote(null);
		}
		else {
			if(entity.getAntigenNote() != domain.getAntigenName()) {
				entity.setAntigenNote(domain.getAntigenNote());
				modified = true;
			}
		}	
		
		// update antigen source
		log.info("processAntigen/sourceService.update()");
		SearchResults<ProbeSourceDomain> sourceResults = new SearchResults<ProbeSourceDomain>();
		sourceResults = sourceService.update(domain.getProbeSource(), user);
		entity.setProbeSource(sourceDAO.get(Integer.valueOf(sourceResults.items.get(0).getSourceKey())));

		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			antigenDAO.update(entity);
			log.info("processAntigen/changes processed: " + domain.getAntigenKey());
		}
		else {
			log.info("processAntigen/no changes processed: " + domain.getAntigenKey());
		}
			
		// return entity translated to domain
		log.info("processAntigen/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAntigen/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<AntigenDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		Antigen entity = antigenDAO.get(key);
		results.setItem(translator.translate(antigenDAO.get(key)));
		antigenDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public AntigenDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntigenDomain domain = new AntigenDomain();
		if (antigenDAO.get(key) != null) {
			domain = translator.translate(antigenDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntigenDomain> getResults(Integer key) {
        SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
        results.setItem(translator.translate(antigenDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntigenDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		String cmd = "select count(*) as objectCount from gxd_antigen";
		
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
	public List<SlimAntigenDomain> search(AntigenDomain searchDomain) {

		List<SlimAntigenDomain> results = new ArrayList<SlimAntigenDomain>();
		
		log.info("domain toString: " + searchDomain.toString());
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_antigen a";
		String where = "where a._antigen_key is not null";
		String orderBy = "order by a.antigenName";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
		Boolean from_accession = false;
		Boolean from_source = false;
		
		// if parameter exists, then add to where-clause
		// creation/modification by/date
		log.info("createdBy: " + searchDomain.getCreatedBy() + " modifiedBy: " + searchDomain.getModifiedBy() + " createDate: " + searchDomain.getCreation_date() + "modDate" + searchDomain.getModification_date());
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			from_accession = true;
		}						
	
		if (from_accession == true) {
			from = from + ", gxd_antigen_acc_view acc";
			where = where + "\nand a._antigen_key = acc._object_key"; 
		}

		// name
		if (searchDomain.getAntigenName() != null && ! searchDomain.getAntigenName().isEmpty()) {
			where = where + "\nand a.antigenName ilike '" + searchDomain.getAntigenName() + "'";
		}
		
		// region covered
		if (searchDomain.getRegionCovered() != null && !searchDomain.getRegionCovered().isEmpty()) {
			where = where + "\nand a.regionCovered ilike '" + searchDomain.getRegionCovered() + "'";
		}
		
		// notes
		if (searchDomain.getAntigenNote() != null && !searchDomain.getAntigenNote().isEmpty()) {
			where = where + "\nand a.antigenNote ilike '" + searchDomain.getAntigenNote() + "'";
		}
		
		// source organism
				
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getOrganismKey() != null && !searchDomain.getProbeSource().getOrganismKey().isEmpty()) {
			where = where + "\nand  s._organism_key = " + searchDomain.getProbeSource().getOrganismKey();
			from_source = true;
		}
		
		// source strain
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getStrainKey() != null && !searchDomain.getProbeSource().getStrainKey().isEmpty()) {
			where = where + "\nand  s._strain_key = " + searchDomain.getProbeSource().getStrainKey();
			from_source = true;
		}
		
		// source tissue
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getTissueKey() != null && !searchDomain.getProbeSource().getTissueKey().isEmpty()) {
			where = where + "\nand  s._Tissue_key = " + searchDomain.getProbeSource().getTissueKey();
			from_source = true;
		}
		
		// source tissue description
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getDescription() != null && !searchDomain.getProbeSource().getDescription().isEmpty()) {
			where = where + "\nand s.description ilike '" + searchDomain.getProbeSource().getDescription() + "'";
			from_source = true;
		}
		
		// source cell line
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getCellLineKey() != null  && !searchDomain.getProbeSource().getCellLineKey().isEmpty() ) {
			where = where + "\nand s._cellline_key = " + searchDomain.getProbeSource().getCellLineKey();
			from_source = true;
		}
		
		// source gender
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getGenderKey() != null && ! searchDomain.getProbeSource().getGenderKey().isEmpty() ) {
			where = where + "\nand s._gender_key = " + searchDomain.getProbeSource().getGenderKey();
			from_source = true;
		}
		
		// source age
		if (searchDomain.getProbeSource() != null && searchDomain.getProbeSource().getAge() != null && ! searchDomain.getProbeSource().getAge().isEmpty() ) {
			where = where + "\nand s.age ilike '" + searchDomain.getProbeSource().getAge() + "'";
			from_source = true;
		}
		
		if (from_source == true) {
			from = from + ", prb_source s";
			where = where + "\nand a._source_key = s._source_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info("cmd: " + cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAntigenDomain domain = new SlimAntigenDomain();
				domain = slimtranslator.translate(antigenDAO.get(rs.getInt("_antigen_key")));				
				antigenDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional	
	public List<SlimAntibodyDomain> getAntibodies(Integer key) {
		// get antibodies by antigen key 
		// return AntibodyDomain
		
		List<SlimAntibodyDomain> results = new ArrayList<SlimAntibodyDomain>();

		String cmd = "select distinct g._antibody_key, g.antibodyName, acc.accID"
				+ "\nfrom gxd_antibody g, gxd_antibody_acc_view acc"
				+ "\nwhere g._antigen_key = " + key
				+ "\nand g._antibody_key = acc._object_key"
				+ "\norder by antibodyName";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAntibodyDomain domain = new SlimAntibodyDomain();
				domain.setAntibodyKey(rs.getString("_antibody_key"));
				domain.setAntibodyName(rs.getString("antibodyName"));				
				domain.setAccID(rs.getString("accID"));								
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		return(results);
	}		
}
