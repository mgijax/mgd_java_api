package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDagParentDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.translator.SlimTermTranslator;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class TermService extends BaseService<TermDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private TermDAO termDAO;
	
	@Inject
	private MGISynonymService synonymService;
	
	private TermTranslator translator = new TermTranslator();
	private SlimTermTranslator slimtranslator = new SlimTermTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<TermDomain> create(TermDomain domain, User user) {
		SearchResults<TermDomain> results = new SearchResults<TermDomain>();
		//results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		
		Term entity = new Term();	
		String vocabKey = domain.getVocabKey();
		
		log.info("vocabKey: " + vocabKey );
		entity.set_vocab_key(Integer.valueOf(vocabKey));
		
		log.info("term: " + domain.getTerm());
		entity.setTerm(domain.getTerm());
		entity.setAbbreviation(domain.getAbbreviation());
		entity.setNote(domain.getNote());
		
		log.info("seqNum: " + domain.getSequenceNum());
		if (vocabKey.equals("18")) { // cell line vocab
			log.info("cell line vocab");
			String seqNum = getNextSequenceNum(vocabKey);		
			log.info("next cell line seqnum: " + seqNum);
			entity.setSequenceNum(Integer.valueOf(seqNum));
		}
		else if (domain.getSequenceNum() == null || domain.getSequenceNum().isEmpty()) {
			entity.setSequenceNum(null); // some vocabs have null sequenceNum
		}
		else {
			entity.setSequenceNum(Integer.valueOf(domain.getSequenceNum()));
		}
		log.info("seqNum after calculating: " + domain.getSequenceNum());
		
		log.info("isObsolete: " +  domain.getIsObsolete());
		if (domain.getIsObsolete() == null || domain.getIsObsolete().isEmpty() ) {
			entity.setIsObsolete(0); // default
		}
		else {
			entity.setIsObsolete(Integer.valueOf(domain.getIsObsolete()));	
		}

		entity.setCreatedBy(user);
		entity.setModifiedBy(user);
		entity.setCreation_date(new Date());
		entity.setModification_date(new Date());
		termDAO.persist(entity);
		
		results.setItem(translator.translate(entity));
	
		log.info("processTerm/create processed");												
		return results;
	}

	@Transactional
	public SearchResults<TermDomain> update(TermDomain object, User user) {
		SearchResults<TermDomain> results = new SearchResults<TermDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<TermDomain> delete(Integer key, User user) {
		SearchResults<TermDomain> results = new SearchResults<TermDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public TermDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		TermDomain domain = new TermDomain();
		if (termDAO.get(key) != null) {
			domain = translator.translate(termDAO.get(key));
		}
		else {
			return domain;
		}
		termDAO.clear();
		
		// use SQL query to load list of DAG/Parents
		List<TermDomain> dagParents = new ArrayList<TermDomain>();
		dagParents = getDagParents(key);
		domain.setDagParents(dagParents);
		log.info("get Domain term: " + domain.getTerm());
		
		// use SQL query to load cell type annotation count
		if(domain != null ) {
			if (domain.getVocabKey().equals("102")) {
		
				domain.setCellTypeAnnotCount(getCelltypeAnnotCount(key));
		
			}
		}	
		return domain;
	}
	
    @Transactional
    public SearchResults<TermDomain> getResults(Integer key) {
        SearchResults<TermDomain> results = new SearchResults<TermDomain>();
        results.setItem(translator.translate(termDAO.get(key)));
        termDAO.clear();
        return results;
    }

	@Transactional	
	public List<TermDomain> search(TermDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		List<TermDomain> results = new ArrayList<TermDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		// Note that the initial from clause does not include voc_term. The from/voc_term needs
		// to be at the end of the from clause due to the outer join to synonyms for cell type
		String cmd = "";
		String select = "select distinct t.*, v.name, u1.login as createdby, u2.login as modifiedby";
		String from = "from voc_vocab v, mgi_user u1, mgi_user u2";
		String where = "where t._vocab_key = v._vocab_key"
				+ "\nand t._createdby_key = u1._user_key"
				+ "\nand t._modifiedby_key = u2._user_key";
		String orderBy = "order by t.term";
		
		// building SQL command for querying for exact synonyms with same string as term
		// right now just used by vocabKey = 102 celltype
		String synonymFrom = from + ", mgi_synonym s, voc_term t" ;
		String synonymJoin = "left outer join MGI_synonym s on "
				+ "\n(t._term_key = s._object_key " 
				+ "\nand s._mgitype_key = 13 "
				+ "\nand s._synonymtype_key = 1017)";
		String synonymWhere = where;
		String synonymUnion = "";
		String synonymOrderBy = "order by term";
		
		Boolean from_accession = false;
		//Boolean from_synonym = false;
		
		// value after escaping apostrophe in term
        String value = null;
	
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
					value = searchDomain.getTerm().replace("'",  "''");
					value = value.replace("(",  "\\(");
					value = value.replace(")", "\\)");
                	where = where + "\nand t.term ilike '" + value + "'";

		}
		if (searchDomain.getAbbreviation() != null && !searchDomain.getAbbreviation().isEmpty()) {
                        value = searchDomain.getAbbreviation().replace("'",  "''");
                        where = where + "\nand t.abbreviation ilike '" + value + "'";

		}		
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty()) {
			where = where + "\nand t._vocab_key = " + searchDomain.getVocabKey();
		}
