package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;
import org.jax.mgi.mgd.api.model.voc.translator.VocabularyTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class VocabService extends BaseService<VocabularyDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private VocabularyDAO vocabularyDAO;
	@Inject
	private TermService termService = new TermService();
	
	private VocabularyTranslator translator = new VocabularyTranslator();

	@Transactional
	public SearchResults<VocabularyDomain> create(VocabularyDomain domain, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<VocabularyDomain> update(VocabularyDomain domain, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		Vocabulary entity = vocabularyDAO.get(Integer.valueOf(domain.getVocabKey()));
		Boolean modified = false;
		String cmd;
		Query query;
		
		log.info("processVocabuary/update");
		if (termService.process(domain.getVocabKey(), domain.getTerms(), user)) {
			modified = true;
		}		
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			vocabularyDAO.update(entity);
			log.info("processVocabulary/changes processed: " + domain.getVocabKey());
		}
		else {
			log.info("processVocabulary/no changes processed: " + domain.getVocabKey());
		}

		// reset of the terms is vocabulary-specific
		// 48 = Journal
		if (domain.getVocabKey().equals("48")) {
		    cmd = "select count(*) from VOC_resetTerms(" + domain.getVocabKey() + ")";
		    log.info("cmd: " + cmd);
		    query = vocabularyDAO.createNativeQuery(cmd);
		    query.getResultList();	
		}
		
		// reset the sequence numbers so there are no gaps
		cmd = "select count(*) from MGI_resetSequenceNum ('VOC_Term'," + domain.getVocabKey() + "," + user.get_user_key() + ")";		    
		log.info("cmd: " + cmd);
		query = vocabularyDAO.createNativeQuery(cmd);
		query.getResultList();	
		
		// return entity translated to domain
		log.info("processVocabulary/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processVocabulary/update/returned results succsssful");
		return results;		
	}

	@Transactional
	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public VocabularyDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		VocabularyDomain domain = new VocabularyDomain();
		if (vocabularyDAO.get(key) != null) {
			domain = translator.translate(vocabularyDAO.get(key));
		}
		vocabularyDAO.clear();
		return domain;
	}
	
    @Transactional
    public SearchResults<VocabularyDomain> getResults(Integer key) {
        SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
        results.setItem(translator.translate(vocabularyDAO.get(key)));
        vocabularyDAO.clear();
        return results;
    }

    @Transactional	
	public SearchResults<VocabularyDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		String cmd = "select count(*) as objectCount from voc_vocab";
		
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
	public SearchResults<SlimVocabularyTermDomain> search(SlimVocabularyTermDomain searchDomain) {	
		// search for 1 vocabulary
		// assumes that either key or  name is being searched
		// returns empty result items if vocabulary does not exist
		// returns SlimVocabularyDomain results if vocabulary does exist
			
		SearchResults<SlimVocabularyTermDomain> results = new SearchResults<SlimVocabularyTermDomain>();
 
		String cmd = "";
		String select = "select v.*, t._term_key, t.term, t.abbreviation";
		String from = "from voc_term t, voc_vocab v";
		String where = "where t._vocab_key = v._vocab_key";
		
		// default order
		// for vocab specific ordering, reset orderBy based on _vocab_key or name
		String orderBy = "order by t.term";		
		
		String celltypeSelect = select + ", a.accid";
		String celltypeFrom = from + ", acc_accession a";
		String celltypeWhere = where + "\nand t._term_key = a._object_key and a.preferred = 1 and a._logicaldb_key = 173";
		Boolean isCellType = false;
		
		// for non-vocab tables that are acting like voc_vocab/voc_term		
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty()) {
			if (searchDomain.getVocabKey().equals("151")
					|| searchDomain.getVocabKey().equals("152")
					|| searchDomain.getVocabKey().equals("153")
					|| searchDomain.getVocabKey().equals("154")
					|| searchDomain.getVocabKey().equals("155")
					|| searchDomain.getVocabKey().equals("156")
					|| searchDomain.getVocabKey().equals("157")
					|| searchDomain.getVocabKey().equals("158")				
					|| searchDomain.getVocabKey().equals("159")
					|| searchDomain.getVocabKey().equals("160")
					|| searchDomain.getVocabKey().equals("162")
					|| searchDomain.getVocabKey().equals("163")
					|| searchDomain.getVocabKey().equals("172")
					|| searchDomain.getVocabKey().equals("173")					
					) {
				
				return searchGXDVocab(searchDomain.getVocabKey());		
			}
		}
		
		// for _vocab_key = 96, 97 (used by mgi_relationship)	
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty() &&
					(searchDomain.getVocabKey().equals("96")
					|| searchDomain.getVocabKey().equals("97"))) {
			if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
					return searchRelationshipVocab(searchDomain.getVocabKey(), searchDomain.getName());		
			}
		}
		
		// for UIs that use getName()
		// for ordering by sequenceNum, add specific vocab to this list		
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			if (searchDomain.getName().equals("GXD HT Evaluation State")
					|| searchDomain.getName().equals("GXD HT Curation State")
					|| searchDomain.getName().equals("GXD HT Study Type")
					|| searchDomain.getName().equals("GXD HT Age")
					|| searchDomain.getName().equals("GXD HT Experiment Type")
					|| searchDomain.getName().equals("GXD HT Experiment Variables")
					|| searchDomain.getName().equals("GXD HT Relevance")
					|| searchDomain.getName().equals("GXD HT RNA-Seq Type")					
					|| searchDomain.getName().equals("GXD HT Sample")
					|| searchDomain.getName().equals("GXD Index Assay")
					|| searchDomain.getName().equals("GXD Index Stages")
					) {
				orderBy = "order by t.sequencenum";
			}
			
			// cell ontology (_vocab_key = 102)/get primary id
			if (searchDomain.getName().equals("Cell Ontology")) {
				select = celltypeSelect;
				from = celltypeFrom;
				where = celltypeWhere;
				isCellType = true;
			}
		}
		
		// for UIs that use getVocabKey()
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty()) {
			where = where + "\nand t._vocab_key = " + searchDomain.getVocabKey();
			
			// list of _vocab_key = 79/marker feature types to exclude
			if (searchDomain.getVocabKey().equals("79")) {
				where = where + "\nand t.term not in ("
					+ "'DNA segment',"
					+ "'QTL',"
					+ "'BAC/YAC end',"
					+ "'BAC end',"
					+ "'YAC end',"
					+ "'PAC end',"
					+ "'transgene',"
					+ "'complex/cluster/region',"
					+ "'cytogenetic marker',"
					+ "'all feature types'"
					+ ")";
				 orderBy = "order by t.term";		
			}
			
			// 39 = Allele Pair State
			// 42 = Allele Compound
			// 37 = Allele Status
			// 147 = GXD Default Age
			// 17 = Gender
			// 10 = Segment Type (probe)
			// 24 = Vector Type (probe)
			// 150 = Molecular Segment Note (probe)
			// 161 = GXD Assay Age
			// 174 = Allele Inducible
			// 180 = GXD Antibody Type
			// 187 = GXD color
			if (searchDomain.getVocabKey().equals("39")
					|| searchDomain.getVocabKey().equals("42")
					|| searchDomain.getVocabKey().equals("37") 
					|| searchDomain.getVocabKey().equals("147")
					|| searchDomain.getVocabKey().equals("17")
					|| searchDomain.getVocabKey().equals("10")
					|| searchDomain.getVocabKey().equals("24")
					|| searchDomain.getVocabKey().equals("150")	
					|| searchDomain.getVocabKey().equals("161")	
					|| searchDomain.getVocabKey().equals("174")	
					|| searchDomain.getVocabKey().equals("180")		
					|| searchDomain.getVocabKey().equals("187")																
					) {
				orderBy = "order by t.sequenceNum";
			}

			// 2 = Mammalian Phenotype Evidence Code
			// 3 = GO Evidence Code
			if (searchDomain.getVocabKey().equals("2")
					|| searchDomain.getVocabKey().equals("3")) {
				orderBy = "order by t.abbreviation";
			}
			
			// cell ontology (_vocab_key = 102)/get priimary id
			if (searchDomain.getVocabKey().equals("102")) {
				select = celltypeSelect;
				from = celltypeFrom;
				where = celltypeWhere;
				isCellType = true;
			}			
		}
		
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand v.name ilike '" + searchDomain.getName() + "'";
		}
		
		// special list of _vocab_key = 86
		// HTMP Property->MP-Sex Specificity
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			if (searchDomain.getName().equals("MP-Sex-Specificity")) {		
					select = "(select _Vocab_key, _Term_key, 'MP-Sex-Specificity' as name, 'F' as term, 'F' as abbreviation from VOC_Term where _Term_key = 8836535"
						+ "\nunion"
						+ "\nselect _Vocab_key, _Term_key, 'MP-Sex-Specificity' as name, 'M' as term, 'M' as abbreviation from VOC_Term where _Term_key = 8836535"
						+ "\nunion"
						+ "\nselect _Vocab_key, _Term_key, 'MP-Sex-Specificity' as name, 'NA' as term, 'NA' as abbreviation from VOC_Term where _Term_key = 8836535"						
						+ ")";
				from = "";
				where = "";
				orderBy = "order by term";				
			}
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);		
		
		try {
			SlimVocabularyTermDomain domain = new SlimVocabularyTermDomain();						
			List<SlimTermDomain> termList = new ArrayList<SlimTermDomain>();
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				SlimTermDomain termDomain = new SlimTermDomain();				
				domain.setVocabKey(rs.getString("_vocab_key"));
				domain.setName(rs.getString("name"));
				termDomain.setTermKey(rs.getString("_term_key"));
				termDomain.set_term_key(rs.getInt("_term_key"));
				termDomain.setTerm(rs.getString("term"));
				termDomain.setAbbreviation(rs.getString("abbreviation"));
				termDomain.setVocabKey(rs.getString("_vocab_key"));
				
				if (isCellType == true) {
					termDomain.setPrimaryid(rs.getString("accid"));
				}
				
				termList.add(termDomain);
			}
			
			domain.setTerms(termList);
			results.setItem(domain);		
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public SearchResults<SlimVocabularyTermDomain> searchGXDVocab(String vocabKey) {	
		// returns list of gxd vocabulary into SlimVocabularyTermDomain format
		//
		// for those using abbreviations (control (154), embedding (155), fixation (156), gel rna type (172))
		//		voc_term.abbreviation is used
		//		which means any crud change to GXD term tables will also require manual crud change to VOC_Term
		//		that is, the GXD term (GXD_GelControl, GXD_EmbeddingMethod, etc) and VOC_Term tables must be synced up
		//
		// for those vocabs that are *not* using abbreviations, all crud changes are using GXD term tables only
		//
		
		SearchResults<SlimVocabularyTermDomain> results = new SearchResults<SlimVocabularyTermDomain>();
		
		String cmd = "";
		
		if (vocabKey.equals("158")) {
			cmd = "select _assaytype_key as termKey, assayType as term from gxd_assaytype order by term";
		}	
		else if (vocabKey.equals("151")) {
			cmd = "select _term_key as termKey, term as term from voc_term where _vocab_key = 151 order by term";
		}		
		else if (vocabKey.equals("154")
				 || vocabKey.equals("155")
				 || vocabKey.equals("156")	
				 || vocabKey.equals("172")) {
			cmd = "select v._term_key as termKey, v.abbreviation as term, 1 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term = 'Not Specified'" + 
				"\nunion" + 
				"\nselect v._term_key as termKey, v.abbreviation as term, 2 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term != 'Not Specified'" + 
				"\norder by orderBy, term\n";
		}	
		else if (vocabKey.equals("162")) {
			cmd = "select v._term_key as termKey, v.term as term, v.abbreviation, 1 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term = 'Not Specified'" + 
				"\nunion" + 
				"\nselect v._term_key as termKey, v.term as term, v.abbreviation, 2 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term != 'Not Specified'" + 
				"\norder by orderBy, term\n";
		}	
		else if (vocabKey.equals("163")) {
			cmd = "select v._term_key as termKey, v.term as term, v.abbreviation, 1 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term = 'Present'" + 
				"\nunion" + 
				"\nselect v._term_key as termKey, v.term as term, v.abbreviation, 2 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term = 'Absent'" + 					
				"\nunion" + 
				"\nselect v._term_key as termKey, v.term as term, v.abbreviation, 3 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term = 'Not Specified'" + 
				"\nunion" +					
				"\nselect v._term_key as termKey, v.term as term, v.abbreviation, 4 as orderBy from voc_term v where v._vocab_key = " + vocabKey +
					"\nand v.term not in ('Not Specified', 'Present', 'Absent')" + 
				"\norder by orderBy, term\n";
		}		
		else {
			cmd = "select _term_key as termKey, term as term, 1 as orderBy from voc_term where _vocab_key = " + vocabKey +
					"\nand term = 'Not Specified'" +
				"\nunion" +
				"\nselect _term_key as termKey, term as term, 2 as orderBy from voc_term where _vocab_key = " + vocabKey +
					"\nand term != 'Not Specified'" + 
				"\norder by orderBy, term\n";
		}		
		log.info(cmd);		
		
		try {
			SlimVocabularyTermDomain domain = new SlimVocabularyTermDomain();						
			List<SlimTermDomain> termList = new ArrayList<SlimTermDomain>();
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				SlimTermDomain termDomain = new SlimTermDomain();				
				domain.setVocabKey(vocabKey);						
				domain.setName(rs.getString("term"));
				termDomain.setTermKey(rs.getString("termKey"));
				termDomain.set_term_key(rs.getInt("termKey"));
				termDomain.setTerm(rs.getString("term"));
				termDomain.setVocabKey(rs.getString("termKey"));
				
				if (vocabKey.equals("162")) {
					termDomain.setAbbreviation(rs.getString("abbreviation"));
				}
				
				termList.add(termDomain);
			}
			
			domain.setTerms(termList);
			results.setItem(domain);		
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;			
	}

	@Transactional
	public SearchResults<SlimVocabularyTermDomain> searchRelationshipVocab(String vocabKey, String vocabName) {	
		// returns list of relationship vocabulary into SlimVocabularyTermDomain format
		
		SearchResults<SlimVocabularyTermDomain> results = new SearchResults<SlimVocabularyTermDomain>();
		
		String cmd = "";

		if (vocabName.equals("mutationInvolves")) {
			cmd = "select _term_key as termKey, term from voc_term where _vocab_key = " + vocabKey
					+ "\nand _term_key in (12438350, 12438354, 12438355, 12438356, 12438357, 12438358, 12438359, 12438360, 12438361, 12438362, 31401216, 95815138)" 
					+ "\norder by term";
		}
		else if (vocabName.equals("expressesComponents")) {		
			cmd = "select _term_key as termKey, term from voc_term where _vocab_key = " + vocabKey
					+ "\nand _term_key in (12438346)"					
					+ "\norder by term";
		}
		else if (vocabName.equals("driverComponents")) {		
			cmd = "select _term_key as termKey, term from voc_term where _vocab_key = " + vocabKey
					+ "\nand _term_key in (111172001)"
					+ "\norder by term";
		}	
		else if (vocabName.equals("clusterHasMember")) {		
			cmd = "select _term_key as termKey, term from voc_term where _vocab_key = " + vocabKey
					+ "\nand _term_key in (12438344, 12438347)"
					+ "\norder by term";
		}	
		else if (vocabName.equals("properties")) {		
			cmd = "select _term_key as termKey, term from voc_term where _vocab_key = " + vocabKey
					+ "\nand _vocab_key = 97 and term like 'Non_mouse%' "
					+ "\norder by sequenceNum";
		}
		
		log.info(cmd);		
		
		try {
			SlimVocabularyTermDomain domain = new SlimVocabularyTermDomain();						
			List<SlimTermDomain> termList = new ArrayList<SlimTermDomain>();
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				SlimTermDomain termDomain = new SlimTermDomain();				
				domain.setVocabKey(vocabKey);
				domain.setName(rs.getString("term"));
				termDomain.setTermKey(rs.getString("termKey"));
				termDomain.set_term_key(rs.getInt("termKey"));
				termDomain.setTerm(rs.getString("term"));
				termDomain.setVocabKey(vocabKey);
				termList.add(termDomain);
			}
			
			domain.setTerms(termList);
			results.setItem(domain);		
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;			
	}
	
    @Transactional
    public List<SlimVocabularyDomain> searchSimple() {
    	
    	List<SlimVocabularyDomain> results = new ArrayList<SlimVocabularyDomain>();
    	
    	String cmd = "select _vocab_key, name"
				+ "\nfrom voc_vocab"
    			+ "\nwhere isSimple = '1'"
				+ "\norder by name";
		
		log.info(cmd);
		
		try {								
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimVocabularyDomain domain = new SlimVocabularyDomain();
				domain.setVocabKey(rs.getString("_vocab_key"));
				domain.setName(rs.getString("name"));
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
	public SearchResults<String> getVocabList(SlimVocabularyTermDomain searchDomain) {
		// generate SQL command to return a list of distinct terms for given vocabulary
		
		List<String> results = new ArrayList<String>();

		String cmd = "";
		String select = "select distinct term from VOC_Term where _vocab_key = " + searchDomain.getVocabKey();
		String orderBy = "order by term";
		cmd = select + "\n" + orderBy;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.add(rs.getString("term"));
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return new SearchResults<String>(results);
	}
	
}
