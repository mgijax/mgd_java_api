package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.StrainDataSetDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeStrainService extends BaseService<ProbeStrainDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeStrainDAO probeStrainDAO;
	@Inject
	private TermDAO termDAO;
	
	private ProbeStrainTranslator translator = new ProbeStrainTranslator();
	private SlimProbeStrainTranslator slimtranslator = new SlimProbeStrainTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeStrainDomain> create(ProbeStrainDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		ProbeStrain entity = new ProbeStrain();
		
		log.info("processStrain/create");
		
		entity.setSpecies(termDAO.get(Integer.valueOf(domain.getSpeciesKey())));			
		entity.setStrainType(termDAO.get(Integer.valueOf(domain.getStrainTypeKey())));
		entity.setStrain(domain.getStrain());
		entity.setStandard(Integer.valueOf(domain.getStandard()));
		entity.setIsPrivate(Integer.valueOf(domain.getIsPrivate()));
		entity.setGeneticBackground(Integer.valueOf(domain.getGeneticBackground()));
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		probeStrainDAO.persist(entity);
		
		// return entity translated to domain
		log.info("processStrain/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<ProbeStrainDomain> update(ProbeStrainDomain object, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeStrainDomain> delete(Integer key, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeStrainDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeStrainDomain domain = new ProbeStrainDomain();
		if (probeStrainDAO.get(key) != null) {
			domain = translator.translate(probeStrainDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeStrainDomain> getResults(Integer key) {
        SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
        results.setItem(translator.translate(probeStrainDAO.get(key)));
        return results;
    }

	@Transactional	
	public SearchResults<ProbeStrainDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		String cmd = "select count(*) as objectCount from prb_strain";
		
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
	public List<SlimProbeStrainDomain> search(ProbeStrainDomain searchDomain) {

		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct p._strain_key, p.strain";
		String from = "from prb_strain p";
		String where = "where p._strain_key is not null";
		String orderBy = "order by p.strain";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		Boolean from_accession = false;
		//Boolean from_raccession = false;
		Boolean from_attribute = false;
		Boolean from_needsreview = false;
		Boolean from_marker = false;
		Boolean from_genotype = false;
		Boolean from_reference = false;
		Boolean from_strainNote = false;
		Boolean from_impcNote = false;
		Boolean from_nomenNote = false;
		Boolean from_mclNote = false;

		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("p", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getStrain() != null && !searchDomain.getStrain().isEmpty()) {
			where = where + "\nand p.strain ilike '" + searchDomain.getStrain() + "'";
		}
		
		if (searchDomain.getSpeciesKey() != null && !searchDomain.getSpeciesKey().isEmpty()) {
			where = where + "\nand p._species_key = " + searchDomain.getSpeciesKey();
		}
		
		if (searchDomain.getStrainTypeKey() != null && !searchDomain.getStrainTypeKey().isEmpty()) {
			where = where + "\nand p._straintype_key = " + searchDomain.getStrainTypeKey();
		}

		if (searchDomain.getIsPrivate() != null && !searchDomain.getIsPrivate().isEmpty()) {
			where = where + "\nand p.private = " + searchDomain.getIsPrivate();
		}

		if (searchDomain.getStandard() != null && !searchDomain.getStandard().isEmpty()) {
			where = where + "\nand p.standard = " + searchDomain.getStandard();
		}
		
		if (searchDomain.getGeneticBackground() != null && !searchDomain.getGeneticBackground().isEmpty()) {
			where = where + "\nand p.geneticBackground = " + searchDomain.getGeneticBackground();
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

		if (searchDomain.getAttributes() != null) {		
			AnnotationDomain annotDomain = searchDomain.getAttributes().get(0);		
			value = annotDomain.getTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand va1._term_key = " + value;
				from_attribute = true;
			}
		}

		if (searchDomain.getNeedsReview() != null) {		
			AnnotationDomain annotDomain = searchDomain.getNeedsReview().get(0);		
			value = annotDomain.getTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand va2._term_key = " + value;
				from_needsreview = true;
			}
		}
		
		// markers
		if (searchDomain.getMarkers() != null) {

			value = searchDomain.getMarkers().get(0).getQualifierKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand m._qualifier_key = " + value;				
				from_marker = true;
			}
			
			value = searchDomain.getMarkers().get(0).getMarkerKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand m._marker_key = " + value;				
				from_marker = true;
			}

			value = searchDomain.getMarkers().get(0).getAlleleKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand m._allele_key = " + value;				
				from_marker = true;
			}
			
			value = searchDomain.getMarkers().get(0).getChromosome();
			if (value != null && !value.isEmpty()) {
				value = "'" + value + "'";
				where = where + "\nand m.chromosome ilike " + value;
				from_marker = true;
			}	
			
			value = searchDomain.getMarkers().get(0).getMarkerSymbol();
			if (value != null && !value.isEmpty() && value.contains("%")) {
				value = "'" + value + "'";
				where = where + "\nand m.symbol ilike " + value;
				from_marker = true;
			}
	
			value = searchDomain.getMarkers().get(0).getAlleleSymbol();
			if (value != null && !value.isEmpty() && value.contains("%")) {
				value = "'" + value + "'";
				where = where + "\nand m.alleleSymbol ilike " + value;
				from_marker = true;
			}
			
		}

		// genotypes
		if (searchDomain.getGenotypes() != null) {

			value = searchDomain.getGenotypes().get(0).getQualifierKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand g._qualifier_key = " + value;				
				from_genotype = true;
			}
			
			value = searchDomain.getGenotypes().get(0).getGenotypeKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand g._genotype_key = " + value;				
				from_genotype = true;
			}

			value = searchDomain.getGenotypes().get(0).getGenotypeDisplay();
			if (value != null && !value.isEmpty() && value.contains("%")) {
				value = "'" + value + "'";
				where = where + "\nand g.description ilike " + value;
				from_genotype = true;
			}
			
			String gcmResults[] = DateSQLQuery.queryByCreationModification("g", searchDomain.getGenotypes().get(0).getCreatedBy(), searchDomain.getGenotypes().get(0).getModifiedBy(), searchDomain.getGenotypes().get(0).getCreation_date(), searchDomain.getGenotypes().get(0).getModification_date());
			if (gcmResults.length > 0) {
				from = from + gcmResults[0];
				where = where + gcmResults[1];
				if (!gcmResults[0].isEmpty() || !gcmResults[1].isEmpty()) {			
					from_genotype = true;
				}
			}			
		}
		
		// references
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand ref._Refs_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replace("'",  "''");
				where = where + "\nand ref.short_citation ilike '" + value + "'";
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getJnumid() != null && !searchDomain.getRefAssocs().get(0).getJnumid().isEmpty()) {
				where = where + "\nand ref.jnumid ilike '" + searchDomain.getRefAssocs().get(0).getJnumid() + "'";
				from_reference = true;				
			}
			String rcmResults[] = DateSQLQuery.queryByCreationModification("ref", searchDomain.getRefAssocs().get(0).getCreatedBy(), searchDomain.getRefAssocs().get(0).getModifiedBy(), searchDomain.getRefAssocs().get(0).getCreation_date(), searchDomain.getRefAssocs().get(0).getModification_date());
			if (rcmResults.length > 0) {
				from = from + rcmResults[0];
				where = where + rcmResults[1];
				if (!rcmResults[0].isEmpty() || !rcmResults[1].isEmpty()) {			
					from_reference = true;
				}
			}			
		}

		if (searchDomain.getStrainOriginNote() != null && !searchDomain.getStrainOriginNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getStrainOriginNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note1.note ilike '" + value + "'" ;
			from_strainNote = true;
		}
		
		if (searchDomain.getImpcNote() != null && !searchDomain.getImpcNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getImpcNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note2.note ilike '" + value + "'" ;
			from_impcNote = true;
		}
	
		if (searchDomain.getNomenNote() != null && !searchDomain.getNomenNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getNomenNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note3.note ilike '" + value + "'" ;
			from_nomenNote = true;
		}
		
		if (searchDomain.getMclNote() != null && !searchDomain.getMclNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getMclNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note4.note ilike '" + value + "'" ;
			from_mclNote = true;
		}
			
