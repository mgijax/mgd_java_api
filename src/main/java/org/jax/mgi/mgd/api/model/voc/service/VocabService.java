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
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VocabularyDomain> create(VocabularyDomain object, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<VocabularyDomain> update(VocabularyDomain object, User user) {
		SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
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
		return domain;
	}
	
    @Transactional
    public SearchResults<VocabularyDomain> getResults(Integer key) {
        SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
        results.setItem(translator.translate(vocabularyDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<SlimVocabularyDomain> search(SlimVocabularyDomain searchDomain) {	
		// search for 1 vocabulary
		// assumes that either key or  name is being searched
		// returns empty result items if vocabulary does not exist
		// returns SlimVocabularyDomain results if vocabulary does exist
			
		SearchResults<SlimVocabularyDomain> results = new SearchResults<SlimVocabularyDomain>();

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
			SlimVocabularyDomain domain = new SlimVocabularyDomain();						
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
