package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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

	@Transactional
	public SearchResults<AntigenDomain> create(AntigenDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		Antigen entity = new Antigen();
		String note = "";
		
		log.info("processAntigen/create");
		
		if(domain.getAntigenName() ==  null || domain.getAntigenName().isEmpty()) {
			entity.setAntigenName(null);
		}
		else {
			entity.setAntigenName(domain.getAntigenName());
		}
		
		if(domain.getRegionCovered() ==  null || domain.getRegionCovered().isEmpty()) {
			entity.setRegionCovered(null);
		}
		else {
			entity.setRegionCovered(domain.getRegionCovered());
		}
		
		if(domain.getAntigenNote() == null || domain.getAntigenNote().isEmpty()) {
			entity.setAntigenNote(null);
		}
		else {
			note = DecodeString.setDecodeToLatin9(domain.getAntigenNote());
			note = note.replace("''", "'");			
			entity.setAntigenNote(note);
		}
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
			
		// add antigen source
		log.info("processAntigen/sourceService.create() agePrefix: " + domain.getProbeSource().getAgePrefix() + " ageStage: " + domain.getProbeSource().getAgeStage());
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
		String note = "";
		
		log.info("processAntigen/update");
		
		if(domain.getAntigenName() ==  null || domain.getAntigenName().isEmpty()) {
			entity.setAntigenName(null);
		}
		else {
			entity.setAntigenName(domain.getAntigenName());
		}
		
		if(domain.getRegionCovered() ==  null || domain.getRegionCovered().isEmpty()) {
			entity.setRegionCovered(null);
		}
		else {
			entity.setRegionCovered(domain.getRegionCovered());
		}
		
		if(domain.getAntigenNote() == null || domain.getAntigenNote().isEmpty()) {
			entity.setAntigenNote(null);
		}
		else {
			note = DecodeString.setDecodeToLatin9(domain.getAntigenNote());
			note = note.replace("''", "'");			
			entity.setAntigenNote(note);		}	
		
		// update antigen source
		log.info("processAntigen/sourceService.update()");
		SearchResults<ProbeSourceDomain> sourceResults = new SearchResults<ProbeSourceDomain>();
		sourceResults = sourceService.update(domain.getProbeSource(), user);
		entity.setProbeSource(sourceDAO.get(Integer.valueOf(sourceResults.items.get(0).getSourceKey())));

		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		antigenDAO.update(entity);
		log.info("processAntigen/changes processed: " + domain.getAntigenKey());
			
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
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_antigen a";
		String where = "where a._antigen_key is not null";
		String orderBy = "order by a.antigenName";
		String value;
		Boolean from_accession = false;
		Boolean from_source = false;
		Boolean from_antibody = false;
		Boolean from_antibodyaccession = false;
		
		// if parameter exists, then add to where-clause
		// creation/modification by/date
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			if (! searchDomain.getAccID().startsWith("MGI:")) {
				where = where + "\nand acc.numericPart = '" + searchDomain.getAccID() + "'";
			}
			else {
				where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			}
			from_accession = true;
		}					

		// name
		if (searchDomain.getAntigenName() != null && ! searchDomain.getAntigenName().isEmpty()) {
			value = searchDomain.getAntigenName().replaceAll("'", "''");
			where = where + "\nand a.antigenName ilike '" + value + "'";
		}
		
		// region covered
		if (searchDomain.getRegionCovered() != null && !searchDomain.getRegionCovered().isEmpty()) {
			where = where + "\nand a.regionCovered ilike '" + searchDomain.getRegionCovered() + "'";
		}
		
		// notes
		if (searchDomain.getAntigenNote() != null && !searchDomain.getAntigenNote().isEmpty()) {
			value = searchDomain.getAntigenNote().replace("''", "'");			
			where = where + "\nand a.antigenNote ilike '" + value + "'";
		}
		
		// source
				
		if (searchDomain.getProbeSource() != null) {
			 log.info("AntigenService.search has search domain: " + searchDomain.getProbeSource().getTissue());

			// source organism
			if (searchDomain.getProbeSource().getOrganismKey() != null && !searchDomain.getProbeSource().getOrganismKey().isEmpty()) {
				where = where + "\nand s._organism_key = " + searchDomain.getProbeSource().getOrganismKey();
				from_source = true;
			}
			
			// source strain
			if (searchDomain.getProbeSource().getStrain() != null && !searchDomain.getProbeSource().getStrain().isEmpty()) {
				//where = where + "\nand s._strain_key = " + searchDomain.getProbeSource().getStrainKey();
				where = where + "\nand s.strain ilike '" + searchDomain.getProbeSource().getStrain() + "'";
				from_source = true;
			}
			
			// source tissue
			if (searchDomain.getProbeSource().getTissue() != null && !searchDomain.getProbeSource().getTissue().isEmpty()) {
				//where = where + "\nand s._Tissue_key = " + searchDomain.getProbeSource().getTissueKey();
				where = where + "\nand s.tissue ilike '" + searchDomain.getProbeSource().getTissue() + "'";
				from_source = true;
			}
			
			// source tissue description
			if (searchDomain.getProbeSource().getDescription() != null && !searchDomain.getProbeSource().getDescription().isEmpty()) {
				where = where + "\nand s.description ilike '" + searchDomain.getProbeSource().getDescription() + "'";
				from_source = true;
			}
			
			// source cell line
			if (searchDomain.getProbeSource().getCellLine() != null  && !searchDomain.getProbeSource().getCellLine().isEmpty() ) {
				where = where + "\nand s.cellline ilike '" + searchDomain.getProbeSource().getCellLine() + "'";
				from_source = true;
			}
			
			// source gender
			if (searchDomain.getProbeSource().getGenderKey() != null && ! searchDomain.getProbeSource().getGenderKey().isEmpty() ) {
				where = where + "\nand s._gender_key = " + searchDomain.getProbeSource().getGenderKey();
				from_source = true;
			}
			
			// source age
			String ageSearch = "";
			if (searchDomain.getProbeSource().getAgePrefix() != null && ! searchDomain.getProbeSource().getAgePrefix().isEmpty() ) {
				ageSearch = searchDomain.getProbeSource().getAgePrefix();
			}
			if (searchDomain.getProbeSource().getAgeStage() != null && ! searchDomain.getProbeSource().getAgeStage().isEmpty() ) {
				ageSearch = ageSearch + "%" + searchDomain.getProbeSource().getAgeStage();
			}			
			if (ageSearch.length() > 0) {
				where = where + "\nand s.age ilike '%" + ageSearch + "%'";
				from_source = true;	
			}
		}
		
		// antibodies
		if(searchDomain.getAntibodies() != null && ! searchDomain.getAntibodies().isEmpty()) {
			if (searchDomain.getAntibodies().get(0).getAccID()!= null && ! searchDomain.getAntibodies().get(0).getAccID().isEmpty()) {
				where = where + "\nand acc2.accID ilike '" + searchDomain.getAntibodies().get(0).getAccID() + "'";
				from_antibodyaccession = true;
			}
			else if (searchDomain.getAntibodies().get(0).getAntibodyName() != null && ! searchDomain.getAntibodies().get(0).getAntibodyName().isEmpty()) {
				where = where + "\nand aav.antibodyName ilike '" + searchDomain.getAntibodies().get(0).getAntibodyName()+ "'";
				from_antibody =  true;
			}
		}

		if (from_accession == true) {
			from = from + ", gxd_antigen_acc_view acc";
			where = where + "\nand a._antigen_key = acc._object_key"; 
		}
		
		if (from_antibodyaccession == true) {
			from = from + ", gxd_antibodyantigen_view aav, gxd_antibody_acc_view acc2";
			where = where + "\nand a._antigen_key = aav._antigen_key and aav._antibody_key = acc2._object_key"; 
		}
		
		if (from_antibody == true ) {
			from = from + ", gxd_antibodyantigen_view aav";
			where = where + "\nand a._antigen_key = aav._antigen_key";
		}
		
		if (from_source == true) {
			from = from + ", prb_source_view s";
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
	@Transactional	
	public AntigenDomain validateAntigenAcc(AntigenDomain searchDomain) {
		
		String where = "\nwhere mgiid = '" + searchDomain.getAccID() + "'";
		if (! searchDomain.getAccID().toUpperCase().startsWith("MGI:")) {
			where = "\nwhere numericPart = " + searchDomain.getAccID();
		}
		
		String cmd = "select _antigen_Key" 
				+ "\nfrom gxd_antigen_view"
				+ where;
		
		log.info(cmd);
		
		AntigenDomain domain = new AntigenDomain();
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				domain = translator.translate(antigenDAO.get(rs.getInt("_antigen_key")));				
				antigenDAO.clear();
			}
			sqlExecutor.cleanup();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		log.info("accID: " + domain.getAccID());
		return domain;
	}
}
