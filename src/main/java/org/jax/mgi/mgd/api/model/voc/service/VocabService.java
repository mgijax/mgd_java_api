package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimVocabularyTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.translator.VocabularyTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VocabService extends BaseService<VocabularyDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private VocabularyDAO vocabularyDAO;
	
	private VocabularyTranslator translator = new VocabularyTranslator();
	private TermService termService = new TermService();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VocabularyDomain> create(VocabularyDomain domain, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<VocabularyDomain> update(VocabularyDomain domain, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		
		if(domain.getTerms() != null && !domain.getTerms().isEmpty()) {
			log.info("VocabService.createTerms calling TermService.process");
			termService.process(domain.getTerms(), user);
			log.info("VocabService.createTerms returned from calling TermService.process");
			// return results with new terms 
			return  this.getResults(Integer.valueOf(domain.getVocabKey()));
			
		}
		// otherwise return the same results
		log.info("VocabService.createTerms no terms to process"); // should never get here.
		results.setItem(domain);
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
    public List<SlimVocabularyDomain> searchSimple() {
    	
    	List<SlimVocabularyDomain> results = new ArrayList<SlimVocabularyDomain>();
    	
    	String cmd = "";
		String select = "select v._vocab_key, v.name";
		String from = "from voc_vocab v";
		String where = "where v.isSimple = '1'";
		String orderBy = "order by v.name";
		
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {						
			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimVocabularyDomain domain = new SlimVocabularyDomain();
				domain.setVocabKey(rs.getString("_vocab_key"));
				domain.setName(rs.getString("name"));
				log.info("key: " + domain.getVocabKey() + " name: " + domain.getName());
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
	public SearchResults<SlimVocabularyTermDomain> search(SlimVocabularyTermDomain searchDomain) {	
		// search for 1 vocabulary
		// assumes that either key or  name is being searched
		// returns empty result items if vocabulary does not exist
		// returns SlimVocabularyDomain results if vocabulary does exist
			
		SearchResults<SlimVocabularyTermDomain> results = new SearchResults<SlimVocabularyTermDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select v.*, t._term_key, t.term, t.abbreviation";
		String from = "from voc_term t, voc_vocab v";
		String where = "where t._vocab_key = v._vocab_key";
		
		// default order
		// for vocab specific ordering, reset orderBy based on _vocab_key or name
		String orderBy = "order by t.term";		
		
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
					|| searchDomain.getName().equals("GXD HT Sample")
					|| searchDomain.getName().equals("GXD Index Assay")
					|| searchDomain.getName().equals("GXD Index Stages")
					) {
				orderBy = "order by t.sequencenum";
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
			if (searchDomain.getVocabKey().equals("39")
					|| searchDomain.getVocabKey().equals("42")) {
				orderBy = "order by t.sequenceNum";
			}

			// 2 = Mammalian Phenotype Evidence Code
			// 3 = GO Evidence Code
			if (searchDomain.getVocabKey().equals("2")
					|| searchDomain.getVocabKey().equals("3")) {
				orderBy = "order by t.abbreviation";
			}
			
		}
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand v.name ilike '" + searchDomain.getName() + "'";
		}
		
		// special list of _vocab_key = 86
		// HTMP Property->MP-Sex Specificity
		log.info("searchDomain.getName" + searchDomain.getName() );
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

}
