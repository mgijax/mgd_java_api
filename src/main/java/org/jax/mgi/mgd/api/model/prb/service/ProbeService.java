package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.AccessionDAO;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeSourceDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeSummaryDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SummaryProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeSummaryTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class ProbeService extends BaseService<ProbeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeDAO probeDAO;
	@Inject
	private ProbeSourceDAO sourceDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private AccessionDAO accDAO;
	
	@Inject
	private ProbeMarkerService markerService;
	@Inject
	private ProbeReferenceService referenceService;
	@Inject
	private ProbeSourceService sourceService;
	@Inject
	private ProbeNoteService probeNoteService;
	@Inject
	private NoteService noteService;
	
	private ProbeTranslator translator = new ProbeTranslator();
	private SlimProbeTranslator slimtranslator = new SlimProbeTranslator();
	private SlimProbeSummaryTranslator slimsummarytranslator = new SlimProbeSummaryTranslator();
	private AccessionTranslator acctranslator = new AccessionTranslator();

	private String mgiTypeKey = "3";
	
	@Transactional
	public SearchResults<ProbeDomain> create(ProbeDomain domain, User user) {	
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		Probe entity = new Probe();
		
		log.info("processProbe/create");		

		// default = Not Applicable
		if (domain.getSegmentTypeKey() == null || domain.getSegmentTypeKey().isEmpty()) {
			domain.setSegmentTypeKey("63474");
		}
		
		// primer
		if (domain.getSegmentTypeKey().equals("63473")) {		
			
			// default = Not Applicable
			if (domain.getVectorTypeKey() == null || domain.getVectorTypeKey().isEmpty()) {
				domain.setVectorTypeKey("316369");
			}		
			
			entity.setInsertSite(null);
			entity.setInsertSize(null);
			entity.setDerivedFrom(null);
			entity.setAmpPrimer(null);
			
			if (domain.getPrimer1sequence() == null || domain.getPrimer1sequence().isEmpty()) {
				entity.setPrimer1sequence(null);
			}
			else {
				entity.setPrimer1sequence(domain.getPrimer1sequence());
			}
	
			if (domain.getPrimer2sequence() == null || domain.getPrimer2sequence().isEmpty()) {
				entity.setPrimer2sequence(null);
			}
			else {
				entity.setPrimer2sequence(domain.getPrimer2sequence());
			}
			
			if (domain.getProductSize() == null || domain.getProductSize().isEmpty()) {
				entity.setProductSize(null);
			}
			else {
				entity.setProductSize(domain.getProductSize());
			}			
		}
		
		// molecular segment
		else {
			
			// default = Not Specified
			if (domain.getVectorTypeKey() == null || domain.getVectorTypeKey().isEmpty()) {
				domain.setVectorTypeKey("316370");
			}
			
			entity.setPrimer1sequence(null);
			entity.setPrimer2sequence(null);
			entity.setProductSize(null);
			
			if (domain.getDerivedFromAccID() == null || domain.getDerivedFromAccID().isEmpty()) {
				entity.setDerivedFrom(null);
			}
			else {
				entity.setDerivedFrom(probeDAO.get(Integer.valueOf(domain.getDerivedFromKey())));
			}
			
			if (domain.getAmpPrimerAccID() == null || domain.getAmpPrimerAccID().isEmpty()) {
				entity.setAmpPrimer(null);
			}
			else {
				entity.setAmpPrimer(probeDAO.get(Integer.valueOf(domain.getAmpPrimerKey())));
			}
			
			if (domain.getInsertSite() == null || domain.getInsertSite().isEmpty()) {
				entity.setInsertSite(null);
			}
			else {
				entity.setInsertSite(domain.getInsertSite());
			}

			if (domain.getInsertSize() == null || domain.getInsertSize().isEmpty()) {
				entity.setInsertSize(null);
			}
			else {
				entity.setInsertSize(domain.getInsertSize());
			}					
		}

		entity.setName(domain.getName());
		
		if (domain.getRegionCovered() == null || domain.getRegionCovered().isEmpty()) {
			entity.setRegionCovered(null);
		}
		else {
			entity.setRegionCovered(domain.getRegionCovered());
		}
		
		entity.setSegmentType(termDAO.get(Integer.valueOf(domain.getSegmentTypeKey())));
		entity.setVectorType(termDAO.get(Integer.valueOf(domain.getVectorTypeKey())));		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
			
		// can add an anonymous probe source
		if (domain.getProbeSource().getName() == null || domain.getProbeSource().getName().isEmpty()) {
			
			// if primer, default Organism, Strain, Tissue, Cell Line, Age, and Sex = “Not Applicable.”
			if (domain.getSegmentTypeKey().equals("63473")) {		
				domain.getProbeSource().setSegmentTypeKey("63473");
				domain.getProbeSource().setVectorKey("316369");
				domain.getProbeSource().setOrganismKey("74");
				domain.getProbeSource().setStrainKey("-2");
				domain.getProbeSource().setTissueKey("-2");
				domain.getProbeSource().setCellLineKey("316336");
				domain.getProbeSource().setAgePrefix("Not Applicable");
				domain.getProbeSource().setGenderKey("315168");
			}
			
			// else, defaults = "Not Specified" in sourceService.create()

			SearchResults<ProbeSourceDomain> sourceResults = new SearchResults<ProbeSourceDomain>();
			sourceResults = sourceService.create(domain.getProbeSource(), user);
			entity.setProbeSource(sourceDAO.get(Integer.valueOf(sourceResults.items.get(0).getSourceKey())));
		}
		else {
			entity.setProbeSource(sourceDAO.get(Integer.valueOf(domain.getProbeSource().getSourceKey())));	
		}
		
		// execute persist/insert/send to database
		probeDAO.persist(entity);

		probeNoteService.process(String.valueOf(entity.get_probe_key()), domain.getGeneralNote(), user);
		noteService.process(String.valueOf(entity.get_probe_key()), domain.getRawsequenceNote(), mgiTypeKey, user);
		markerService.process(String.valueOf(entity.get_probe_key()), domain.getMarkers(), user);
		referenceService.process(String.valueOf(entity.get_probe_key()), domain.getReferences(), user);
		
		// return entity translated to domain
		log.info("processProbe/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<ProbeDomain> update(ProbeDomain domain, User user) {
		// update existing entity object from in-coming domain
		
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		Probe entity = probeDAO.get(Integer.valueOf(domain.getProbeKey()));
		Boolean modified = true;
		
		log.info("processProbe/update");
				
		entity.setName(domain.getName());
		entity.setRegionCovered(domain.getRegionCovered());
		entity.setSegmentType(termDAO.get(Integer.valueOf(domain.getSegmentTypeKey())));	
		entity.setVectorType(termDAO.get(Integer.valueOf(domain.getVectorTypeKey())));	
		
		// primer
		if (domain.getSegmentTypeKey().equals("63473")) {		
	
			entity.setInsertSite(null);
			entity.setInsertSize(null);
			entity.setDerivedFrom(null);
			entity.setAmpPrimer(null);
			
			if (domain.getPrimer1sequence() == null || domain.getPrimer1sequence().isEmpty()) {
				entity.setPrimer1sequence(null);
			}
			else {
				entity.setPrimer1sequence(domain.getPrimer1sequence());
			}
	
			if (domain.getPrimer2sequence() == null || domain.getPrimer2sequence().isEmpty()) {
				entity.setPrimer2sequence(null);
			}
			else {
				entity.setPrimer2sequence(domain.getPrimer2sequence());
			}
			
			if (domain.getProductSize() == null || domain.getProductSize().isEmpty()) {
				entity.setProductSize(null);
			}
			else {
				entity.setProductSize(domain.getProductSize());
			}			
		}
		
		// molecular segment
		else {

			entity.setPrimer1sequence(null);
			entity.setPrimer2sequence(null);
			entity.setProductSize(null);
			
			if (domain.getDerivedFromAccID() == null || domain.getDerivedFromAccID().isEmpty()) {
				entity.setDerivedFrom(null);
			}
			else {
				entity.setDerivedFrom(probeDAO.get(Integer.valueOf(domain.getDerivedFromKey())));
			}
			
			if (domain.getAmpPrimerAccID() == null || domain.getAmpPrimerAccID().isEmpty()) {
				entity.setAmpPrimer(null);
			}
			else {
				entity.setAmpPrimer(probeDAO.get(Integer.valueOf(domain.getAmpPrimerKey())));
			}
			
			if (domain.getInsertSite() == null || domain.getInsertSite().isEmpty()) {
				entity.setInsertSite(null);
			}
			else {
				entity.setInsertSite(domain.getInsertSite());
			}

			if (domain.getInsertSize() == null || domain.getInsertSize().isEmpty()) {
				entity.setInsertSize(null);
			}
			else {
				entity.setInsertSize(domain.getInsertSize());
			}					
		}

		// can only modify an anonymous source
		if (domain.getProbeSource().getName() == null || domain.getProbeSource().getName().isEmpty()) {
			sourceService.update(domain.getProbeSource(), user);
		}
		entity.setProbeSource(sourceDAO.get(Integer.valueOf(domain.getProbeSource().getSourceKey())));	
		
		if (probeNoteService.process(domain.getProbeKey(), domain.getGeneralNote(), user)) {
			modified = true;
		}
		if (noteService.process(domain.getProbeKey(), domain.getRawsequenceNote(), mgiTypeKey, user)) {
			modified = true;			
		}
		if (markerService.process(domain.getProbeKey(), domain.getMarkers(), user)) {
			modified = true;			
		}
		if (referenceService.process(domain.getProbeKey(), domain.getReferences(), user)) {
			modified = true;			
		}
		
		// finish update
		if (modified) {		
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			probeDAO.update(entity);
			log.info("processProbe/changes processed: " + domain.getProbeKey());		
		}
		
		// return entity translated to domain
		log.info("processProbe/update/returning results");
		results.setItem(translator.translate(entity));		
		log.info("processProbe/update/returned results succsssful");
		return results;			
	}

	@Transactional
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		Probe entity = probeDAO.get(key);
		results.setItem(translator.translate(probeDAO.get(key)));
		probeDAO.remove(entity);
		return results;
	}

	@Transactional
	public ProbeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain

		ProbeDomain domain = new ProbeDomain();

		if (probeDAO.get(key) != null) {
			domain = translator.translate(probeDAO.get(key));
			
	        // determine hasExpression
	    	String cmd = "select case when exists (select 1 from gxd_probeprep p, gxd_assay e" +
	    				"\nwhere p._probe_key = " + key +
	    				"\nand p._probeprep_key = e._probeprep_key)" +
	    				"\nthen 1 else 0 end as hasExpression";
	    	log.info(cmd);	
	    	try {
	    		ResultSet rs = sqlExecutor.executeProto(cmd);
	    		while (rs.next()) {
	    			domain.setHasExpression(rs.getString("hasExpression"));
	    		}
	    		//sqlExecutor.cleanup();
	    	}
	    	catch (Exception e) {
	    		e.printStackTrace();
	    	}	
	    	
			// attach childClones
			try {
				List<SlimProbeSummaryDomain> childClones = new ArrayList<SlimProbeSummaryDomain>();
				childClones = getChildClones(key);
				if (!childClones.isEmpty()) {
					domain.setChildClones(childClones);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// attach accession ids for each prb_reference
			if (domain.getReferences() != null && !domain.getReferences().isEmpty()) {
				for (int i = 0; i < domain.getReferences().size(); i++) {
					List<AccessionDomain> accessionIds = new ArrayList<AccessionDomain>();
					accessionIds = searchReferences(domain.getProbeKey(), domain.getReferences().get(i).getReferenceKey());
					if (!accessionIds.isEmpty()) {
						domain.getReferences().get(i).setAccessionIds(accessionIds);
					}
				}
				try {
					sqlExecutor.cleanup();
				}
				catch (Exception e) {
					e.printStackTrace();
				}				
			}	
			
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
		 
		String cmd = "";
		String select = "select distinct p._probe_key, p.name";
		String from = "from prb_probe p";
		String where = "where p._probe_key is not null";
		String orderBy = "order by p.name";
		String value;
		Boolean from_accession = false;
		Boolean from_raccession = false;
		Boolean from_parentclone = false;
		Boolean from_ampprimer = false;
		Boolean from_source = false;
		Boolean from_strain = false;
		Boolean from_tissue = false;
		Boolean from_cellline = false;
		Boolean from_marker = false;
		Boolean from_reference = false;
		Boolean from_alias = false;
		Boolean from_generalNote = false;
		Boolean from_rawsequenceNote = false;
		Boolean usedCM = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("p", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
			usedCM = true;
		}

		if (searchDomain.getSegmentTypeKey() != null && !searchDomain.getSegmentTypeKey().isEmpty()) {
			where = where + "\nand p._segmenttype_key = " + searchDomain.getSegmentTypeKey();
		}

		if (searchDomain.getVectorTypeKey() != null && !searchDomain.getVectorTypeKey().isEmpty()) {
			where = where + "\nand p._vector_key = " + searchDomain.getVectorTypeKey();
		}
		
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			value = searchDomain.getName().replaceAll("'", "''");
			where = where + "\nand p.name ilike '" + value + "'" ;
		}

		if (searchDomain.getRegionCovered() != null && !searchDomain.getRegionCovered().isEmpty()) {
			value = searchDomain.getRegionCovered().replace("'", "''");
			where = where + "\nand p.regioncovered ilike '" + value + "'" ;
		}

		if (searchDomain.getPrimer1sequence() != null && !searchDomain.getPrimer1sequence().isEmpty()) {
			where = where + "\nand p.primer1sequence ilike '" + searchDomain.getPrimer1sequence() + "'" ;
		}

		if (searchDomain.getPrimer2sequence() != null && !searchDomain.getPrimer2sequence().isEmpty()) {
			where = where + "\nand p.primer2sequence ilike '" + searchDomain.getPrimer2sequence() + "'" ;
		}

		if (searchDomain.getInsertSite() != null && !searchDomain.getInsertSite().isEmpty()) {
			where = where + "\nand p.insertsite ilike '" + searchDomain.getInsertSite() + "'" ;
		}

		if (searchDomain.getInsertSize() != null && !searchDomain.getInsertSize().isEmpty()) {
			where = where + "\nand p.insertsize ilike '" + searchDomain.getInsertSize() + "'" ;
		}

		if (searchDomain.getProductSize() != null && !searchDomain.getProductSize().isEmpty()) {
			where = where + "\nand p.productsize ilike '" + searchDomain.getProductSize() + "'" ;
		}
		
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {
			value = searchDomain.getAccID().toUpperCase();
			if (!value.startsWith("MGI:")) {
				where = where + "\nand acc.numericPart = '" + value + "'";
			}
			else {
				where = where + "\nand acc.accID = '" + value + "'";
			}
			from_accession = true;
		}	
		
		// parent clone
		if (searchDomain.getDerivedFromAccID() != null && !searchDomain.getDerivedFromAccID().isEmpty()) {
			where = where + "\nand pc.accID ilike '" + searchDomain.getDerivedFromAccID() + "'";
			from_parentclone = true;			
		}
		
		// amp primer
		if (searchDomain.getAmpPrimerAccID() != null && !searchDomain.getAmpPrimerAccID().isEmpty()) {
			where = where + "\nand pamp.accID ilike '" + searchDomain.getAmpPrimerAccID() + "'";
			from_ampprimer = true;			
		}
		
		// probe source
		if (searchDomain.getProbeSource() != null) {
			
			if (searchDomain.getProbeSource().getSourceKey() != null && !searchDomain.getProbeSource().getSourceKey().isEmpty()) {
				where = where + "\nand p._source_key = " + searchDomain.getProbeSource().getSourceKey();			
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
					ageStage = "% " + searchDomain.getProbeSource().getAgeStage();
				}
				if (agePrefix.length() > 0 || ageStage.length() > 0) {
					where = where + "\nand s.age ilike '" + agePrefix + ageStage + "'";
					from_source = true;									
				}
			}
		}
		
		// markers
		if (searchDomain.getMarkers() != null) {
			
			if (searchDomain.getMarkers().get(0).getMarkerKey() != null && !searchDomain.getMarkers().get(0).getMarkerKey().isEmpty()) {
				where = where + "\nand m._marker_key = " + searchDomain.getMarkers().get(0).getMarkerKey();
				from_marker = true;
			}
			
			else if (searchDomain.getMarkers().get(0).getMarkerSymbol() != null && !searchDomain.getMarkers().get(0).getMarkerSymbol().isEmpty()) {
				where = where + "\nand m.symbol ilike '" + searchDomain.getMarkers().get(0).getMarkerSymbol() + "'";
				from_marker = true;
			}
			
			if (searchDomain.getMarkers().get(0).getRefsKey() != null && !searchDomain.getMarkers().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand m._refs_key = " + searchDomain.getMarkers().get(0).getRefsKey();
				from_marker = true;
			}
			
			if (searchDomain.getMarkers().get(0).getRelationship() != null && !searchDomain.getMarkers().get(0).getRelationship().isEmpty()) {
				if (searchDomain.getMarkers().get(0).getRelationship().equals("(none)")) {
					where = where + "\nand m.relationship is null";
				}
				else {
					where = where + "\nand m.relationship = '" + searchDomain.getMarkers().get(0).getRelationship() + "'";
				}
				from_marker = true;
			}
			
			if (usedCM == false) {
				String markercmResults[] = DateSQLQuery.queryByCreationModification("m", 
						searchDomain.getMarkers().get(0).getCreatedBy(), 
						searchDomain.getMarkers().get(0).getModifiedBy(), 
						searchDomain.getMarkers().get(0).getCreation_date(), 
						searchDomain.getMarkers().get(0).getModification_date());
			
				if (markercmResults.length > 0) {
					if (markercmResults[0].length() > 0 || markercmResults[1].length() > 0) {
						from = from + markercmResults[0];
						where = where + markercmResults[1];
						from_marker = true;
						usedCM = true;
					}
				}
			}
		}

		// references
		if (searchDomain.getReferences() != null) {			
			
			if (searchDomain.getReferences().get(0).getRefsKey() != null && !searchDomain.getReferences().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand r._refs_key = " + searchDomain.getReferences().get(0).getRefsKey();
				from_reference = true;
			}

			if (searchDomain.getReferences().get(0).getAccessionIds() != null) {
				if (searchDomain.getReferences().get(0).getAccessionIds().get(0).getAccID() != null && !searchDomain.getReferences().get(0).getAccessionIds().get(0).getAccID().isEmpty()) {
					if (searchDomain.getReferences().get(0).getAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getReferences().get(0).getAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
						where = where + "\nand racc._logicaldb_key = " + searchDomain.getReferences().get(0).getAccessionIds().get(0).getLogicaldbKey();
					}	
					where = where + "\nand racc.accID ilike '" + searchDomain.getReferences().get(0).getAccessionIds().get(0).getAccID() + "'";
					from_reference = true;
					from_raccession = true;			
				}				
			}
			
			if (searchDomain.getReferences().get(0).getAliases() != null) {
				if (searchDomain.getReferences().get(0).getAliases().get(0).getAlias() != null && !searchDomain.getReferences().get(0).getAliases().get(0).getAlias().isEmpty()) {
					where = where + "\nand a.alias ilike '" + searchDomain.getReferences().get(0).getAliases().get(0).getAlias() + "'";
					from_reference = true;
					from_alias = true;
				}				
			}
		}

		if (searchDomain.getGeneralNote() != null && !searchDomain.getGeneralNote().getNote().isEmpty()) {
			value = searchDomain.getGeneralNote().getNote().replace("'",  "''");
			where = where + "\nand note1.note ilike '" + value + "'" ;
			from_generalNote = true;
		}
		
		if (searchDomain.getRawsequenceNote() != null && !searchDomain.getRawsequenceNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getRawsequenceNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note2.note ilike '" + value + "'" ;
			from_rawsequenceNote = true;
		}
		
		// building from...
		
		if (from_accession == true) {
			from = from + ", acc_accession acc";
			where = where + "\nand acc._mgitype_key = 3 and p._probe_key = acc._object_key and acc.prefixPart = 'MGI:'";
		}

		if (from_parentclone == true) {
			from = from + ", acc_accession pc";
			where = where + "\nand p.derivedfrom is not null and p.derivedfrom = pc._object_key and pc._mgitype_key = 3"; 
		}
		
		if (from_ampprimer == true) {
			from = from + ", acc_accession pamp";
			where = where + "\nand p.ampprimer is not null and p.ampprimer = pamp._object_key and pamp._mgitype_key = 3 "; 
		}
		
		if (from_source == true) {
			from = from + ", prb_source s";
			where = where + "\nand p._source_key = s._source_key";
		}

		if (from_strain == true) {
			from = from + ", prb_strain ss";
			where = where + "\nand s._strain_key = ss._strain_key";
		}
	
		if (from_tissue == true) {
			from = from + ", prb_tissue st";
			where = where + "\nand s._tissue_key = st._tissue_key";
		}

		if (from_cellline == true) {
			from = from + ", voc_term sc";
			where = where + "\nand s._cellline_key = sc._term_key and sc._vocab_key = 18";
		}
		
		if (from_marker == true) {
			from = from + ", prb_marker_view m";
			where = where + "\nand p._probe_key = m._probe_key";
		}

		if (from_reference == true) {
			from = from + ", prb_reference r";
			where = where + "\nand p._probe_key = r._probe_key";
		}
		
		if (from_raccession == true) {
			from = from + ", acc_accession racc, acc_accessionreference rracc";
			where = where + "\nand p._probe_key = racc._object_key"
					+ "\nand r._refs_key = rracc._refs_key"
					+ "\nand racc._mgitype_key = 3"
					+ "\nand rracc._accession_key = racc._accession_key";
		}

		if (from_alias == true) {
			from = from + ", prb_alias a";
			where = where + "\nand r._reference_key = a._reference_key";
		}
	
		if (from_generalNote == true) {
			from = from + ", prb_notes note1";
			where = where + "\nand p._probe_key = note1._probe_key";
		}
		
		if (from_rawsequenceNote == true) {
			from = from + ", mgi_note_probe_view note2";
			where = where + "\nand p._probe_key = note2._object_key";
			where = where + "\nand note2._notetype_key = 1037";
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

	@Transactional
	public List<AccessionDomain> searchReferences(String probeKey, String referenceKey) {
		// select prb_accref_view for given probe
		
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();
		
		String cmd = "\nselect _accession_key"
			+ "\nfrom PRB_AccRef_View"
			+ "\nwhere _object_key = " + probeKey
			+ "\nand _reference_key = " + referenceKey
			+ "\norder by _reference_key, logicaldb, accid";
		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AccessionDomain domain = new AccessionDomain();
				domain = acctranslator.translate(accDAO.get(rs.getInt("_accession_key")));				
				accDAO.clear();
				results.add(domain);
			}
			// do not cleanup() until after all calls have been made
			//sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public List<SlimProbeDomain> validateProbe(SlimProbeDomain searchDomain) {
		
		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();
		
		String value = searchDomain.getAccID().toUpperCase();
		if (!value.contains("MGI:")) {
			value = "MGI:" + value;
		}
		
		String cmd = "select accID, _object_key, description from PRB_Acc_View"
				+ "\nwhere accID = '" + value + "'";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimProbeDomain slimdomain = new SlimProbeDomain();
				slimdomain.setAccID(rs.getString("accID"));
				slimdomain.setProbeKey(rs.getString("_object_key"));
				slimdomain.setName(rs.getString("description"));
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public List<SlimProbeDomain> validateAmpPrimer(SlimProbeDomain searchDomain) {
		// validate the amp primer 
		// segmentype of amp primer = "primer" (63473)
		
		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();
		
		String value = searchDomain.getAccID().toUpperCase();
		if (!value.contains("MGI:")) {
			value = "MGI:" + value;
		}
		
		String cmd = "select a.accID, a._object_key, a.description"
				+ "\nfrom PRB_Acc_View a, PRB_Probe p"
				+ "\nwhere a.accID = '" + value + "'"
				+ "\nand a._object_key = p._probe_key"
				+ "\nand p._segmenttype_key = 63473";
		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimProbeDomain slimdomain = new SlimProbeDomain();
				slimdomain.setAccID(rs.getString("accID"));
				slimdomain.setProbeKey(rs.getString("_object_key"));
				slimdomain.setName(rs.getString("description"));
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	// --------------------------------------------------
	// get probes by marker

	public String getProbeBySQL(String queryType, String accid, int offset, int limit, boolean returnCount) {
		String probesQ = "";
		if (queryType.equals("marker")) {
			probesQ = ""
			+ "\nselect distinct p._probe_key, p.name"
			+ "\n  from prb_probe p, prb_marker pm, acc_accession ma "
			+ "\n  where p._probe_key = pm._probe_key "
			+ "\n  and pm._marker_key = ma._object_key "
			+ "\n  and ma._mgitype_key = 2 "
			+ "\n  and ma._logicaldb_key = 1 "
			+ "\n  and ma.accid = '" + accid + "' "
			;
		} else if (queryType.equals("reference")) {
			probesQ = ""
			+ "\nselect distinct p._probe_key, p.name"
			+ "\n  from prb_probe p, prb_reference pr, acc_accession ra "
			+ "\n  where p._probe_key = pr._probe_key "
			+ "\n  and pr._refs_key = ra._object_key "
			+ "\n  and ra._mgitype_key = 1 "
			+ "\n  and ra._logicaldb_key = 1 "
			+ "\n  and ra.accid = '" + accid + "' "
			;
		} else if (queryType.equals("search")) {
			probesQ = accid;
		} else {
		    throw new RuntimeException("Unknown query type: " + queryType);
		}

		if (returnCount) {
		    return "\nwith probes as (" + probesQ + ")\nselect count(*) as total_count from probes";
		} else {
		    probesQ = addPaginationSQL(probesQ, "name", offset, limit);
		}

		String cmd = "\nwith probes as materialized (" 
		+ probesQ
		+ "\n)," 
		+ "\nprobeMarkers as materialized ( "
		+ "\n  select p._probe_key, "
		+ "\n     array_to_string(array_agg(mm.symbol), '|'::text) AS markers, "
		+ "\n     array_to_string(array_agg(ma.accid), '|'::text) AS markerids "
		+ "\n  from probes p "
		+ "\n    left outer join prb_marker pm "
		+ "\n        on p._probe_key = pm._probe_key"
		+ "\n    left outer join mrk_marker mm"
		+ "\n        on pm._marker_key = mm._marker_key"
		+ "\n    left outer join acc_accession ma"
		+ "\n        on mm._marker_key = ma._object_key"
		+ "\n        and ma._mgitype_key = 2"
		+ "\n        and ma._logicaldb_key = 1"
		+ "\n        and ma.preferred = 1"
		+ "\n  group by p._probe_key "
		+ "\n), "
		+ "\nprobeAliases as materialized ( "
		+ "\n  select p._probe_key, "
		+ "\n    array_to_string(array_agg(DISTINCT pa.alias), '|'::text) AS aliases "
		+ "\n  from probes p "
		+ "\n    left outer join prb_reference pr "
		+ "\n        on p._probe_key = pr._probe_key"
		+ "\n    left outer join prb_alias pa "
		+ "\n        on pr._reference_key = pa._reference_key"
		+ "\n  group by p._probe_key "
		+ "\n), "
		+ "\nprobeJnums as materialized ( "
		+ "\n  select p._probe_key, "
		+ "\n    array_to_string(array_agg(DISTINCT ra.accid), '|'::text) AS jnums "
		+ "\n  from probes p"
		+ "\n    left outer join prb_reference pr"
		+ "\n        on p._probe_key = pr._probe_key"
		+ "\n    left outer join acc_accession ra"
		+ "\n        on pr._refs_key = ra._object_key"
		+ "\n        and ra._mgitype_key = 1"
		+ "\n        and ra._logicaldb_key = 1"
		+ "\n        and ra.preferred = 1"
		+ "\n        and ra.prefixPart = 'J:'"
		+ "\n  group by p._probe_key "
		+ "\n), "
		+ "\nprobeParent as materialized ("
		+ "\n  select p._probe_key, pp.name, pa.accid"
		+ "\n  from probes p"
		+ "\n    join prb_probe p2 on p._probe_key = p2._probe_key"
		+ "\n    left outer join prb_probe pp on pp._probe_key = p2.derivedFrom"
		+ "\n    left outer join acc_accession pa on pa._object_key = pp._probe_key"
		+ "\n    and pa._mgitype_key = 3"
		+ "\n    and pa._logicaldb_key = 1"
		+ "\n    and pa.preferred = 1"
		+ "\n)"
		+ "\nselect  "
		+ "\n  p._probe_key,  "
		+ "\n  pa.accid,  "
		+ "\n  pt.term as segmentType,  "
		+ "\n  p.name, "
		+ "\n  p.primer1sequence, "
		+ "\n  p.primer2sequence, "
		+ "\n  p.regioncovered, "
		+ "\n  pj.jnums, "
		+ "\n  pm.markers, "
		+ "\n  pm.markerids, "
		+ "\n  pal.aliases, "
		+ "\n  o.commonname as organism, "
		+ "\n  pp.name as parent, "
		+ "\n  pp.accid as parentid "
		+ "\nfrom  "
		+ "\n  prb_probe p "
		+ "\n    join probeMarkers pm on p._probe_key = pm._probe_key "
		+ "\n    join probeAliases pal on p._probe_key = pal._probe_key "
		+ "\n    join probeJnums pj on p._probe_key = pj._probe_key  "
		+ "\n    join probeParent pp on p._probe_key = pp._probe_key,  "
		+ "\n  voc_term pt,  "
		+ "\n  prb_source ps, "
		+ "\n  mgi_organism o, "
		+ "\n  acc_accession pa "
		+ "\nwhere p._source_key = ps._source_key "
		+ "\nand ps._organism_key = o._organism_key "
		+ "\nand p._segmenttype_key = pt._term_key "
		+ "\nand p._probe_key = pa._object_key "
		+ "\nand pa._mgitype_key = 3 "
		+ "\nand pa._logicaldb_key = 1 "
		+ "\nand pa.preferred = 1 "
		+ "\norder by p.name "
		;

		return cmd;
	}

	public SearchResults<SummaryProbeDomain> getProbeByMarker(String accid, int offset, int limit) {
		// return list of probe domains by marker acc id
		SearchResults<SummaryProbeDomain> results = new SearchResults<SummaryProbeDomain>();
		
		String cmd = getProbeBySQL("marker", accid, offset, limit, true);
		results.total_count = processSummaryProbeCount(cmd);

		cmd = getProbeBySQL("marker", accid, offset, limit, false);
		results.items = processSummaryProbeDomain(cmd);		
		return results;
	}

	public Response downloadProbeByMarker (String accid) {
		String cmd = getProbeBySQL("marker", accid, -1, -1, false);
		return download(cmd, getTsvFileName("getProbeByMarker", null), new ProbeFormatter());
	}
	
	// --------------------------------------------------
	// get probes by reference

	public SearchResults<SummaryProbeDomain> getProbeByRef(String accid, int offset, int limit) {
		// return list of probe domains by reference jnumid
		
		SearchResults<SummaryProbeDomain> results = new SearchResults<SummaryProbeDomain>();

		String cmd = getProbeBySQL("reference", accid, offset, limit, true);
		results.total_count = processSummaryProbeCount(cmd);

		cmd = getProbeBySQL("reference", accid, offset, limit, false);
		results.items = processSummaryProbeDomain(cmd);		
		return results;
	}
	
	public Response downloadProbeByRef (String accid) {
		String cmd = getProbeBySQL("reference", accid, -1, -1, false);
		return download(cmd, getTsvFileName("getProbeByRef", null), new ProbeFormatter());
	}
	
	// --------------------------------------------------
	// get probes by search

	public String getProbeBySearchSQL(String name, String segmentTypeKey, int offset, int limit, boolean returnCount) {

		String cmd = "select distinct p._probe_key, p.name from prb_probe p where 1=1 ";
		if (segmentTypeKey != null && !segmentTypeKey.isEmpty()) {
			cmd = cmd + "\nand p._segmenttype_key = " + segmentTypeKey;
		}
		
		if (name != null && !name.isEmpty()) {
			cmd = cmd + "\nand p.name ilike '" + name + "'";
			cmd = cmd + "\nunion" +
					"\nselect distinct p._probe_key, p.name from PRB_Probe p, PRB_Alias a, PRB_Reference r" +
					"\nwhere a.alias ilike '" + name + "'" +
					"\nand a._reference_key = r._reference_key" +
					"\nand r._probe_key = p._probe_key";
			if (segmentTypeKey != null && !segmentTypeKey.isEmpty()) {
				cmd = cmd + "\nand p._segmenttype_key = " + segmentTypeKey;
			}
		}
		
		return getProbeBySQL("search", cmd, offset, limit, returnCount);
	}

	public SearchResults<SummaryProbeDomain> getProbeBySearch(String name, String segmentTypeKey, int offset, int limit) {
		// return list of probe domains by search
		// currently supported search:  name, segementTypeKey 
		
		SearchResults<SummaryProbeDomain> results = new SearchResults<SummaryProbeDomain>();
		
		String cmd = getProbeBySearchSQL(name, segmentTypeKey, offset, limit, true);
		results.total_count = processSummaryProbeCount(cmd);

		cmd = getProbeBySearchSQL(name, segmentTypeKey, offset, limit, false);
		results.items = processSummaryProbeDomain(cmd);		

		return results;
	}	

	public Response downloadProbeBySearch (String name, String segmentTypeKey) {
		String cmd = getProbeBySearchSQL(name, segmentTypeKey, -1, -1, false);
		return download(cmd, getTsvFileName("getProbeBySearch", null), new ProbeFormatter());
	}
	
	
	// --------------------------------------------------

	public Long processSummaryProbeCount(String cmd) {
		Long total_count = null;
		log.info(cmd);
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				total_count = rs.getLong("total_count");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		return total_count;
	}

	public List<SummaryProbeDomain> processSummaryProbeDomain(String cmd) {
		// return list of probe domains by "cmd" string
		
		List<SummaryProbeDomain> results = new ArrayList<SummaryProbeDomain>();
		
		log.info(cmd);	

		// translate each probe and store in SummaryProbeDomain
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryProbeDomain domain = new SummaryProbeDomain();
				domain.setProbeKey(rs.getString("_probe_key"));
				domain.setName(rs.getString("name"));
				domain.setProbeID(rs.getString("accid"));
				domain.setSegmentType(rs.getString("segmentType"));
				domain.setPrimer1Sequence(rs.getString("primer1sequence"));
				domain.setPrimer2Sequence(rs.getString("primer2sequence"));
				domain.setRegionCovered(rs.getString("regionCovered"));				
				domain.setOrganism(rs.getString("organism"));
				domain.setMarkerID(rs.getString("markerids"));
				domain.setMarkerSymbol(rs.getString("markers"));
				domain.setAliases(rs.getString("aliases"));
				domain.setJnumIDs(rs.getString("jnums"));
				domain.setParentID(rs.getString("parentid"));
				domain.setParentName(rs.getString("parent"));
	
				results.add(domain);
				probeDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	

		return results;
	}
	
	@Transactional	
	public List<SlimProbeSummaryDomain> getChildClones(Integer probeKey) {
		// return domain with list of child clones of probe key

		List<SlimProbeSummaryDomain> results = new ArrayList<SlimProbeSummaryDomain>();
		
		String cmd = "\nselect _probe_key, name from prb_probe where derivedfrom = " + probeKey + "\norder by name";
		
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimProbeSummaryDomain slimdomain = new SlimProbeSummaryDomain();
				slimdomain = slimsummarytranslator.translate(probeDAO.get(rs.getInt("_probe_key")));
				probeDAO.clear();	
				results.add(slimdomain);
			}
			//sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return results;
	}	

	public static class ProbeFormatter implements TsvFormatter {
		public String format (ResultSet obj) {
			String[][] cols = {
				{"Probe ID",   "accid"},
				{"Name",       "name"},
				{"Type",       "segmentType"},
				{"Markers",    "markers"},
				{"Marker IDs", "markerids"},
				{"Region Covered", "regionCovered"},
				{"Primer Sequence 1", "primer1sequence"},
				{"Primer Sequence 2", "primer2sequence"},
				{"Aliases",    "aliases"},
				{"Organism",   "organism"},
				{"Parent ID",  "parentid"},
				{"Parent Name","parent"},
				{"J#s",        "jnums"}

			};
			return formatTsvHelper(obj, cols);
		}
	}
}