//		if (searchDomain.getRawsequenceNote() != null && !searchDomain.getRawsequenceNote().getNoteChunk().isEmpty()) {
//			value = searchDomain.getRawsequenceNote().getNoteChunk().replace("'",  "''");
//			where = where + "\nand note2.note ilike '" + value + "'" ;
//			from_rawsequenceNote = true;
//		}
		
		// building from...
		
		if (from_accession == true) {
			from = from + ", acc_accession acc";
			where = where + "\nand acc._mgitype_key = 10 and p._probe_key = acc._object_key and acc.prefixPart = 'MGI:'";
		}
		
		if (from_marker == true) {
			from = from + ", prb_strain_marker_view m";
			where = where + "\nand p._strain_key = m._strain_key";
		}
		
		if (from_genotype == true) {
			from = from + ", prb_strain_genotype_view g";
			where = where + "\nand p._strain_key = g._strain_key";
		}
		
		if (from_reference == true) {
			from = from + ", mgi_reference_strain_view ref";
			where = where + "\nand p._strain_key = ref._object_key";
		}

		if (from_strainNote == true) {
			from = from + ", mgi_note_strain_view note1";
			where = where + "\nand p._strain_key = note1._object_key";
			where = where + "\nand note1._notetype_key = 1011";
		}
		
		if (from_impcNote == true) {
			from = from + ", mgi_note_strain_view note2";
			where = where + "\nand p._strain_key = note2._object_key";
			where = where + "\nand note2._notetype_key = 1012";
		}
		
		if (from_nomenNote == true) {
			from = from + ", mgi_note_strain_view note3";
			where = where + "\nand p._strain_key = note3._object_key";
			where = where + "\nand note3._notetype_key = 1013";
		}
		
		if (from_mclNote == true) {
			from = from + ", mgi_note_strain_view note4";
			where = where + "\nand p._strain_key = note4._object_key";
			where = where + "\nand note4._notetype_key = 1038";
		}
		
