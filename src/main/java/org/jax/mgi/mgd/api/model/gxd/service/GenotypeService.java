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

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDataSetDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGenotypeTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneAssocService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
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
		noteService.process(domain.getGenotypeKey(), domain.getGeneralNote(), mgiTypeKey, "1027", user);
		noteService.process(domain.getGenotypeKey(), domain.getPrivateCuratorialNote(), mgiTypeKey, "1028", user);
		// update combination note 1
		// combination note 2 & 3 get updated by nightly process (allelecombination)
		// using allele detail note
		// then run processAlleleCombinations to finish the job
		noteService.process(String.valueOf(entity.get_genotype_key()), domain.getAlleleDetailNote(), mgiTypeKey, "1016", user);
				
		// process Allele Pairs
		log.info("processGenotypes/allele pairs");
		allelePairService.process(String.valueOf(entity.get_genotype_key()), domain.getAllelePairs(), user);		
		
		if (domain.getUseAllelePairDefaultOrder() == "1") {
			cmd = "select count(*) from GXD_orderAllelePairs (" + String.valueOf(entity.get_genotype_key()) + ")";
			log.info("processGenotype/order allele pairs: " + cmd);
			query = genotypeDAO.createNativeQuery(cmd);
			query.getResultList();
		}
		
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
		Boolean modified = false;
		String cmd;
		Query query;
		
		log.info("processGenotype/update");
		
		if (!String.valueOf(entity.getStrain().get_strain_key()).equals(domain.getStrainKey())) {
			entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainKey())));
			modified = true;
		}
		
		if (!String.valueOf(entity.getIsConditional()).equals(domain.getIsConditional())) {
			entity.setIsConditional(Integer.valueOf(domain.getIsConditional()));
			modified = true;
		}
		
		if (!String.valueOf(entity.getExistsAs().get_term_key()).equals(domain.getExistsAsKey())) {
			entity.setExistsAs(termDAO.get(Integer.valueOf(domain.getExistsAsKey())));
			modified = true;
		}
		
		// process all notes
		if (noteService.process(domain.getGenotypeKey(), domain.getGeneralNote(), mgiTypeKey, "1027", user)) {
			modified = true;
		}
		if (noteService.process(domain.getGenotypeKey(), domain.getPrivateCuratorialNote(), mgiTypeKey, "1028", user)) {
			modified = true;
		}
		// update combination note 1
		// combination note 2 & 3 get updated by nightly process (allelecombination)
		// using allele detail note
		// then run processAlleleCombinations to finish the job
		if (noteService.process(domain.getGenotypeKey(), domain.getAlleleDetailNote(), mgiTypeKey, "1016", user)) {
			modified = true;
		}

		// process Allele Pairs
		log.info("processGenotypes/allele pairs");
		if (allelePairService.process(domain.getGenotypeKey(), domain.getAllelePairs(), user)) {
			modified = true;			
		}

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
		if (imagePaneAssocService.process(null, imagePaneAssocs, user)) {
			modified = true;			
		}
					
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			genotypeDAO.update(entity);
			log.info("processGenotype/changes processed: " + domain.getGenotypeKey());
		}
		else {
			log.info("processGenotype/no changes processed: " + domain.getGenotypeKey());
		}

		// order allele pairs
		if (domain.getUseAllelePairDefaultOrder() == "1") {
			cmd = "select count(*) from GXD_orderAllelePairs (" + String.valueOf(entity.get_genotype_key()) + ")";
			log.info("processGenotype/order allele pairs: " + cmd);
			query = genotypeDAO.createNativeQuery(cmd);
			query.getResultList();
		}
		
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

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		
		// search includes allele pair attribute
		// if search does *not* include allele pair clause, see below for "union" 
		
		// "select" for all
		String select = "(select distinct g._genotype_key, ps.strain, a1.symbol" +
				", concat(ps.strain,',',a1.symbol,',',a2.symbol) as genotypeDisplay";
		
		// "from" if allele pair = true
		String from = "from gxd_genotype g" +
				"\nleft outer join gxd_allelepair ap on (g._genotype_key = ap._genotype_key)" +
				"\nleft outer join all_allele a1 on (ap._allele_key_1 = a1._allele_key)" +		
				"\nleft outer join all_allele a2 on (ap._allele_key_2 = a2._allele_key)," +
				"\nprb_strain ps";
		
		// "where" for all
		String where = "where g._strain_key = ps._strain_key";
		
		// "where" if allele pair = true
		String whereAllelePair = ""; 

		String orderBy = "order by strain, symbol NULLS FIRST";			
		String limit = Constants.SEARCH_RETURN_LIMIT5000;
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
				where = where + "\nand ps.strain ilike '" + searchDomain.getStrain() + "'";
			}
			if (searchDomain.getIsConditional() != null && !searchDomain.getIsConditional().isEmpty()) {
				where = where + "\nand g.isConditional = " + searchDomain.getIsConditional();
			}
			if (searchDomain.getExistsAsKey() != null && !searchDomain.getExistsAsKey().isEmpty()) {
				where = where + "\nand g._ExistsAs_key = " + searchDomain.getExistsAsKey();
			}
	
			// Allele Pair
			if (searchDomain.getAllelePairs() != null && !searchDomain.getAllelePairs().isEmpty()) {
	
				value = searchDomain.getAllelePairs().get(0).getMarkerKey();
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._Marker_key = " + value;
					from_allele = true;
					from_marker = true;
				}
				
				value = searchDomain.getAllelePairs().get(0).getMarkerSymbol();
				if (value != null && !value.isEmpty()) {
					value = "'" + value + "'";
					whereAllelePair = whereAllelePair + "\nand m.symbol ilike " + value;
					from_allele = true;
					from_marker = true;
				}
						
				value = searchDomain.getAllelePairs().get(0).getAlleleKey1();
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + 
						"\nand (ap._Allele_key_1 = " + value +
						"\nor ap._Allele_key_2 = " + value + ")";
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getAlleleKey2();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + 
						"\nand (ap._Allele_key_1 = " + value +
						"\nor ap._Allele_key_2 = " + value + ")";
					from_allele = true;				
				}
			
				value = searchDomain.getAllelePairs().get(0).getAlleleSymbol1();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + 
						"\nand (a1.symbol ilike '" + value + "'" +
						"\nor a2.symbol ilike '" + value + "')";
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getAlleleSymbol2();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + 
						"\nand (a1.symbol ilike '" + value + "'" +
						"\nor a2.symbol ilike '" + value + "')";
					from_allele = true;				
				}
				
				// mutant cell line
				// only works if either mutant cell line 1 OR 2, not both
				
				value = searchDomain.getAllelePairs().get(0).getCellLineKey1();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._mutantcellline_key_1 = ac._cellline_key";
					whereAllelePair = whereAllelePair + "\nand ac._cellLine_key = " + value;
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getCellLine1();
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._mutantcellline_key_1 = ac._cellline_key";
					whereAllelePair = whereAllelePair + "\nand ac.cellLine ilike '" + value + "'";
					from_allele = true;
					from_cellline = true;
				}
				
				value = searchDomain.getAllelePairs().get(0).getCellLineKey2();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._mutantcellline_key_2 = ac._cellline_key";
					whereAllelePair = whereAllelePair + "\nand ac._cellLine_key = " + value;
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getCellLine2();
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._mutantcellline_key_2 = ac._cellline_key";				
					whereAllelePair = whereAllelePair + "\nand ac.cellLine ilike '" + value + "'";
					from_allele = true;
					from_cellline = true;
				}
							
				value = searchDomain.getAllelePairs().get(0).getPairStateKey();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._pairstate_key = " + value;
					from_allele = true;				
				}
				
				value = searchDomain.getAllelePairs().get(0).getCompoundKey();			
				if (value != null && !value.isEmpty()) {
					whereAllelePair = whereAllelePair + "\nand ap._compound_key = " + value;
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
		if (from_marker == true) {
			from = from + ", mrk_marker m";
			where = where + "\nand ap._marker_key = m._marker_key";
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
				orderBy + "\n" + limit;
		 
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			Integer prevObjectKey = 0;
			Integer newObjectKey = 0;
			String newDescription = "";
			String prevDescription = "";
			String newStrain = "";
			String prevStrain = "";
			Boolean addResults = false;
			
			// concatenate description when grouped by _genotype_key
			
			while (rs.next()) {
				
				newObjectKey = rs.getInt("_genotype_key");
				newStrain = rs.getString("strain");
				newDescription = rs.getString("symbol");
								
				// group description by _object_key
				if (prevObjectKey.equals(0)) {
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				else if (newObjectKey.equals(prevObjectKey)) {
					prevDescription = prevDescription + "," + newDescription;
					addResults = false;
				}
				else {
					addResults = true;
				}
				
				if (addResults) {

					if (prevDescription != null) {
						prevDescription = prevStrain + " " + prevDescription;
					}
					else {
						prevDescription = prevStrain;
					}
					
					SlimGenotypeDomain domain = new SlimGenotypeDomain();
					domain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					domain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(domain);

					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				
				// if last record, then add to result set
				if (rs.isLast() == true) {
					
					if (prevObjectKey.equals(newObjectKey)) {
						prevDescription = prevStrain + " " + prevDescription;
					}
					else {
						prevObjectKey = newObjectKey;
						prevStrain = newStrain;
						prevDescription = newDescription;
						prevDescription = prevStrain + " " + prevDescription;
					}
					
					SlimGenotypeDomain domain = new SlimGenotypeDomain();
					domain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					domain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(domain);
				}
								
			}
			sqlExecutor.cleanup();
			
			// now we order by description - see note above at first 'select = '
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

		String cmd = "select * from gxd_getgenotypesdatasets(" + key + ")";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				GenotypeDataSetDomain domain = new GenotypeDataSetDomain();
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setJnum(rs.getString("jnum"));
				domain.setJnumid(rs.getString("jnumid"));
				domain.setShort_citation(rs.getString("short_citation"));
				domain.setDataSet(rs.getString("dataSet"));
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
	public List<SlimGenotypeDomain> searchDataSets(Integer key) {
		// search data sets by reference key
		// return SlimGenotypeDomain
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		String cmd = 
				"select * from gxd_genotype_dataset_view where _Refs_key = " + key;

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

}
