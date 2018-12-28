package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class TermService extends BaseService<TermDomain> {

	protected Logger log = Logger.getLogger(TermService.class);

	@Inject
	private TermDAO termDAO;
	
//	@Inject
//	private VocabularyDAO vocabDAO;
	
//	@Inject
//	private UserService userService;
	
	private TermTranslator translator = new TermTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<TermDomain> create(TermDomain termDomain, User user) {
		/*
		The VOC_Term primary key is not auto-sequenced - we will need a way to get the next primary key available.

		Increment the max sequence number of the vocabulary OR user can specify sequenceNum. This requires 'shuffling' of sequence numbers above this (update).

		Optional *single* synonym for 'GO properties' vocabulary only. Single synonym is convention in the Teleuse/EI Simple Vocab Module. MGI_SynonymType._SynonymType_key = 1034 (MGI_GORel), MGI_SynonymType._MGIType_key = 13 (VOC_Term), VOC_Vocab._Vocab_key = 82 (GO Property)
		*/
		
		//Vocabulary vocab = vocabDAO.getByName(termDomain.getVocabName());

		//if(vocab == null) {
		//	throw new NotFoundException("Vocabulary not found for: " + termDomain.getVocabName());
		//}
		
		//for(Term t: vocab.getTerms()) {
		//	if(termDomain.getTerm().equals(t.getTerm())) {
		//		throw new DuplicateEntryException("Duplicate Term found for: " + termDomain.getTerm() + " in " + vocab.getName());
		//	}
		//}
		
		//Term term = translator.translate(termDomain);
		
		//log.debug("Creating Term: " + term);
		
		//Date now = new Date();
		//term.setCreation_date(now);
		//term.setCreatedBy(user);
		//term.setModification_date(now);
		//term.setModifiedBy(user);
		//term.set_term_key(termDAO.getNextKey());
		//term.setVocab(vocab);

		//Term returnTerm = termDAO.create(term);
		//log.info("Create finished: " + returnTerm);
		
		//TermDomain returnTermDomain = translator.translate(returnTerm);
		
		//log.info("Return Domain: " + returnTermDomain);
		
		//return returnTermDomain;
		return null;
	}

	@Transactional
	public SearchResults<TermDomain> update(TermDomain object, User user) {
		//Term term = translator.translate(object);
		
		//term.setCreatedBy(userService.getUserByUsername(object.getCreatedBy()));
		//term.setModifiedBy(user);
		
		//Term returnTerm = termDAO.update(term);

		//TermDomain termDomain = translator.translate(returnTerm);
		//return termDomain;
		return null;
	}
	
	@Transactional
	public TermDomain get(Integer key) {
		return translator.translate(termDAO.get(key));
	}
	
    @Transactional
    public SearchResults<TermDomain> getResults(Integer key) {
        SearchResults<TermDomain> results = new SearchResults<TermDomain>();
        results.setItem(translator.translate(termDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<TermDomain> delete(Integer key, User user) {
        // TODO Auto-generated method stub
		return null;
	}

	@Transactional	
	public List<TermDomain> search(TermDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<TermDomain> results = new ArrayList<TermDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select t.*, v.name, u1.login as createdby, u2.login as modifiedby";
		String from = "from voc_term t, voc_vocab v, mgi_user u1, mgi_user u2";
		String where = "where t._vocab_key = v._vocab_key"
				+ "\nand t._createdby_key = u1._user_key"
				+ "\nand t._modifiedby_key = u2._user_key";
		String orderBy = "order by t.term";
		Boolean from_accession = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("t", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getTermKey() != null && !searchDomain.getTermKey().isEmpty()) {
			where = where + "\nand t._term_key = " + searchDomain.getTermKey();
		}
		if (searchDomain.getTerm() != null && !searchDomain.getTerm().isEmpty()) {
			where = where + "\nand t.term ilike '" + searchDomain.getTerm() + "'";
		}
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty()) {
			where = where + "\nand t._vocab_key = " + searchDomain.getVocabKey();
		}
		if (searchDomain.getVocabName() != null && !searchDomain.getVocabName().isEmpty()) {
			where = where + "\nand v.name ilike '" + searchDomain.getVocabName() + "'";
		}
		
		// accession id
		if (searchDomain.getAccessionId() != null) {
			where = where + "\nand a.accID ilike '" + searchDomain.getAccessionId().getAccID() + "'";
			from_accession = true;
		}
		
		if (from_accession == true) {
			select = select + ", a.*";
			from = from + ", acc_accession a";
			where = where + "\nand t._term_key = a._object_key" 
					+ "\nand a._mgitype_key = 13 and a.preferred = 1";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain domain = new TermDomain();
				SlimAccessionDomain accDomain = new SlimAccessionDomain();				
			
				domain.setTermKey(rs.getString("_term_key"));
				domain.setTerm(rs.getString("term"));
				domain.setVocabKey(rs.getString("_vocab_key"));
				domain.setVocabName(rs.getString("name"));
				domain.setAbbreviation(rs.getString("abbreviation"));
				domain.setNote(rs.getString("note"));
				domain.setSequenceNum(rs.getString("sequenceNum"));
				domain.setIsObsolete(rs.getString("isObsolete"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdby"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedby"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));	
				
				if (from_accession == true) {
					accDomain.setAccessionKey(rs.getString("_accession_key"));
					accDomain.setLogicaldbKey(rs.getString("_logicaldb_key"));
					accDomain.setObjectKey(rs.getString("_object_key"));
					accDomain.setMgiTypeKey(rs.getString("_mgitype_key"));
					accDomain.setAccID(rs.getString("accID"));
					accDomain.setPrefixPart(rs.getString("prefixPart"));
					accDomain.setNumericPart(rs.getString("numericPart"));
					domain.setAccessionId(accDomain);
				}
				
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
	public SearchResults<SlimTermDomain> validTerm(int vocabKey, String term) {
		// verify that the term is valid for the given vocabulary name
		// returns empty result items if term does not exist
	
		SearchResults<SlimTermDomain> results = new SearchResults<SlimTermDomain>();

		String cmd = "select t._term_key, t.term, t.abbreviation"
				+ "\nfrom voc_term t"
				+ "\nwhere t._vocab_key = " + vocabKey
				+ "\nand t.term = '" + term + "'";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimTermDomain domain = new SlimTermDomain();						
				domain.setTermKey(rs.getString("_term_key"));
				domain.setTerm(rs.getString("term"));
				domain.setAbbreviation(rs.getString("abbreviation"));
				results.setItem(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
	@Transactional
	public SearchResults<SlimTermDomain> validWorkflowStatus(String status) {
		// verify that the work flow status is valid
		// returns empty result items if workflow status does not exist
		// _vocab_key = 128 ("Workflow Status")
		return validTerm(128, status);
	}
		
}
