package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISetDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISetMemberDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISet;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISetMemberTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISetTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class MGISetService extends BaseService<MGISetDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGISetDAO setDAO;
	@Inject
	private MGISetMemberDAO setMemberDAO;
	@Inject
	private MGISetMemberService setMemberService;
	
	private MGISetTranslator translator = new MGISetTranslator();				
	private MGISetMemberTranslator memberTranslator = new MGISetMemberTranslator();					

	@Transactional
	public SearchResults<MGISetDomain> create(MGISetDomain object, User user) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetDomain> update(MGISetDomain domain, User user) {
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"
		log.info("domain: " + domain.getSetKey());
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		MGISet entity = setDAO.get(Integer.valueOf(domain.getSetKey()));
		Boolean modified = false;

		// process genotype clipboard set member
		if (domain.getGenotypeClipboardMembers() != null ) {		
			if (setMemberService.process(domain.getSetKey(), domain.getGenotypeClipboardMembers(), user)) {
				modified = true;
			}
		}
		// process celltype clipboard set member
		else if(domain.getCelltypeClipboardMembers() != null ) {
			if (setMemberService.process(domain.getSetKey(), domain.getCelltypeClipboardMembers(), user)) {
				log.info("Called getCelltypeClipboard setMemberService");
				modified = true;
			}
		}
		// process emapa clipboard set member
		else if(domain.getEmapaClipboardMembers() != null ) {
			if (setMemberService.process(domain.getSetKey(), domain.getEmapaClipboardMembers(), user)) {
				modified = true;
			}	
		}
		
		// only if modifications were actually made
		if (modified == true) {
			// only make this change if updating the mgi_set fields
			//entity.setModification_date(new Date());
			//entity.setModifiedBy(user);
			setDAO.update(entity);
			log.info("processSet/changes processed: " + domain.getSetKey());
		}
		else {
			log.info("processSet/no changes processed: " + domain.getSetKey());
		}
			
		// return entity translated to domain
		log.info("processSet/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processSet/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<MGISetDomain> delete(Integer key, User user) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGISetDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGISetDomain domain = new MGISetDomain();
		if (setDAO.get(key) != null) {
			domain = translator.translate(setDAO.get(key));
		}
		setDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MGISetDomain> getResults(Integer key) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setItem(translator.translate(setDAO.get(key)));
		setDAO.clear();
		return results;
    }

	public List<MGISetDomain> getBySetUserOrderBy(MGISetDomain searchDomain, String orderByField) {
		// return all set members by _set_key, _createdby_key

		List<MGISetDomain> results = new ArrayList<MGISetDomain>();
		List<MGISetMemberDomain> listOfMembers = new ArrayList<MGISetMemberDomain>();
				
		MGISetDomain domain = new MGISetDomain();
		domain.setSetKey(searchDomain.getSetKey());
		domain.setCreatedBy(searchDomain.getCreatedBy());
		
		String select = "\nselect s.*";
		String from = "\nfrom mgi_setmember s, mgi_user u";
		String where = "\nwhere s._createdby_key = u._user_key" 
				+ "\nand s._set_key = " + searchDomain.getSetKey()
				+ "\nand s._createdby_key = u._user_key"
				+ "\nand u.login = '" + searchDomain.getCreatedBy() + "'";
		String orderBy = "\norder by s." + orderByField;

		if (searchDomain.getSetKey().equals("1046")) {
			select += ", t.term ";
			from += ", voc_term t";
			where += "\nand s._object_key = t._term_key";
		}

		String cmd = select + from + where + orderBy;

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberDomain memberDomain = new MGISetMemberDomain();
				memberDomain = memberTranslator.translate(setMemberDAO.get(rs.getInt("_setmember_key")));
				if (searchDomain.getSetKey().equals("1046")) {
					memberDomain.setLabel(rs.getString("term"));
				}
				setMemberDAO.clear();
				listOfMembers.add(memberDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (domain.getSetKey().equals("1055")) {
			domain.setGenotypeClipboardMembers(listOfMembers);
		}
		else if (domain.getSetKey().equals("1059")) {
			domain.setCelltypeClipboardMembers(listOfMembers);
		}
		else if (domain.getSetKey().equals("1046")) {
			domain.setEmapaClipboardMembers(listOfMembers);
		}	

		results.add(domain);
		return results;
	}

	@Transactional	
	public List<MGISetDomain> getBySetUser(MGISetDomain searchDomain) {
		return getBySetUserOrderBy(searchDomain, "label");
	}
	
	@Transactional	
	public List<MGISetDomain> getBySetUserBySeqNum(MGISetDomain searchDomain) {
		return getBySetUserOrderBy(searchDomain, "sequenceNum");
	}

}