//		if (from_raccession == true) {
//			from = from + ", acc_accession racc, acc_accessionreference rracc";
//			where = where + "\nand p._probe_key = racc._object_key"
//					+ "\nand r._refs_key = rracc._refs_key"
//					+ "\nand racc._mgitype_key = 10"
//					+ "\nand rracc._accession_key = racc._accession_key";
//		}

		if (from_attribute == true) {
			from = from + ", voc_annot va1";
			where = where + "\nand va1._annottype_key = " + searchDomain.getAttributes().get(0).getAnnotTypeKey()
					+ "\nand p._strain_key = va1._object_key";
		}

		if (from_needsreview == true) {
			from = from + ", voc_annot va2";
			where = where + "\nand va2._annottype_key = " + searchDomain.getNeedsReview().get(0).getAnnotTypeKey()
						+ "\nand p._strain_key = va2._object_key";
		}
		
//		
//		if (from_rawsequenceNote == true) {
//			from = from + ", mgi_note_probe_view note2";
//			where = where + "\nand p._probe_key = note2._object_key";
//			where = where + "\nand note2._notetype_key = 1037";
//		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimProbeStrainDomain domain = new SlimProbeStrainDomain();
				domain = slimtranslator.translate(probeStrainDAO.get(rs.getInt("_strain_key")));				
				probeStrainDAO.clear();
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
	public List<SlimProbeStrainDomain> validateStrain(SlimProbeStrainDomain searchDomain) {
		// validate the Strain by strain symbol
   
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		String cmd = "";
		String select = "select s._strain_key";
		String from = "from prb_strain s";
		String where = "where s._Strain_key is not null";
		
		Boolean from_accession = false;
		
		if (searchDomain.getStrain() != null && !searchDomain.getStrain().isEmpty()) {
			where = where + "\nand s.strain ilike '" + searchDomain.getStrain() + "'" ;
		}
		
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(acc.accID) = '" + mgiid.toLowerCase() + "'";	
			from_accession = true;
		}

		if (from_accession == true) {
			from = from + ", prb_strain_acc_view acc";
			where = where + "\nand s._strain_key = acc._object_key" 
				+ "\nand acc._mgitype_key = 10"
				+ "\nand acc._logicaldb_key = 1"
				+ "\nand acc.preferred = 1";		
		}
		
		cmd = "\n" + select + "\n" + from + "\n" + where;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimProbeStrainDomain slimdomain = new SlimProbeStrainDomain();
				slimdomain = slimtranslator.translate(probeStrainDAO.get(rs.getInt("_strain_key")));				
				probeStrainDAO.clear();
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
	public SearchResults<String> getStrainList() {
		// generate SQL command to return a list of distinct strains
		
		List<String> results = new ArrayList<String>();

		// building SQL command : select + from + where + orderBy
		String cmd = "select distinct strain from PRB_Strain";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.add(rs.getString("strain"));
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(results);
		return new SearchResults<String>(results);
	}	

	@Transactional	
	public SearchResults<String> getStrainListProbeAntigen() {
		// generate SQL command to return a list of distinct strains probe/antigen
		
		List<String> results = new ArrayList<String>();

		// building SQL command : select + from + where + orderBy
		String cmd = "(select distinct s.strain" + 
				"\nfrom prb_strain s, prb_source ps, prb_probe p" + 
				"\nwhere p._source_key = ps._Source_key" + 
				"\nand ps._strain_key = s._strain_key" + 
				"\nunion" + 
				"\nselect distinct s.strain" + 
				"\nfrom prb_strain s, prb_source ps, gxd_antigen p" + 
				"\nwhere p._source_key = ps._Source_key" + 
				"\nand ps._strain_key = s._strain_key" + 
				"\n)" + 
				"\norder by strain";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.add(rs.getString("strain"));
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(results);
		return new SearchResults<String>(results);
	}	
	
	// For Data Sets
	
	@Transactional	
	public List<StrainDataSetDomain> getDataSetsAcc(Integer key) {
		// search data sets by strain key 
		// return StrainDataSetDomain
		// using PRB_getStrainDataSets()
		
		List<StrainDataSetDomain> results = new ArrayList<StrainDataSetDomain>();

		String cmd = "select distinct * from PRB_getStrainDataSets(" + key + ")";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				StrainDataSetDomain domain = new StrainDataSetDomain();
				domain.setAccid(rs.getString("accid"));													
				domain.setDataSet(rs.getString("dataSet"));					
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Comparator<StrainDataSetDomain> compareByDataSet = Comparator.comparing(StrainDataSetDomain::getDataSet);	
		Comparator<StrainDataSetDomain> compareByAccID = Comparator.comparing(StrainDataSetDomain::getAccid);	
		Comparator<StrainDataSetDomain> compareAll = compareByDataSet.thenComparing(compareByAccID);
		results.sort(compareAll);
		
		return results;
	}	

	@Transactional	
	public List<StrainDataSetDomain> getDataSetsRef(Integer key) {
		// search data sets by strain key 
		// return StrainDataSetDomain
		// using PRB_getStrainReferences
		
		List<StrainDataSetDomain> results = new ArrayList<StrainDataSetDomain>();

		String cmd = "select distinct * from PRB_getStrainReferences(" + key + ")";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				StrainDataSetDomain domain = new StrainDataSetDomain();
				domain.setJnum(rs.getInt("jnum"));													
				domain.setDataSet(rs.getString("dataSet"));				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Comparator<StrainDataSetDomain> compareByJnum = Comparator.comparingInt(StrainDataSetDomain::getJnum);	
		Comparator<StrainDataSetDomain> compareByDataSet = Comparator.comparing(StrainDataSetDomain::getDataSet);	
		Comparator<StrainDataSetDomain> compareAll = compareByJnum.thenComparing(compareByDataSet);
		results.sort(compareAll);
		
		return results;
	}

	@Transactional	
	public List<SlimProbeStrainDomain> getByRef(Integer key) {
		// search strains by ref key 
		// using PRB_getStrainByReference
		
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		String cmd = "select distinct * from PRB_getStrainByReference(" + key + ")";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimProbeStrainDomain domain = new SlimProbeStrainDomain();
				domain = slimtranslator.translate(probeStrainDAO.get(rs.getInt("_strain_key")));				
				probeStrainDAO.clear();
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
