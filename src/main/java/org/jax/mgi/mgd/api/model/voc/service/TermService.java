package org.jax.mgi.mgd.api.model.voc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuffer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermAncestorDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyEdgesViewDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyViewDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TreeViewNodeDomain;
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
			if (domain.getVocabKey() != null && !domain.getVocabKey().isEmpty()) {
				addAnnotCount(domain);
				addEmapInfo(domain);
			}
		}	
		return domain;
	}

	@Transactional
	public TermDomain getByAccid (String accid) {
		String cmd = "\nselect _object_key from acc_accession "
			+ "\nwhere accid = '" + accid + "'"
			+ "\nand _mgitype_key = 13 and preferred = 1";
		int termKey = 0;
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				termKey = rs.getInt("_object_key");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return termKey == 0 ? null : get(termKey);
	}

	@Transactional
	public SearchResults<TermDomain> getResults(Integer key) {
		SearchResults<TermDomain> results = new SearchResults<TermDomain>();
		results.setItem(translator.translate(termDAO.get(key)));
		termDAO.clear();
		return results;
	}


	public String multiMatchClause(String colExpr, String op, String searchTerm) {
		String[] values = searchTerm.split(";");
		String clause = "(";
		for(int i = 0; i < values.length; i++) {
			String value = values[i].trim();
			value = value.replace("'",  "''").replace("(",  "\\(").replace(")", "\\)");
			if (i > 0) clause += " or ";
			clause += colExpr + " " + op + " '" + value + "'";
		}
		clause += ")";
		return clause;
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
			where += "\nand " + multiMatchClause("t.term" , "ilike", searchDomain.getTerm());
		}
		if (searchDomain.getStagesearch() != null && !searchDomain.getStagesearch().isEmpty()) {
			String ssch = searchDomain.getStagesearch();
			from += ", voc_term_emapa ea";
			synonymFrom += ", voc_term_emapa ea";
			String w =  "\nand ea._term_key = t._term_key"
				+ "\nand exists (select 1 from voc_term_emaps es "
				+ "\n  where es._emapa_term_key = ea._term_key "
				+ "\n  and es._stage_key in (" + ssch + "))"
				;
			where += w;
			synonymWhere += w;
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
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty()
		&& searchDomain.getTerm() != null && !searchDomain.getTerm().isEmpty()) {
			synonymWhere = synonymWhere 
					+ "\nand " + multiMatchClause("s.synonym", "ilike", searchDomain.getTerm())
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
		if (searchDomain.getVocabKey() != null && !searchDomain.getVocabKey().isEmpty() ) {
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
				//List<TermDomain> dagParents = new ArrayList<TermDomain>();
				//dagParents = getDagParents(rs.getInt("_term_key"));
				//domain.setDagParents(dagParents);
				
				if(domain.getVocabKey() != null && !domain.getVocabKey().isEmpty()) {
					addEmapInfo(domain);
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
    public int searchByTerm(TermDomain searchDomain, Boolean clearSQL) {
        // using searchDomain fields, generate SQL command
        // search by _vocab_key and (term or abbreviation)
    	// clearSQL (true/false) determines if the sqlExecutor will be called (or not)
        // return term key (int)

    	int key = 0;

        String cmd = "";
        String select = "select distinct t._term_key from voc_term t";
        String where = "where t._vocab_key = " + searchDomain.getVocabKey();

        String value;
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

        cmd = "\n" + select + "\n" + where;
        log.info(cmd);

        try {
            ResultSet rs = sqlExecutor.executeProto(cmd);
            while (rs.next()) {
                key = rs.getInt("_term_key");
            }
			if (clearSQL) {            
				sqlExecutor.cleanup();
			}
        }
        catch (Exception e) {
        	e.printStackTrace();
        }

        return key;
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
		
		String cmd = "select t2._term_key as parentKey, t2.term as parentTerm, dl.label" + 
				"\nfrom voc_term t1, dag_node n1 , dag_edge e1, dag_label dl, dag_node n2, voc_term t2" + 
				"\nwhere n1._object_key = " + termKey +
				"\nand n1._object_key = t1._term_key" + 
				"\nand n1._node_key = e1._child_key" + 
				"\nand e1._parent_key = n2._node_key" + 
				"\nand n2._object_key = t2._term_key" +
				"\nand e1._label_key = dl._label_key" +
				"\norder by t2.term";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				Integer key = Integer.valueOf(rs.getString("parentKey"));
				TermDomain parentDomain = translator.translate(termDAO.get(key));
				termDAO.clear();		
				addAnnotCount(parentDomain);
				addEmapInfo(parentDomain);
				parentDomain.setEdgeLabel(rs.getString("label"));
				results.add(parentDomain);				
			}
			sqlExecutor.cleanup();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@Transactional	
	// Returns the number of direct annotations to the given term.
	// The vocabKey indicates which kind of term it is:
	//     102=cell type, 90=EMAPA, 91=EMAPS
	// If vocabKey is not one of these, no action is taken.
	public void addAnnotCount(TermDomain term) {
		String termKey = term.getTermKey();
		String vocabKey = term.getVocabKey();
		String cmd;
		switch (vocabKey) {
		case "102":
			// CellType
			cmd = "\nselect count(*) as annotCt" + 
				"\nfrom gxd_isresultcelltype irc, gxd_isresultstructure irs" +
				"\nwhere irc._result_key = irs._result_key" +
				"\nand irc._celltype_term_key = " + termKey
				;
			break;
		case "90":
			// EMAPA
			cmd = "\nselect count(*) as annotCt" + 
				"\nfrom gxd_expression" +
				"\nwhere _emapa_term_key = " + termKey
				;
			break;
		case "91":
			// EMAPS
			cmd = "\nselect count(*) as annotCt" + 
				"\nfrom gxd_expression e, voc_term_emaps es" +
				"\nwhere e._emapa_term_key = es._emapa_term_key" +
				"\nand e._stage_key = es._stage_key" +
				"\nand es._term_key = " + termKey
				;
			break;
		default:
			return;
		}

		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				term.setAnnotCount(rs.getString("annotCt"));
			}
			sqlExecutor.cleanup();						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEmapInfo (TermDomain term) {
		String termKey = term.getTermKey();
		String vocabKey = term.getVocabKey();
		String cmd;
		switch (vocabKey) {
		case "90": 
			cmd = "\nselect ea.startstage, ea.endstage, null as theilerstage, t1.dpcmin, t2.dpcmax"
			+ "\nfrom voc_term_emapa ea, gxd_theilerstage t1, gxd_theilerstage t2"
			+ "\nwhere ea.startstage = t1._stage_key and ea.endstage = t2._stage_key and ea._term_key = " + termKey
			;
			break;
		case "91":
			cmd = "\nselect es._stage_key as startstage, es._stage_key as endstage, " 
			+ " es._stage_key as theilerstage, t.dpcmin, t.dpcMax, es._emapa_term_key, aa.accid as emapaid"
			+ "\nfrom voc_term_emaps es, gxd_theilerstage t, acc_accession aa"
			+ "\nwhere es._stage_key = t._stage_key"
			+ "\nand es._term_key = " + termKey
			+ "\nand es._emapa_term_key = aa._object_key and aa._mgitype_key = 13 and aa._logicaldb_key = 169 and aa.preferred = 1"
			;
			break;
		default:
			return;
		}
		log.info(cmd);
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				term.setTheilerstage(rs.getInt("theilerstage"));
				term.setStartstage(rs.getInt("startstage"));
				term.setEndstage(rs.getInt("endstage"));
				term.setDpcmin(rs.getString("dpcmin"));
				term.setDpcmax(rs.getString("dpcmax"));
				if (vocabKey.equals("91")) {
					term.setEmapaTermKey(rs.getString("_emapa_term_key"));
					term.setEmapaTermID(rs.getString("emapaid"));
				}
			}
			sqlExecutor.cleanup();
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.toString());
			return;
		}

	}
	
	@Transactional	
	public List<TermAncestorDomain> getAncestorKeys(String keys) {
		// return list of ancestors from string of termKeys in format xxx,yyy,zzz

		List<TermAncestorDomain> results = new ArrayList<TermAncestorDomain>();
		
		// if keys is empty, return results
		if (keys.isEmpty()) {
			return results;
		}
		
		String cmd = "\nselect t._term_key as termKey, ancestor._term_key as ancestorKey" +
				"\nfrom VOC_Term t, VOC_VocabDAG vd, DAG_Node d, DAG_Closure dc, DAG_Node dh, VOC_Term ancestor" +
				"\nwhere t._Term_key in (" + keys + ")" +
				"\nand t._Vocab_key = vd._Vocab_key" +
				"\nand vd._DAG_key = d._DAG_key" +
				"\nand t._Term_key = d._Object_key" +
				"\nand d._Node_key = dc._Descendent_key" +
				"\nand dc._Ancestor_key = dh._Node_key" +
				"\nand dh._Object_key = ancestor._Term_key" +
				"\norder by termKey, ancestorKey";

		log.info(cmd);

		Integer prevTermKey = 0;
		TermAncestorDomain domain = null;
		List<Integer> ancestors = null;

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				int termKey = rs.getInt("termKey");
				int ancestorKey = rs.getInt("ancestorKey");
				
				if (!prevTermKey.equals(termKey)) {
					
					if (!rs.isFirst()) {
						domain.setAncestors(ancestors);
						results.add(domain);
					}
					
					domain = new TermAncestorDomain();
					domain.setTermKey(termKey);
					ancestors = new ArrayList<Integer>();
				}
	
				ancestors.add(ancestorKey);
				prevTermKey = termKey;
				
				if (rs.isLast()) {
					domain.setAncestors(ancestors);
					results.add(domain);				
				}
			}
			sqlExecutor.cleanup();						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional	
	public List<TermFamilyViewDomain> getTermFamilyByAccId(String accid) {
		// return TermFamilyViewDomain by term accid
		
		List<TermFamilyViewDomain> results = new ArrayList<TermFamilyViewDomain>();
		
		String cmd = "select * from VOC_TermFamily_View where searchid = '" + accid + "'";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermFamilyViewDomain domain = new TermFamilyViewDomain();	
				domain.setTermKey(rs.getString("_term_key"));
				domain.setSearchid(rs.getString("searchid"));
				domain.setAccid(rs.getString("accid"));
				domain.setTerm(rs.getString("term"));
				domain.setVocabKey(rs.getString("_vocab_key"));
				domain.setAbbreviation(rs.getString("abbreviation"));
				domain.setNote(rs.getString("note"));
				domain.setSequenceNum(rs.getString("sequencenum"));
				domain.setIsObsolete(rs.getString("isobsolete"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
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
	public List<TermFamilyEdgesViewDomain> getTermFamilyEdgesByAccId(String accid) {
		// return TermFamilyEdgesViewDomain by term accid
		
		List<TermFamilyEdgesViewDomain> results = new ArrayList<TermFamilyEdgesViewDomain>();
		
		String cmd = "select * from VOC_TermFamilyEdges_View where searchid = '" + accid + "'";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermFamilyEdgesViewDomain domain = new TermFamilyEdgesViewDomain();	
				domain.setEdgeKey(rs.getString("_edge_key"));
				domain.setChildKey(rs.getString("_child_key"));
				domain.setParentKey(rs.getString("_parent_key"));				
				domain.setSearchid(rs.getString("searchid"));
				domain.setLabel(rs.getString("label"));
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
	
	// Generate query that returns the immediate children of the specified voc_term primary id
	public String getTreeViewChildrenSQL (String accid) {
		String cmd = ""
		    + "\nselect "
		    + "\n  ct._term_key, "
		    + "\n  ct.term, "
		    + "\n  ca.accid,"
		    + "\n  case when exists ("
		    + "\n    select 1 from dag_edge ee "
		    + "\n    where ee._parent_key = cn._node_key and ee._dag_key = cn._dag_key"
		    + "\n  ) then 1 else 0 end as ex"
		    + "\nfrom"
		    + "\n  voc_term pt"
		    + "\n  join acc_accession pa "
		    + "\n    on pt._term_key = pa._object_key"
		    + "\n    and pa._mgitype_key = 13"
		    + "\n    and pa.accid = '" + accid + "'"
		    + "\n  join dag_node pn on pt._term_key = pn._object_key"
		    + "\n  join dag_dag pd on pn._dag_key = pd._dag_key and pd._mgitype_key = 13"
		    + "\n  join dag_edge e on pn._node_key = e._parent_key and e._dag_key = pn._dag_key  "
		    + "\n  join dag_node cn on cn._node_key = e._child_key and e._dag_key = cn._dag_key"
		    + "\n  join voc_term ct on ct._term_key = cn._object_key"
		    + "\n  join acc_accession ca on ct._term_key = ca._object_key"
		    + "\n    and ca._mgitype_key = 13"
		    + "\n    and ca.preferred = 1"
		    + "\norder by ct.term"
		    ;
		return cmd;
	}

	// Returns a JSON formatted list of immediate children on the given term id.
	public String getTreeViewChildren (String accid) {
		String cmd = getTreeViewChildrenSQL(accid);
		log.info(cmd);
		StringBuffer b = new StringBuffer();
		b.append("[");
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			boolean first = true;
			while (rs.next()) {
				if (!first) {
					b.append(",");
				}
				first = false;
				b.append("{");
				b.append("\"label\":\"" + rs.getString("term") + "\"");
				b.append(",");
				b.append("\"termKey\":\"" + rs.getString("_term_key") + "\"");
				b.append(",");
				b.append("\"id\":\"" + rs.getString("accid") + "\"");
				if (rs.getString("ex").equals("1")) {
					b.append(",");
					b.append("\"ex\":true");
				}
				b.append("}");
			}
			sqlExecutor.cleanup();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		b.append("]");
		return b.toString();
	}

	// Returns a list containing the keys of nodes along the default path
	// from the given term to the root.
	public List<String> getDefaultAncestorPath (String accid) {
		List<String> path = new ArrayList<String>();

		// first get the term key and vocab key for the given accid
		String tkey = null;
		String vkey = null;
		try {
			String cmd = "\nselect t._term_key, t._vocab_key "
			+ "\nfrom acc_accession a, voc_term t"
			+ "\nwhere a.accid = '" + accid + "'"
			+ "\nand a._mgitype_key = 13"
			+ "\nand a._object_key = t._term_key"
			;
			log.info(cmd);
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
		    		tkey = rs.getString("_term_key");
				vkey = rs.getString("_vocab_key");
			}
			sqlExecutor.cleanup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// now march up the dag, getting the preferred parent for each node.
		while (tkey != null) {
			// add current term's key to the path
			path.add(tkey);

			// pick a single parent for the current term
			String getParentCmd = "";
			switch (vkey) {
			case "90": // EMAPA
			case "91": // EMAPS:
				// EMAPA and EMAPS have defined way to get preferred parent
				getParentCmd = "\nselect _defaultparent_key as _parent_key"
				+ "\nfrom " + (vkey.equals("90") ? "voc_term_emapa" : "voc_term_emaps")
				+ "\nwhere _term_key = " + tkey;
				break;
			default:
				// default is to return the parent with the min key
				getParentCmd = "\nselect min(p._object_key) as _parent_key"
				+ "\nfrom dag_node n, dag_dag d, dag_edge e, dag_node p"
				+ "\nwhere n._object_key = " + tkey 
				+ "\nand n._dag_key = d._dag_key"
				+ "\nand d._mgitype_key = 13"
				+ "\nand n._node_key = e._child_key"
				+ "\nand e._parent_key = p._node_key"
				;
				break;
			}
			try {
				log.info(getParentCmd);
				String pkey = null; // parent key
				ResultSet rs = sqlExecutor.executeProto(getParentCmd);
				while (rs.next()) {
					pkey = rs.getString("_parent_key");
				}
				sqlExecutor.cleanup();
				// either got a parent key, or this is the root and pkey is null
				// In either case...
				tkey = pkey;
			} catch (Exception e) {
				e.printStackTrace();
				// error - exit loop
				tkey = null;
			}
		}
		//
		return path;
	}

	public String getTreeViewSQL (List<String> ancestorPath) {
		
		StringBuffer b = new StringBuffer();
		int n = 0;
		b.append("with termKeys1(_term_key, _parent_key) as (\n");
		for (String tkey : ancestorPath) {
			n += 1;
			String pkey = n < ancestorPath.size() ? ancestorPath.get(n) : null;
			if (n > 1) b.append(" union ");
			// Need to cast pkey as integer to handle case where the focus node is the root.
			// In that case, pkey is null, and without the cast, postgress infers the column type as text.
			// Which makes the UNION inside termKeys2 (below) complain.
			b.append(" select " + tkey + ", cast(" + pkey + " as int)");
		}
		b.append("\n),");


		String cmd = b.toString()
		// add the immediate children of nodes in termKeys
		+ "\ntermKeys2 as ("
		+ "\n  select _term_key, _parent_key from termKeys1"
		+ "\n  union "
		+ "\n  select distinct cn._object_key as _term_key, p._term_key as _parent_key"
		+ "\n  from termKeys1 p, dag_node pn, dag_dag d, dag_edge e, dag_node cn"
		+ "\n  where p._term_key = pn._object_key"
		+ "\n  and pn._dag_key = d._dag_key"
		+ "\n  and d._mgitype_key = 13"
		+ "\n  and pn._node_key = e._parent_key"
		+ "\n  and e._child_key = cn._node_key"
		+ "\n)"
		// main query - returns (_parent_key, _term_key, term, accid, hasChildren)
		+ "\nselect n._parent_key, t._term_key, t.term, a.accid,"
		+ "\n  case when exists ("
		+ "\n    select 1"
		+ "\n    from dag_closure dc"
		+ "\n    where t._term_key = dc._ancestorobject_key "
		+ "\n    and dc._mgitype_key = 13"
		+ "\n) then 1 else 0 end as hasChildren"
		+ "\nfrom termKeys2 n, voc_term t, acc_accession a"
		+ "\nwhere n._term_key = t._term_key"
		+ "\nand t._term_key = a._object_key"
		+ "\nand a._mgitype_key = 13"
		+ "\nand a.preferred =1 "
		;
		return cmd;
	}

	public static class TreeViewNodeDomainComparator implements Comparator<TreeViewNodeDomain> {
		public int compare(TreeViewNodeDomain a, TreeViewNodeDomain b) {
			return a.getLabel().compareTo(b.getLabel());
		}
	}

	public String getTreeView (String accid) {
		TreeViewNodeDomain rootNode = null;
		// get keys of terms in default path to root.
		List<String> pathKeys = getDefaultAncestorPath(accid);
		// Get treeview data for this path
		String cmd = getTreeViewSQL(pathKeys);
		log.info(cmd);
		try {
			List<TreeViewNodeDomain> tvnList = new ArrayList<TreeViewNodeDomain>();
			Map<String,List<TreeViewNodeDomain>> key2nodes 
			    = new HashMap<String,List<TreeViewNodeDomain>>();
			// first pass - create domain objects and build mappings
			// The same term can appear more than once if it is a child of multiple
			// terms along the path. 
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
			    String tkey = rs.getString("_term_key");
			    String pkey = rs.getString("_parent_key");
			    //
			    TreeViewNodeDomain tvn = new TreeViewNodeDomain();
			    tvn.setTermKey(tkey);
			    tvn.setParentKey(pkey);
			    tvn.setId(rs.getString("accid"));
			    tvn.setLabel(rs.getString("term"));
			    tvn.setEx(rs.getBoolean("hasChildren"));
			    int ki = pathKeys.indexOf(tkey);
			    if (ki >= 0) {
			    	String nxt = (ki+1) < pathKeys.size() ? pathKeys.get(ki+1) : null;
			        if ((pkey == null && nxt == null) || (pkey != null && pkey.equals(nxt))) {
				    // term is on the path and its parent is next on the path. set it to open.
				    tvn.setOc("open");
			        }
			    }

			    if (pkey == null) rootNode = tvn;

			    //
			    tvnList.add(tvn);
			    //
			    if (key2nodes.containsKey(tkey)) {
			    	key2nodes.get(tkey).add(tvn);
			    } else {
			        List<TreeViewNodeDomain> tmp =  new ArrayList<TreeViewNodeDomain>();
				tmp.add(tvn);
				key2nodes.put(tkey, tmp);
			    }
			}
			sqlExecutor.cleanup();			

			// second pass - add each node to its parent's children list
			for (TreeViewNodeDomain tvn : tvnList) {
				String pkey = tvn.getParentKey();
				if (pkey != null) {
					List<TreeViewNodeDomain> ptvnList = key2nodes.get(pkey);
					for (TreeViewNodeDomain ptvn : ptvnList) {
						if (! "open".equals(ptvn.getOc())) continue;
						List <TreeViewNodeDomain> kids = ptvn.getChildren();
						if (kids == null) {
							kids = new ArrayList<TreeViewNodeDomain>();
							ptvn.setChildren(kids);
						}
						kids.add(tvn);
					}
				}
			}

			// third pass - sort siblings by label
			for (TreeViewNodeDomain tvn : tvnList) {
				List<TreeViewNodeDomain> kids = tvn.getChildren();
				if (kids != null) {
					kids.sort(new TreeViewNodeDomainComparator());
				}
			}
		}
		catch (Exception e) {
			log.info("Exception caught: " + e.toString());
			e.printStackTrace();
		}
		// Last pass - generate the json
		String rootJson = rootNode == null ? "" : rootNode.toJson();
		return "[" + rootJson + "]";
	}
}