//		if (searchDomain.getVocabName() != null && !searchDomain.getVocabName().isEmpty()) {
//			where = where + "\nand v.name ilike '" + searchDomain.getVocabName() + "'";
//		}
		
		// accession id
		if (searchDomain.getAccessionIds() != null) {
			where = where + "\nand lower(a.accID) = lower('" + searchDomain.getAccessionIds().get(0).getAccID() + "')";
			from_accession = true;
		}
		
		// for cell types we want to query the term synonyms by the passed in term string
		// we create a union of the term search and the synonym search, exact syn only
		if (searchDomain.getVocabKey().equals("102") && value != null) {
			synonymWhere = synonymWhere + "\nand s.synonym ilike '" + value + "'"
					+ "\nand s._mgitype_key = 13"
					+ "\nand s._synonymtype_key = 1017"
					+ "\nand t._term_key = s._object_key"
					+ "\nand t._vocab_key = " + searchDomain.getVocabKey();
			synonymUnion = "\nunion\n";
			synonymUnion = synonymUnion 
					+ select + "\n"
					+ synonymFrom + "\n"
					+ synonymWhere; 
		}
		
		if (from_accession == true) {
			select = select + ", a.*";
			from = from + ", acc_accession a";
			where = where + "\nand t._term_key = a._object_key" 
					+ "\nand a._mgitype_key = 13 and a.preferred = 1";
		}
		
		
		// now add voc_term to from - this has to be the last table because of the outer join
		// to mgi_synonym
		from = from + ", voc_term t";
		
		// include obsolete terms?
		if(searchDomain.getIncludeObsolete().equals(Boolean.FALSE)) {
			where = where + "\nand isObsolete != 1";
		}
		// make this easy to copy/paste for troubleshooting
		if (searchDomain.getVocabKey().equals("102")) {
			cmd = "\n" + select + "\n" + from + "\n" + synonymJoin + "\n" + where + "\n" + synonymUnion + "\n" + synonymOrderBy;
		}
		else {
			cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		}
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain domain = new TermDomain();
				domain = translator.translate(termDAO.get(rs.getInt("_term_key")));
				termDAO.clear();
				
				// use SQL query to load list of DAG/Parents
				List<TermDomain> dagParents = new ArrayList<TermDomain>();
				dagParents = getDagParents(rs.getInt("_term_key"));
				domain.setDagParents(dagParents);
				
				// use SQL query to load cell type annotation count
				if(searchDomain.getVocabKey().equals("102")) {
					domain.setCellTypeAnnotCount(getCelltypeAnnotCount(rs.getInt("_term_key")));
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
	public Boolean process(String vocabKey, List<TermDomain> domain, User user) {
		// process term associations (create, delete, update)

		Boolean modified = false;
		String mgiTypeKey = "13";
		
		if (domain == null || domain.isEmpty()) {
			log.info("processTerm/nothing to process");
			return modified;
		}
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {

			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("TermService.process.create");
				// if term is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getTerm() == null || domain.get(i).getTerm().isEmpty()) {
					log.info("empty term");
					continue;
				}
				
				Term entity = new Term();	
				log.info("vocabKey: " + vocabKey );
				entity.set_vocab_key(Integer.valueOf(vocabKey));
				log.info("term: " + domain.get(i).getTerm());
				entity.setTerm(domain.get(i).getTerm());
				entity.setAbbreviation(domain.get(i).getAbbreviation());
				entity.setNote(domain.get(i).getNote());
				log.info("seqNum: " + domain.get(i).getSequenceNum());
				if (domain.get(i).getVocabKey().equals("18")) { // cell line vocab
					log.info("cell line vocab");
					String seqNum = getNextSequenceNum(domain.get(i).getVocabKey());
					log.info("next cell line seqnum: " + seqNum);
					entity.setSequenceNum(Integer.valueOf(seqNum));
				}
				else if (domain.get(i).getSequenceNum() == null || domain.get(0).getSequenceNum().isEmpty()) {
					entity.setSequenceNum(null); // some vocabs have null sequenceNum
				}
				else {
					entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
				}
				log.info("seqNum after calculating: " + domain.get(i).getSequenceNum());
				
				log.info("isObsolete: " +  domain.get(i).getIsObsolete());
				if (domain.get(i).getIsObsolete() == null || domain.get(i).getIsObsolete().isEmpty() ) {
					entity.setIsObsolete(0); // default
				}
				else {
					entity.setIsObsolete(Integer.valueOf(domain.get(i).getIsObsolete()));	
				}

				entity.setCreatedBy(user);
				entity.setModifiedBy(user);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				termDAO.persist(entity);
				
				if (vocabKey.equals("82")) {
					log.info("processTerm/update synonym");
					modified = synonymService.process(String.valueOf(entity.get_term_key()), domain.get(i).getGoRelSynonyms(), mgiTypeKey, user);
				}
				
				modified = true;
				log.info("processTerm/create processed");												
			}
			
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processTerm delete");
				Term entity = termDAO.get(Integer.valueOf(domain.get(i).getTermKey()));
				termDAO.remove(entity);
				modified = true;
				log.info("processTerm/delete processed");
			} 
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processTerm update");
				Term entity = termDAO.get(Integer.valueOf(domain.get(i).getTermKey()));		

				if (vocabKey.equals("82")) {
					log.info("processTerm/update synonym");
					modified = synonymService.process(String.valueOf(entity.get_term_key()), domain.get(i).getGoRelSynonyms(), mgiTypeKey, user);
				}	
				
				entity.setTerm(domain.get(i).getTerm());
				entity.setAbbreviation(domain.get(i).getAbbreviation());
				entity.setNote(domain.get(i).getNote());
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
				entity.setIsObsolete(Integer.valueOf(domain.get(i).getIsObsolete()));			
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);		
				termDAO.update(entity);
				modified = true;
				log.info("processTerm/changes processed: " + domain.get(i).getTermKey());								
			}
			else {
				log.info("processTerm/no changes processed: " + domain.get(i).getTermKey());
			} 
		}
		
		log.info("processTerm/processing successful");
		return modified;
	}
	
	@Transactional
	public List<TermDomain> validateTerm(TermDomain domain) {
		// verify that the term is valid for the given vocabulary name
		// returns empty result items if term does not exist
		return search(domain);
	}

	@Transactional
	// Assumes the vocabKey being sent has non-null sequenceNum
	public String getNextSequenceNum(String vocabKey) {
		log.info("in getNextSequencNum");
		String next = null;
		
		String cmd = "select max(sequenceNum) as maxNum from VOC_Term"
					+ "\nwhere _Vocab_key = " + vocabKey;
		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			log.info("iterating over rs");
			while (rs.next()) {
				log.info("calling rs.getInt(\"maxNum\") + 1");
				int n  = rs.getInt("maxNum") + 1;
				log.info("value of n: " + n);
				log.info("calling String.valueOf(n)");
				next = String.valueOf(n);
				log.info("getNextSequenceNum next: " + next);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return next;
	}
	
	@Transactional	
	public List<SlimTermDomain> validateMPHeaderTerm(SlimTermDomain domain) {
		// verify that the term is a header term
		// return empty result items if term is not a header term
		
		List<SlimTermDomain> results = new ArrayList<SlimTermDomain>();

		String cmd = "select d._object_key from DAG_Node d" 
				+ "\nwhere d._dag_key = 4" 
				+  "\nand d._label_key = 3" 
				+  "\nand d._object_key = " + domain.getTermKey();

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimTermDomain slimdomain = new SlimTermDomain();
				slimdomain = slimtranslator.translate(termDAO.get(rs.getInt("_object_key")));
				termDAO.clear();		
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
	public SearchResults<SlimTermDomain> validateTermSlim(int vocabKey, String term) {
		// verify that the term is valid for the given vocabulary name
		// returns empty result items if term does not exist
		SearchResults<SlimTermDomain> results = new SearchResults<SlimTermDomain>();

		String cmd = "select t._term_key, t.term, t.abbreviation"
				+ "\nfrom voc_term t"
				+ "\nwhere t._vocab_key = " + vocabKey
				+ "\nand t.term ilike '" + term + "'";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimTermDomain domain = new SlimTermDomain();						
				domain = slimtranslator.translate(termDAO.get(rs.getInt("_term_key")));
				log.info("domain vocabKey: " + domain.getVocabKey() + " domain term key: " + domain.getTermKey() + " domain term: " + domain.getTerm());
				termDAO.clear();					
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
		return validateTermSlim(128, status);
	}
	
	@Transactional
	public List<SlimTermDomain> getTermSet(String setName) {
		// get the set of terms for a given MGI_Set
		// returns empty result items if setName does not exist
		// for objectType VOC_Term
		List<SlimTermDomain> results = new ArrayList<SlimTermDomain>();

		String cmd = "select t._term_key, t.term, sm.sequencenum" + 
				"\nfrom mgi_set s, mgi_setmember sm, voc_Term t" + 
				"\nwhere s.name = '" + setName + "'" + 
				"\nand s._mgitype_key = 13" + 
				"\nand s._set_key = sm._set_key" + 
				"\nand sm._object_key = t._term_key" + 
				"\norder by sm.sequenceNum";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimTermDomain domain = new SlimTermDomain();						
				domain = slimtranslator.translate(termDAO.get(rs.getInt("_term_key")));
				termDAO.clear();					
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
	public SearchResults<String> getVocabTermList(int vocabKey) {
		// generate SQL command to return a list of terms
		List<String> results = new ArrayList<String>();
		
		String cmd = "select term from VOC_Term where _Vocab_key = " + vocabKey;
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
		
		Collections.sort(results);
		return new SearchResults<String>(results);
	
	}

	@Transactional	
	public List<TermDomain> getJournalLicense(String journal) {
		// return list of journal license terms

		List<TermDomain> results = new ArrayList<TermDomain>();
		
		String cmd = "select _term_key"
				+ "\nfrom voc_term"
				+ "\nwhere _vocab_key = 48"
				+ "\nand term = '" + journal + "'";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				TermDomain domain = new TermDomain();						
				domain = translator.translate(termDAO.get(rs.getInt("_term_key")));
				termDAO.clear();					
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
	public List<TermDomain> getDagParents(Integer termKey) {
		// return list of parent terms of a term's children (e1._child_key)

		List<TermDomain> results = new ArrayList<TermDomain>();
		
		String cmd = "select t2._term_key as parentKey, t2.term as parentTerm" + 
				"\nfrom voc_term t1, dag_node n1 , dag_edge e1, dag_node n2, voc_term t2" + 
				"\nwhere n1._object_key = " + termKey +
				"\nand n1._object_key = t1._term_key" + 
				"\nand n1._node_key = e1._child_key" + 
				"\nand e1._parent_key = n2._node_key" + 
				"\nand n2._object_key = t2._term_key" +
				"\norder by t2.term";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				//TermDagParentDomain domain = new TermDagParentDomain();
				Integer key = Integer.valueOf(rs.getString("parentKey"));
				TermDomain parentDomain = translator.translate(termDAO.get(key));
				termDAO.clear();		
				//TermDomain parentDomain = get(Integer.valueOf(rs.getString("parentKey")));
				//domain.setParentKey(rs.getString("parentKey"));
				//domain.setParentTerm(rs.getString("parentTerm"));
				parentDomain.setCellTypeAnnotCount(getCelltypeAnnotCount(key));
				results.add(parentDomain);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@Transactional	
	public String getCelltypeAnnotCount(Integer termKey) {
		String cmd = "select count(*) as annotCt" + 
				"\nfrom gxd_isresultcelltype" +
				"\nwhere _celltype_term_key = " + termKey;
		log.info(cmd);
		
		String count = "";
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				count = rs.getString("annotCt");			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
		
	}
	
}
