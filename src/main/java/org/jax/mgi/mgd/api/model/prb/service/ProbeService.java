package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
import org.jax.mgi.mgd.api.model.prb.entities.Probe;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

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
	private AccessionTranslator acctranslator = new AccessionTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "3";
	
	@Transactional
	public SearchResults<ProbeDomain> create(ProbeDomain domain, User user) {	
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		Probe entity = new Probe();
		
		log.info("processProbe/create");		
		
		// default = Not Specified
		if (domain.getSegmentTypeKey() == null || domain.getSegmentTypeKey().isEmpty()) {
			domain.setSegmentTypeKey("63474");
		}
		
		// default = Not Specified
		if (domain.getVectorTypeKey() == null || domain.getVectorTypeKey().isEmpty()) {
			domain.setVectorTypeKey("316370");
		}
		
		// primer
		if (domain.getSegmentTypeKey().equals("63473")) {		
	
			entity.setInsertSite(null);
			entity.setInsertSize(null);
			entity.setDerivedFrom(null);
			
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
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct p._probe_key, p.name";
		String from = "from prb_probe p";
		String where = "where p._probe_key is not null";
		String orderBy = "order by p.name";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		Boolean from_accession = false;
		Boolean from_raccession = false;
		Boolean from_parentclone = false;
		Boolean from_source = false;
		Boolean from_strain = false;
		Boolean from_tissue = false;
		Boolean from_cellline = false;
		Boolean from_marker = false;
		Boolean from_reference = false;
		Boolean from_alias = false;
		Boolean from_generalNote = false;
		Boolean from_rawsequenceNote = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("p", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getSegmentTypeKey() != null && !searchDomain.getSegmentTypeKey().isEmpty()) {
			where = where + "\nand p._segmenttype_key = " + searchDomain.getSegmentTypeKey();
		}

		if (searchDomain.getVectorTypeKey() != null && !searchDomain.getVectorTypeKey().isEmpty()) {
			where = where + "\nand p._vector_key = " + searchDomain.getVectorTypeKey();
		}
		
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand p.name ilike '" + searchDomain.getName() + "'" ;
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
			where = where + "\nand pc._mgitype_key = 3 and p.derivedfrom = pc._object_key"; 
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
		
		String cmd = "select accID, _object_key, description from PRB_Acc_View"
					+ "\nwhere accID = '" + searchDomain.getAccID().toUpperCase() + "'";
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
	
}
