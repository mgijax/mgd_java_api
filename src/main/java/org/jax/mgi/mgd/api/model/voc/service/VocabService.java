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
		return null;
	}

	@Transactional
	public SearchResults<VocabularyDomain> update(VocabularyDomain object, User user) {
		return null;
	}

	@Transactional
	public VocabularyDomain get(Integer key) {
		return translator.translate(vocabularyDAO.get(key));
	}
	
    @Transactional
    public SearchResults<VocabularyDomain> getResults(Integer key) {
        SearchResults<VocabularyDomain> results = new SearchResults<VocabularyDomain>();
        results.setItem(translator.translate(vocabularyDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
		return null;
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
		String orderBy = "order by t.sequencenum";		
		
		// if parameter exists, then add to where-clause
		
//		String cmResults[] = DateSQLQuery.queryByCreationModification("t", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
//		if (cmResults.length > 0) {
//			from = from + cmResults[0];
//			where = where + cmResults[1];
//		}
		
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty()) {
			where = where + "\nand t._vocab_key = " + searchDomain.getVocabKey();
		}
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand v.name ilike '" + searchDomain.getName() + "'";
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
