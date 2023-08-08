package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDataSetDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGenotypeTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneAssocService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GenotypeService extends BaseService<GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private ProbeStrainDAO strainDAO;
	@Inject
	private TermDAO termDAO;
	
	@Inject
	private AllelePairService allelePairService;
	@Inject
	private NoteService noteService;
	@Inject
	private ImagePaneAssocService imagePaneAssocService;
	
	private GenotypeTranslator translator = new GenotypeTranslator();
	private SlimGenotypeTranslator slimtranslator = new SlimGenotypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "12";
	
	@Transactional
	public SearchResults<GenotypeDomain> create(GenotypeDomain domain, User user) {	
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		Genotype entity = new Genotype();
		String cmd;
		Query query;
		
		log.info("processGenotype/create");
		
		// default strain = Not Specified
		Integer strainKey; 
		if (domain.getStrainKey() == null || domain.getStrainKey().isEmpty()) {
			strainKey = -1;
		}
		else {
			strainKey = Integer.valueOf(domain.getStrainKey());
		}
		entity.setStrain(strainDAO.get(strainKey));	
		
		// if no conditional value is set, then default = 0 (No)
		if (domain.getIsConditional() == null || domain.getIsConditional().isEmpty()) {
			domain.setIsConditional("0");
		}
		entity.setIsConditional(Integer.valueOf(domain.getIsConditional()));			

		// if no genotype exists as value is set, then default = 'Mouse Line' (3982946)
		if (domain.getExistsAsKey() == null || domain.getExistsAsKey().isEmpty()) {
			domain.setExistsAsKey("3982946");
		}
		entity.setExistsAs(termDAO.get(Integer.valueOf(domain.getExistsAsKey())));			
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		genotypeDAO.persist(entity);

		// process all notes
		noteService.process(String.valueOf(entity.get_genotype_key()), domain.getGeneralNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_genotype_key()), domain.getPrivateCuratorialNote(), mgiTypeKey, user);

		// update combination note 1
		// combination note 2 & 3 get updated by nightly process (allelecombination)
		// using allele detail note
		// then run processAlleleCombinations to finish the job
		noteService.process(String.valueOf(entity.get_genotype_key()), domain.getAlleleDetailNote(), mgiTypeKey, user);
				
		// process Allele Pairs
		log.info("processGenotypes/allele pairs");
		allelePairService.process(String.valueOf(entity.get_genotype_key()), domain.getAllelePairs(), user);		
		
		// check duplicate genotype
		cmd = "select count(*) from GXD_checkDuplicateGenotype (" + String.valueOf(entity.get_genotype_key()) + ")";
		log.info("processGenotype/check duplicate: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();

		// process order reset
		cmd = "select count(*) from MGI_resetSequenceNum ('GXD_AllelePair'," + entity.get_genotype_key() + "," + user.get_user_key() + ")";
		log.info("processGenotype/process order reset: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();
	
		// process order allele pairs
		cmd = "select count(*) from GXD_orderAllelePairs (" + entity.get_genotype_key() + ")";
		log.info("processGenotype/process order allele pairs rows: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();	
		
		// process allele/genotype
		cmd = "select count(*) from GXD_orderGenotypesAll (" + entity.get_genotype_key() + ")";
		log.info("processGenotype/process allele/genotype rows: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();		

		// process Image Pane Associations
		log.info("processGenotypes/image pane associations");
		List<ImagePaneAssocDomain> imagePaneAssocs = new ArrayList<ImagePaneAssocDomain>();
		for (int i = 0; i < domain.getImagePaneAssocs().size(); i++) {
			ImagePaneAssocDomain r = new ImagePaneAssocDomain();
			r.setProcessStatus(domain.getImagePaneAssocs().get(i).getProcessStatus());
			r.setAssocKey(domain.getImagePaneAssocs().get(i).getAssocKey());
			r.setImagePaneKey(domain.getImagePaneAssocs().get(i).getImagePaneKey());
			r.setMgiTypeKey(domain.getImagePaneAssocs().get(i).getMgiTypeKey());
			r.setObjectKey(domain.getImagePaneAssocs().get(i).getObjectKey());
			r.setIsPrimary(domain.getImagePaneAssocs().get(i).getIsPrimary());					;
			imagePaneAssocs.add(r);
		}
		imagePaneAssocService.process(domain.getGenotypeKey(), imagePaneAssocs, user);
		
		// return entity translated to domain
		log.info("processGenotype/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<GenotypeDomain> update(GenotypeDomain domain, User user) {
		// update existing entity object from in-coming domain
		
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		Genotype entity = genotypeDAO.get(Integer.valueOf(domain.getGenotypeKey()));
		String cmd;
		Query query;
		
		log.info("processGenotype/update");
		
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainKey())));
		entity.setIsConditional(Integer.valueOf(domain.getIsConditional()));
		entity.setExistsAs(termDAO.get(Integer.valueOf(domain.getExistsAsKey())));
		
		// process all notes
		noteService.process(domain.getGenotypeKey(), domain.getGeneralNote(), mgiTypeKey, user);
		noteService.process(domain.getGenotypeKey(), domain.getPrivateCuratorialNote(), mgiTypeKey, user);

		// update combination note 1
		// combination note 2 & 3 get updated by nightly process (allelecombination)
		// using allele detail note
		// then run processAlleleCombinations to finish the job
		noteService.process(domain.getGenotypeKey(), domain.getAlleleDetailNote(), mgiTypeKey, user);

		// process Allele Pairs
		log.info("processGenotypes/allele pairs");
		allelePairService.process(domain.getGenotypeKey(), domain.getAllelePairs(), user);

		// process Image Pane Associations
		log.info("processGenotypes/image pane associations");
		List<ImagePaneAssocDomain> imagePaneAssocs = new ArrayList<ImagePaneAssocDomain>();
		for (int i = 0; i < domain.getImagePaneAssocs().size(); i++) {
			ImagePaneAssocDomain r = new ImagePaneAssocDomain();
			r.setProcessStatus(domain.getImagePaneAssocs().get(i).getProcessStatus());
			r.setAssocKey(domain.getImagePaneAssocs().get(i).getAssocKey());
			r.setImagePaneKey(domain.getImagePaneAssocs().get(i).getImagePaneKey());
			r.setMgiTypeKey(domain.getImagePaneAssocs().get(i).getMgiTypeKey());
			r.setObjectKey(domain.getImagePaneAssocs().get(i).getObjectKey());
			r.setIsPrimary(domain.getImagePaneAssocs().get(i).getIsPrimary());					;
			imagePaneAssocs.add(r);
		}
		imagePaneAssocService.process(null, imagePaneAssocs, user);
					
		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		genotypeDAO.update(entity);
		log.info("processGenotype/changes processed: " + domain.getGenotypeKey());
	
		// check duplicate genotype
		cmd = "select count(*) from GXD_checkDuplicateGenotype (" + String.valueOf(entity.get_genotype_key()) + ")";
		log.info("processGenotype/check duplicate: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();

		// process order reset
		cmd = "select count(*) from MGI_resetSequenceNum ('GXD_AllelePair'," + entity.get_genotype_key() + "," + user.get_user_key() + ")";
		log.info("processGenotype/process order reset: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();

		// commented out/don't run auto-allele pair order on modify
//		if (domain.getEditAllelePairOrder() == false) {
//			cmd = "select count(*) from GXD_orderAllelePairs (" + entity.get_genotype_key() + ")";
//			log.info("processGenotype/order allele pairs: " + cmd);
//			query = genotypeDAO.createNativeQuery(cmd);
//			query.getResultList();
//		}
		
		// process allele/genotype
		cmd = "select count(*) from GXD_orderGenotypesAll (" + entity.get_genotype_key() + ")";
		log.info("processGenotype/process allele/genotype rows: " + cmd);
		query = genotypeDAO.createNativeQuery(cmd);
		query.getResultList();
				
		// return entity translated to domain
		log.info("processGenotype/update/returning results");
		results.setItem(translator.translate(entity));		
		log.info("processGenotype/update/returned results succsssful");
		return results;			
	}

	@Transactional
	public SearchResults<GenotypeDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		Genotype entity = genotypeDAO.get(key);
		results.setItem(translator.translate(genotypeDAO.get(key)));
		genotypeDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public GenotypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GenotypeDomain domain = new GenotypeDomain();
		if (genotypeDAO.get(key) != null) {
			domain = translator.translate(genotypeDAO.get(key));
		}
		genotypeDAO.clear();
		return domain;
	}

	@Transactional
	public SearchResults<GenotypeDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		results.setItem(translator.translate(genotypeDAO.get(key)));
		genotypeDAO.clear();
		return results;
	}
	
	@Transactional	
	public SearchResults<GenotypeDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		String cmd = "select count(*) as objectCount from gxd_genotype";
		
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
	public List<SlimGenotypeDomain> search(GenotypeDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();
		
		// this query will allow 0 or more marker rows, 0 or more allele1 rows
		List<String> markerList = new ArrayList<String>();
		List<String> allele1List = new ArrayList<String>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		
		// search includes allele pair attribute
		// if search does *not* include allele pair clause, see below for "union" 
		
		// "select" for all
		String select = "(select distinct g._genotype_key, ps.strain, a0.symbol" +
				", concat(ps.strain,',',a0.symbol,',',aa0.symbol) as genotypeDisplay";
		
		// "from" if allele pair = true
		String from = "from gxd_genotype g" +
				"\nleft outer join prb_strain ps on (g._strain_key = ps._strain_key)" +		
				"\nleft outer join gxd_allelepair ap0 on (g._genotype_key = ap0._genotype_key)" +
				"\nleft outer join all_allele a0 on (ap0._allele_key_1 = a0._allele_key)" +		
				"\nleft outer join all_allele aa0 on (ap0._allele_key_2 = aa0._allele_key)";
		
		// "where" for all
		String where = "where g._genotype_key is not null";
		
		// "where" if allele pair = true
		String whereAllelePair = ""; 

		String orderBy = "order by strain, _genotype_key, symbol NULLS FIRST";			
		String value;
		String includeNotExists = "";
		
		Boolean from_allele = false;
		Boolean from_marker = false;
		Boolean from_cellline = false;
		Boolean from_image = false;
		Boolean from_alleleDetailNote = false;
		Boolean from_generalNote = false;
		Boolean from_privateCuratorialNote = false;	
		Boolean from_accession = false;
		Boolean from_cmresults = false;
		
		// if parameter exists, then add to where-clause
		// accession id
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand a.accID ilike '" + mgiid + "'";
			from_accession = true;
		}
		else {
			String cmResults[] = DateSQLQuery.queryByCreationModification("g", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
			if (cmResults.length > 0) {
				from = from + cmResults[0];
				where = where + cmResults[1];
				from_cmresults = true;
			}
			
			if (searchDomain.getGenotypeKey() != null && !searchDomain.getGenotypeKey().isEmpty()) {
				where = where + "\nand g._genotype_key = " + searchDomain.getGenotypeKey();
			}
			if (searchDomain.getStrain() != null && !searchDomain.getStrain().isEmpty()) {
				where = where + "\nand ps.strain ilike '" + searchDomain.getStrain().replace("'", "''") + "'";
			}
			if (searchDomain.getIsConditional() != null && !searchDomain.getIsConditional().isEmpty()) {
				where = where + "\nand g.isConditional = " + searchDomain.getIsConditional();
			}
			if (searchDomain.getExistsAsKey() != null && !searchDomain.getExistsAsKey().isEmpty()) {
				where = where + "\nand g._ExistsAs_key = " + searchDomain.getExistsAsKey();
			}
	
			// Allele Pair
			if (searchDomain.getAllelePairs() != null && !searchDomain.getAllelePairs().isEmpty()) {

				for (int i = 0; i < searchDomain.getAllelePairs().size(); i++) {				

					value = searchDomain.getAllelePairs().get(i).getMarkerKey();
					if (value != null && !value.isEmpty()) {
						markerList.add(value);
						from_allele = true;
						from_marker = true;
					}
					
					value = searchDomain.getAllelePairs().get(i).getAlleleKey1();
					if (value != null && !value.isEmpty()) {
						allele1List.add(value);
						from_allele = true;				
					}					
				}

				value = searchDomain.getAllelePairs().get(0).getMarkerChromosome();
				if (value != null && !value.isEmpty()) {
					value = "'" + value + "'";
					whereAllelePair = whereAllelePair + "\nand m0.chromosome ilike " + value;
					from_allele = true;
					from_marker = true;
				}	
				
				value = searchDomain.getAllelePairs().get(0).getMarkerSymbol();
				if (value != null && !value.isEmpty() && value.contains("%")) {
					value = "'" + value + "'";
					whereAllelePair = whereAllelePair + "\nand m0.symbol ilike " + value;
					from_allele = true;
					from_marker = true;
				}			
				
				value = searchDomain.getAllelePairs().get(0).getAlleleKey2();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + 
						"\nand (ap0._Allele_key_1 = " + value +
						"\nor ap0._Allele_key_2 = " + value + ")";
					from_allele = true;				
				}
			
				value = searchDomain.getAllelePairs().get(0).getAlleleSymbol1();			
				if (value != null && !value.isEmpty() && value.contains("%")) {
					whereAllelePair = whereAllelePair + 
						"\nand (a0.symbol ilike '" + value + "'" +
						"\nor aa0.symbol ilike '" + value + "')";
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getAlleleSymbol2();			
				if (value != null && !value.isEmpty() && value.contains("%")) {
					whereAllelePair = whereAllelePair + 
						"\nand (a0.symbol ilike '" + value + "'" +
						"\nor aa0.symbol ilike '" + value + "')";
					from_allele = true;				
				}
				
				// mutant cell line
				// only works if either mutant cell line 1 OR 2, not both
				
				value = searchDomain.getAllelePairs().get(0).getCellLineKey1();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap0._mutantcellline_key_1 = ac._cellline_key";
					whereAllelePair = whereAllelePair + "\nand ac._cellLine_key = " + value;
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getCellLine1();
				if (value != null && !value.isEmpty() && value.contains("%")) {
					whereAllelePair = whereAllelePair + "\nand ap0._mutantcellline_key_1 = ac._cellline_key";
					whereAllelePair = whereAllelePair + "\nand ac.cellLine ilike '" + value + "'";
					from_allele = true;
					from_cellline = true;
				}
				
				value = searchDomain.getAllelePairs().get(0).getCellLineKey2();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap0._mutantcellline_key_2 = ac._cellline_key";
					whereAllelePair = whereAllelePair + "\nand ac._cellLine_key = " + value;
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getCellLine2();
				if (value != null && !value.isEmpty() && value.contains("%")) {
					whereAllelePair = whereAllelePair + "\nand ap0._mutantcellline_key_2 = ac._cellline_key";				
					whereAllelePair = whereAllelePair + "\nand ac.cellLine ilike '" + value + "'";
					from_allele = true;
					from_cellline = true;
				}
							
				value = searchDomain.getAllelePairs().get(0).getPairStateKey();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap0._pairstate_key = " + value;
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getCompoundKey();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap0._compound_key = " + value;
					from_allele = true;				
				}
				
			}
	
			// image pane associations
			if (searchDomain.getImagePaneAssocs() != null && !searchDomain.getImagePaneAssocs().isEmpty()) {
				value = searchDomain.getImagePaneAssocs().get(0).getMgiID();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand i.mgiID ilike '" + value + "'";
					from_image = true;
				}
				value = searchDomain.getImagePaneAssocs().get(0).getPixID();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand i.pixID ilike '" + value + "'";
					from_image = true;
				}
				value = searchDomain.getImagePaneAssocs().get(0).getIsPrimary();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand i.isPrimary =" + value;
					from_image = true;
				}			
			}
			
			// union for the allele pair does *not* exist
			if (from_allele == false
			 	&& from_marker == false
			 	&& from_cellline == false
			 	&& from_accession == false
			 	&& from_image == false
			 	&& from_cmresults == false) {
				
				includeNotExists = "\nunion all" +
					"\nselect distinct g._genotype_key, ps.strain, ps.strain, ps.strain as genotypeDisplay" +
					"\nfrom gxd_genotype g, prb_strain ps" +
					"\n" + where +
					"\nand not exists (select 1 from gxd_allelepair ap where g._genotype_key = ap._genotype_key)";
			}
			
			// notes
			if (searchDomain.getAlleleDetailNote() != null && !searchDomain.getAlleleDetailNote().getNoteChunk().isEmpty()) {
				value = searchDomain.getAlleleDetailNote().getNoteChunk().replace("'",  "''");
				where = where + "\nand note1._notetype_key = 1016 and note1.note ilike '" + value + "'" ;
				from_alleleDetailNote = true;
			}
			if (searchDomain.getGeneralNote() != null && !searchDomain.getGeneralNote().getNoteChunk().isEmpty() 
					&& searchDomain.getGeneralNote().getNoteChunk().contains("%")) {
				value = searchDomain.getGeneralNote().getNoteChunk().replace("'",  "''");
				where = where + "\nand note2._notetype_key = 1027 and note2.note ilike '" + value + "'" ;
				from_generalNote = true;
			}
			if (searchDomain.getPrivateCuratorialNote() != null && !searchDomain.getPrivateCuratorialNote().getNoteChunk().isEmpty()) {
				value = searchDomain.getPrivateCuratorialNote().getNoteChunk().replace("'",  "''");
				where = where + "\nand note3._notetype_key = 1028 and note3.note ilike '" + value + "'" ;
				from_privateCuratorialNote = true;
			}
		
		}
		
		// final from/where
		
		String fromLeft = "";
		String fromMarker = "";
		for (int i = 0; i < markerList.size(); i++) {
			whereAllelePair = whereAllelePair + "\nand ap" + String.valueOf(i) + "._marker_key = " + markerList.get(i);
			
			if (i > 0) {
				fromLeft = fromLeft 
				+ "\nleft outer join gxd_allelepair ap" + String.valueOf(i)
					+ " on (g._genotype_key = ap" + String.valueOf(i) + "._genotype_key)";
			}
			
			fromMarker = fromMarker + "\n, mrk_marker m" + String.valueOf(i);
			where = where + "\nand ap" + String.valueOf(i) + "._marker_key = m"  + String.valueOf(i) + "._marker_key";
		}

		for (int i = 0; i < allele1List.size(); i++) {
			whereAllelePair = whereAllelePair 
					+ "\nand (ap" + String.valueOf(i) + "._allele_key_1 = " + allele1List.get(i)
					+ "\nor ap" + String.valueOf(i) + "._allele_key_2 = " + allele1List.get(i) + ")";
			
			if (i > 0) {
				fromLeft = fromLeft 
					+ "\nleft outer join all_allele a" + String.valueOf(i)
					+ " on (ap" + String.valueOf(i) + "._allele_key_1 = a" + String.valueOf(i) + "._allele_key)";	
			}
		}
		
		from = from + fromLeft + fromMarker;
		
		if (markerList.size() == 0 && from_marker == true) {
			from = from + ", mrk_marker m0";
			where = where + "\nand ap0._marker_key = m0._marker_key";
		}
		if (from_cellline == true) {
			from = from + ", all_cellline ac";
		}		
		if (from_alleleDetailNote == true) {
			from = from + ", mgi_note_genotype_view note1";
			where = where + "\nand g._genotype_key = note1._object_key";
		}
		if (from_image == true) {
			from = from + ", img_imagepane_assoc_view i";
			where = where + "\nand g._genotype_key = i._object_key" +
					"\nand i._mgitype_key = " + mgiTypeKey;
		}
		if (from_generalNote == true) {
			from = from + ", mgi_note_genotype_view note2";
			where = where + "\nand g._genotype_key = note2._object_key";
		}	
		if (from_privateCuratorialNote == true) {
			from = from + ", mgi_note_genotype_view note3";
			where = where + "\nand g._genotype_key = note3._object_key";
		}		
		if (from_accession == true) {
			from = from + ", gxd_genotype_acc_view a";
			where = where + "\nand g._genotype_key = a._object_key" 
					+ "\nand a._mgitype_key = " + mgiTypeKey;
		}

		cmd = "\n" + select + "\n" + from + "\n" + 
				where + whereAllelePair + includeNotExists + ")\n" + 
				orderBy;
		 
		log.info(cmd);
		
		// must match GenotypeAnotService/search()
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			Integer prevObjectKey = 0;
			Integer newObjectKey = 0;
			String newDescription = "";
			String prevDescription = "";
			String newStrain = "";
			String prevStrain = "";
			Boolean addResults = false;
			
			// concatenate description when grouped by _object_key
			// genotypeDisplay will display 1 allele pair row
			// depending on query, the allele pair row may *not* always be the first row
			
			while (rs.next()) {
				
				newObjectKey = rs.getInt("_genotype_key");
				newStrain = rs.getString("strain");
				newDescription = rs.getString("genotypeDisplay");				
				if (newDescription == null) {
					newDescription = "";					
				}
				
				// group description by _object_key
				if (prevObjectKey.equals(0)) {
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				else if (newObjectKey.equals(prevObjectKey)) {
					addResults = false;
				}
				else {
					addResults = true;
				}
				
				if (addResults) {	
					SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
					slimdomain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					slimdomain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(slimdomain);
					
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				
				// if last record, then add to result set
				if (rs.isLast() == true) {
					
					if (prevObjectKey.equals(newObjectKey)) {
						if (prevDescription == null) {
							prevDescription = prevStrain;
						}
					}
					else {
						prevObjectKey = newObjectKey;
						prevStrain = newStrain;
						prevDescription = newDescription;
					}
					
					SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
					slimdomain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					slimdomain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(slimdomain);
				}
								
			}
			sqlExecutor.cleanup();
			
			// order by description - see note above at first 'select = '
			results.sort(Comparator.comparing(SlimGenotypeDomain::getGenotypeDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return results;
	}	
	
	// For Data Sets
	
	@Transactional	
	public List<GenotypeDataSetDomain> getDataSets(Integer key) {
		// search data sets by genotype key 
		// return GenotypeDataSetDomain
		
		List<GenotypeDataSetDomain> results = new ArrayList<GenotypeDataSetDomain>();

		String cmd = "select distinct * from gxd_getgenotypesdatasets(" + key + ")";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				GenotypeDataSetDomain domain = new GenotypeDataSetDomain();
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setJnumid(rs.getString("jnumid"));									
				domain.setShort_citation(rs.getString("short_citation"));
				domain.setDataSet(rs.getString("dataSet"));	
				
				// puts null/no jnum to bottom of sort
				if (rs.getInt("jnum") == 0) {
					domain.setJnum(Integer.valueOf(999999999));
				}
				else {
					domain.setJnum(rs.getInt("jnum"));					
				}
				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//  order by jnum, data set, short citation
		Comparator<GenotypeDataSetDomain> compareByJnum = Comparator.comparingInt(GenotypeDataSetDomain::getJnum);	
		Comparator<GenotypeDataSetDomain> compareByDataSet = Comparator.comparing(GenotypeDataSetDomain::getDataSet);	
		Comparator<GenotypeDataSetDomain> compareByCitation = Comparator.comparing(GenotypeDataSetDomain::getShort_citation);	
		Comparator<GenotypeDataSetDomain> compareAll = compareByJnum.thenComparing(compareByDataSet).thenComparing(compareByCitation);
		results.sort(compareAll);
		
		return(results);
	}	

	@Transactional	
	public List<SlimGenotypeDomain> searchDataSets(Integer key) {
		// search data sets by reference key
		// return SlimGenotypeDomain
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		String cmd = "select distinct * from gxd_genotype_dataset_view where _Refs_key = " + key;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimGenotypeDomain domain = new SlimGenotypeDomain();
				domain = slimtranslator.translate(genotypeDAO.get(rs.getInt("_genotype_key")));				
				domain.setGenotypeDisplay(rs.getString("strain"));
				genotypeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	// end Data Sets
	
	@Transactional
	public List<SlimGenotypeDomain> validateGenotype(SlimGenotypeDomain searchDomain) {
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();
		
		String mgiID = searchDomain.getAccID().toUpperCase();
		if (!mgiID.contains("MGI:")) {
			mgiID = "MGI:" + mgiID;
		}
		
		String cmd = "select mgiID, _object_key, description from GXD_Genotype_Summary_View where mgiID = '" + mgiID + "'";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
				slimdomain.setAccID(rs.getString("mgiID"));
				slimdomain.setGenotypeKey(rs.getString("_object_key"));
				slimdomain.setGenotypeDisplay(rs.getString("description"));
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
	public List<GenotypeDomain> getGenotypesByAllele(String key) {
		// return list of genotype domains by allele key

		List<GenotypeDomain> results = new ArrayList<GenotypeDomain>();
		
		String cmd = "select g._genotype_key, " +
				"\ncase when exists (select 1 from gxd_expression e where g._genotype_key = e._genotype_key) then 1 else 0 end as hasExpression" + 
				"\nfrom gxd_allelegenotype g where g._allele_key = " + key + " order by g.sequencenum";
		
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				GenotypeDomain domain = new GenotypeDomain();
				domain = translator.translate(genotypeDAO.get(rs.getInt("_genotype_key")));
				domain.setHasExpression(rs.getInt("hasExpression"));
				genotypeDAO.clear();
				results.add(domain);
				genotypeDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return results;
	}

	public String getGenotypeByRefSQL (String accid, int offset, int limit, boolean returnCount) {
		// SQL for selecting genotypes by acc id
		// genotype exists in GXD_Assay/GXD_Specimen, GXD_Assay/GXD_GelLane, MP Annot (1002), DO Annot (1020)
		
		String cmd;

		if (returnCount) {
			cmd = "\nwith genotypes as (" + 
					"\nselect distinct gg._genotype_key" + 
					"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, GXD_Assay ga, GXD_Specimen gs" + 
					"\nwhere aa.jnumid = '" + accid + "'" +
					"\nand aa._Refs_key = ga._Refs_key" + 
					"\nand ga._Assay_key = gs._Assay_key" +					
					"\nand gg._Genotype_key = gs._Genotype_key" + 
					"\nunion" + 
					"\nselect distinct gg._genotype_key" + 
					"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, GXD_Assay ga, GXD_GelLane gs" + 
					"\nwhere aa.jnumid = '" + accid + "'" +
					"\nand aa._Refs_key = ga._Refs_key" + 
					"\nand ga._Assay_key = gs._Assay_key" +					
					"\nand gg._Genotype_key = gs._Genotype_key" + 
					"\nunion" +					
					"\nselect distinct gg._genotype_key" + 
					"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, VOC_Evidence e, VOC_Annot a" + 
					"\nwhere aa.jnumid = '" + accid + "'" +
					"\nand aa._Refs_key = e._Refs_key and e._Annot_key = a._Annot_key and a._AnnotType_key = 1002 and gg._Genotype_key = a._Object_key" + 
					"\nunion" + 
					"\nselect distinct gg._genotype_key" + 
					"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, VOC_Evidence e, VOC_Annot a" + 
					"\nwhere aa.jnumid = '" + accid + "'" +
					"\nand aa._Refs_key = e._Refs_key and e._Annot_key = a._Annot_key and a._AnnotType_key = 1020 and gg._Genotype_key = a._Object_key" + 
					"\n)" + 
					"select count(_genotype_key) as total_count from genotypes";
			return cmd;
		}
		
		cmd = "\nwith genotypes as (" +
				"\nselect distinct gg._genotype_key, gg.isConditional, s.strain" +
				"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, PRB_Strain s, GXD_Assay ga, GXD_Specimen gs" +
				"\nwhere aa.jnumid = '" + accid + "'" +
				"\nand aa._Refs_key = ga._Refs_key" +
				"\nand ga._Assay_key = gs._Assay_key" +									
				"\nand gg._Genotype_key = gs._Genotype_key" +
				"\nand gg._Strain_key = s._Strain_key" +
				"\nunion" +
				"\nselect distinct gg._genotype_key, gg.isConditional, s.strain" +
				"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, PRB_Strain s, GXD_Assay ga, GXD_GelLane gs" +
				"\nwhere aa.jnumid = '" + accid + "'" +
				"\nand aa._Refs_key = ga._Refs_key" +
				"\nand ga._Assay_key = gs._Assay_key" +									
				"\nand gg._Genotype_key = gs._Genotype_key" +
				"\nand gg._Strain_key = s._Strain_key" +
				"\nunion" +				
				"\nselect distinct gg._genotype_key, gg.isConditional, s.strain" +
				"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, PRB_Strain s, VOC_Evidence e, VOC_Annot a" +
				"\nwhere aa.jnumid = '" + accid + "'" +
				"\nand aa._Refs_key = e._Refs_key and e._Annot_key = a._Annot_key and a._AnnotType_key = 1002 and gg._Genotype_key = a._Object_key" +
				"\nand gg._Strain_key = s._Strain_key" +
				"\nunion" +
				"\nselect distinct gg._genotype_key, gg.isConditional, s.strain" +
				"\nfrom BIB_Citation_Cache aa, GXD_Genotype gg, PRB_Strain s, VOC_Evidence e, VOC_Annot a" +
				"\nwhere aa.jnumid = '" + accid + "'" +
				"\nand aa._Refs_key = e._Refs_key and e._Annot_key = a._Annot_key and a._AnnotType_key = 1020 and gg._Genotype_key = a._Object_key" +
				"\nand gg._Strain_key = s._Strain_key" +
				"\n)" +
				"\nselect a.accid as genotypeid, gg.isConditional, gg.strain, n.note as alleleDetailNote," +
				"\ncase when exists (select 1 from GXD_Specimen g where gg._Genotype_key = g._Genotype_key)" +
				"\n    or exists (select 1 from GXD_GelLane g where gg._Genotype_key = g._Genotype_key) then 1 else 0 end as hasAssay," +
				"\ncase when exists (select 1 from VOC_Annot a where gg._Genotype_key = a._Object_key and a._AnnotType_key = 1002) then 1 else 0 end as hasMPAnnot," +
				"\ncase when exists (select 1 from VOC_Annot a where gg._Genotype_key = a._Object_key and a._AnnotType_key = 1002) then 1 else 0 end as hasDOAnnot" +
				"\nfrom genotypes gg" +
				"\n       left outer join MGI_Note n on (" +
				"\n               gg._Genotype_key = n._Object_key" +
				"\n               and n._NoteType_key = 1016" +
				"\n               and n._MGIType_key = 12)," +
				"\nACC_Accession a" + 
				"\nwhere gg._Genotype_key = a._Object_key" + 
				"\nand a._MGIType_key = 12" + 
				"\nand a._Logicaldb_key = 1";

		cmd = addPaginationSQL(cmd, "strain, alleleDetailNote", offset, limit);

		return cmd;

	}
	
	@Transactional	
	public SearchResults<SummaryGenotypeDomain> getGenotypeByRef(String accid, int offset, int limit) {
		// return list of genotype domains by reference jnum id

		SearchResults<SummaryGenotypeDomain> results = new SearchResults<SummaryGenotypeDomain>();
		List<SummaryGenotypeDomain> summaryResults = new ArrayList<SummaryGenotypeDomain>();
		
		String cmd = getGenotypeByRefSQL(accid, offset, limit, true);
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getLong("total_count");
				results.offset = offset;
				results.limit = limit;
				genotypeDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		cmd = getGenotypeByRefSQL(accid, offset, limit, false);
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryGenotypeDomain domain = new SummaryGenotypeDomain();
				domain.setJnumid(accid);
				domain.setGenotypeid(rs.getString("genotypeid"));
				domain.setGenotypeBackground(rs.getString("strain"));
				domain.setAlleleDetailNote(rs.getString("alleleDetailNote"));;
				domain.setIsConditional(rs.getBoolean("isConditional"));
				domain.setHasAssay(rs.getBoolean("hasAssay"));
				domain.setHasMPAnnot(rs.getBoolean("hasMPAnnot"));
				domain.setHasDOAnnot(rs.getBoolean("hasDOAnnot"));
				summaryResults.add(domain);
				genotypeDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		results.items = summaryResults;
		return results;
	}		

	public Response downloadGenotypeByJnum (String accid) {
		String cmd = getGenotypeByRefSQL (accid, -1, -1, false);
		return download(cmd, getTsvFileName("getGenotypeByRef", accid), new GenotypeFormatter());
	}

	public String getGenotypeByAccIDsSQL (String accid, int offset, int limit, boolean returnCount) {
		// SQL for selecting genotypes by acc ids
		// genotype exists in GXD_Specimen, GXD_GelLane, MP Annot (1002), DO Annot (1020)
		
		String cmd;
		
		accid = accid.replaceAll("MGI",  "'MGI");
		accid = accid.replaceAll(",", "',");
		accid = accid + "'";

		if (returnCount) {
			cmd = "select count (aa._object_key) as total_count" + 
					"\nfrom ACC_Accession aa" + 
					"\nwhere aa.accid in (" + accid + ")" +
					"\nand aa._mgitype_key = 12" +
					"\nand aa._logicaldb_key = 1";			
			return cmd;
		}
		
		cmd = "\nselect a.accid as genotypeid, gg.isConditional, s.strain, ao.symbol, n.note as alleleDetailNote," +
				"\ncase when exists (select 1 from GXD_Specimen g where gg._Genotype_key = g._Genotype_key)" +
				"\n    or exists (select 1 from GXD_GelLane g where gg._Genotype_key = g._Genotype_key) then 1 else 0 end as hasAssay," +
				"\ncase when exists (select 1 from VOC_Annot a where gg._Genotype_key = a._Object_key and a._AnnotType_key = 1002) then 1 else 0 end as hasMPAnnot," +
				"\ncase when exists (select 1 from VOC_Annot a where gg._Genotype_key = a._Object_key and a._AnnotType_key = 1002) then 1 else 0 end as hasDOAnnot" +
				"\nfrom gxd_genotype gg" +
				"\n left outer join MGI_Note n on (" +
				"\n    gg._Genotype_key = n._Object_key" +
				"\n    and n._NoteType_key = 1016" +
				"\n    and n._MGIType_key = 12)" +
				"\n left outer join gxd_allelepair ap0 on (gg._genotype_key = ap0._genotype_key)" +
				"\n left outer join all_allele a0 on (ap0._allele_key_1 = a0._allele_key)" +
				"\n,ACC_Accession a, PRB_Strain s" + 
				"\nwhere a.accid in (" + accid + ")" +
				"\nand a._MGIType_key = 12" + 
				"\nand a._Logicaldb_key = 1" +
				"\nand a._Object_key = gg._Genotype_key" +
				"\nand gg._Strain_key = s._Strain_key";

		cmd = addPaginationSQL(cmd, "strain, _genotype_key, symbol NULLS FIRST", offset, limit);

		return cmd;

	}
	
	@Transactional	
	public SearchResults<SummaryGenotypeDomain> getGenotypeByAccIDs(String accid, int offset, int limit) {
		// return list of genotype domains by reference acc ids

		SearchResults<SummaryGenotypeDomain> results = new SearchResults<SummaryGenotypeDomain>();
		List<SummaryGenotypeDomain> summaryResults = new ArrayList<SummaryGenotypeDomain>();
		
		String cmd = getGenotypeByAccIDsSQL(accid, offset, limit, true);
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getLong("total_count");
				results.offset = offset;
				results.limit = limit;
				genotypeDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		cmd = getGenotypeByAccIDsSQL(accid, offset, limit, false);
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryGenotypeDomain domain = new SummaryGenotypeDomain();
				domain.setAccids(accid);
				domain.setGenotypeid(rs.getString("genotypeid"));
				domain.setGenotypeBackground(rs.getString("strain"));
				domain.setAlleleDetailNote(rs.getString("alleleDetailNote"));;
				domain.setIsConditional(rs.getBoolean("isConditional"));
				domain.setHasAssay(rs.getBoolean("hasAssay"));
				domain.setHasMPAnnot(rs.getBoolean("hasMPAnnot"));
				domain.setHasDOAnnot(rs.getBoolean("hasDOAnnot"));
				summaryResults.add(domain);
				genotypeDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		results.items = summaryResults;
		return results;
	}		

	public Response downloadGenotypeByAccIDs (String accid) {
		String cmd = getGenotypeByAccIDsSQL (accid, -1, -1, false);
		accid = accid.replaceAll("MGI",  "'MGI");
		accid = accid.replaceAll(",", "',");
		accid = accid + "'";		
		return download(cmd, getTsvFileName("getGenotypeByAccIDs", accid), new GenotypeFormatter());
	}
	
	public String getGenotypeByClipboardSQL (String userid, int offset, int limit, boolean returnCount) {
		// SQL for selecting genotypes by clipboard/user
		// genotype exists in GXD_Assay/GXD_Specimen, GXD_Assay/GXD_GelLane, MP Annot (1002), DO Annot (1020)
		
		String cmd;

		if (returnCount) {
			cmd = "\nwith genotypes as (" + 
					"\nselect distinct gg._genotype_key, gg.isConditional, s.strain" +
					"\nfrom MGI_SetMember ss, MGI_User u, GXD_Genotype gg, PRB_Strain s" + 
					"\nwhere u.login = '" + userid + "'" +
					"\nand u._user_key = ss._CreatedBy_key" +					
					"\nand ss._set_key = 1055" + 
					"\nand ss._object_key = gg._genotype_key" +
					"\nand gg._strain_key = s._strain_key" +
					"\n)" + 
					"select count(_genotype_key) as total_count from genotypes";
			return cmd;
		}
		
		cmd = "\nwith genotypes as (" +
				"\nselect distinct gg._genotype_key, gg.isConditional, ss.label, s.strain" +
				"\nfrom MGI_SetMember ss, MGI_User u, GXD_Genotype gg, PRB_Strain s" + 
				"\nwhere u.login = '" + userid + "'" +
				"\nand u._user_key = ss._CreatedBy_key" +					
				"\nand ss._set_key = 1055" + 
				"\nand ss._object_key = gg._genotype_key" +
				"\nand gg._strain_key = s._strain_key" +
				"\n)" +
				"\nselect a.accid as genotypeid, gg.isConditional, gg.strain, n.note as alleleDetailNote," +
				"\ncase when exists (select 1 from GXD_Specimen g where gg._Genotype_key = g._Genotype_key)" +
				"\n    or exists (select 1 from GXD_GelLane g where gg._Genotype_key = g._Genotype_key) then 1 else 0 end as hasAssay," +
				"\ncase when exists (select 1 from VOC_Annot a where gg._Genotype_key = a._Object_key and a._AnnotType_key = 1002) then 1 else 0 end as hasMPAnnot," +
				"\ncase when exists (select 1 from VOC_Annot a where gg._Genotype_key = a._Object_key and a._AnnotType_key = 1002) then 1 else 0 end as hasDOAnnot" +
				"\nfrom genotypes gg" +
				"\n       left outer join MGI_Note n on (" +
				"\n               gg._Genotype_key = n._Object_key" +
				"\n               and n._NoteType_key = 1016" +
				"\n               and n._MGIType_key = 12)," +
				"\nACC_Accession a" + 
				"\nwhere gg._Genotype_key = a._Object_key" + 
				"\nand a._MGIType_key = 12" + 
				"\nand a._Logicaldb_key = 1";


		cmd = addPaginationSQL(cmd, "gg.label", offset, limit);

		return cmd;
	}
	
	@Transactional	
	public SearchResults<SummaryGenotypeDomain> getGenotypeByClipboard(String userid, int offset, int limit) {
		// return list of genotype domains by clipboard/user

		SearchResults<SummaryGenotypeDomain> results = new SearchResults<SummaryGenotypeDomain>();
		List<SummaryGenotypeDomain> summaryResults = new ArrayList<SummaryGenotypeDomain>();
		
		String cmd = getGenotypeByClipboardSQL(userid, offset, limit, true);
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getLong("total_count");
				results.offset = offset;
				results.limit = limit;
				genotypeDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		cmd = getGenotypeByClipboardSQL(userid, offset, limit, false);
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryGenotypeDomain domain = new SummaryGenotypeDomain();
				domain.setCreatedBy(userid);
				domain.setGenotypeid(rs.getString("genotypeid"));
				domain.setGenotypeBackground(rs.getString("strain"));
				domain.setAlleleDetailNote(rs.getString("alleleDetailNote"));;
				domain.setIsConditional(rs.getBoolean("isConditional"));
				domain.setHasAssay(rs.getBoolean("hasAssay"));
				domain.setHasMPAnnot(rs.getBoolean("hasMPAnnot"));
				domain.setHasDOAnnot(rs.getBoolean("hasDOAnnot"));
				summaryResults.add(domain);
				genotypeDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		results.items = summaryResults;
		return results;
	}		

	public Response downloadGenotypeByClipboard (String userid) {
		String cmd = getGenotypeByClipboardSQL (userid, -1, -1, false);
		return download(cmd, getTsvFileName("getGenotypeByClipboard", userid), new GenotypeFormatter());
	}
	
	public static class GenotypeFormatter implements TsvFormatter {
		public String format (ResultSet obj) {
			String[][] cols = {
                	{"Genotype ID", "genotypeid"},
                	{"Allele Pair(s)", "alleleDetailNote"},
                	{"Genetic Background/Strain", "strain"},
                	{"GXD Assays", "hasAssay"},
                	{"MP Annot", "hasMPAnnot"},
                	{"DO Annot", "hasDOAnnot"}
			};
			return formatTsvHelper(obj, cols);
		}
	}	
}
