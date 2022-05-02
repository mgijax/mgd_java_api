package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceBookDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceBookTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceBookService extends BaseService<ReferenceBookDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceBookDAO bookDAO;

	private ReferenceBookTranslator translator = new ReferenceBookTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ReferenceBookDomain> create(ReferenceBookDomain domain, User user) {
		SearchResults<ReferenceBookDomain> results = new SearchResults<ReferenceBookDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceBookDomain> update(ReferenceBookDomain domain, User user) {
		SearchResults<ReferenceBookDomain> results = new SearchResults<ReferenceBookDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ReferenceBookDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceBookDomain domain = new ReferenceBookDomain();
		if (bookDAO.get(key) != null) {
			domain = translator.translate(bookDAO.get(key));
		}
		bookDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceBookDomain> getResults(Integer key) {
        SearchResults<ReferenceBookDomain> results = new SearchResults<ReferenceBookDomain>();
        results.setItem(translator.translate(bookDAO.get(key)));
        bookDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ReferenceBookDomain> delete(Integer key, User user) {
		SearchResults<ReferenceBookDomain> results = new SearchResults<ReferenceBookDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ReferenceBookDomain> search(Integer key) {

		List<ReferenceBookDomain> results = new ArrayList<ReferenceBookDomain>();

		String cmd = "\nselect _refs_key from bib_book where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ReferenceBookDomain domain = new ReferenceBookDomain();	
				domain = translator.translate(bookDAO.get(rs.getInt("_refs_key")));
				bookDAO.clear();
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
	public Boolean process(String parentKey, ReferenceBookDomain domain, User user) {
		// process reference notes (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processReferenceBook");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if ((domain.getBook_author() == null || domain.getBook_author().isEmpty())
				&& (domain.getBook_title() == null || domain.getBook_title().isEmpty())
				&& (domain.getPlace() == null || domain.getPlace().isEmpty())
				&& (domain.getPublisher() == null || domain.getPublisher().isEmpty())
				&& (domain.getSeries_ed() == null || domain.getSeries_ed().isEmpty())) {
				log.info("processReferenceBook/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {
			log.info("processReferenceBook create");
			ReferenceBook entity = new ReferenceBook();
			entity.set_refs_key(Integer.valueOf(parentKey));
			
			if (domain.getBook_author() == null || domain.getBook_author().isEmpty()) {
				entity.setBook_au(null);
			}
			else {
				entity.setBook_au(domain.getBook_author());
			}
	
			if (domain.getBook_title() == null || domain.getBook_title().isEmpty()) {
				entity.setBook_title(null);
			}
			else {
				entity.setBook_title(domain.getBook_title());
			}
			
			if (domain.getPlace() == null || domain.getPlace().isEmpty()) {
				entity.setPlace(null);
			}
			else {
				entity.setPlace(domain.getPlace());
			}
			
			if (domain.getPublisher() == null || domain.getPublisher().isEmpty()) {
				entity.setPublisher(null);
			}
			else {
				entity.setPublisher(domain.getPublisher());
			}
									
			if (domain.getSeries_ed() == null || domain.getSeries_ed().isEmpty()) {
				entity.setSeries_ed(null);
			}
			else {
				entity.setSeries_ed(domain.getSeries_ed());
			}
			
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			bookDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processReferenceBook delete:" + parentKey);				
			ReferenceBook entity = bookDAO.get(Integer.valueOf(parentKey));				
			bookDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processReferenceBook update:" + parentKey);								
			ReferenceBook entity = bookDAO.get(Integer.valueOf(parentKey));				
			entity.set_refs_key(Integer.valueOf(parentKey));

			if (domain.getBook_author() == null || domain.getBook_author().isEmpty()) {
				entity.setBook_au(null);
			}
			else {
				entity.setBook_au(domain.getBook_author());
			}
	
			if (domain.getBook_title() == null || domain.getBook_title().isEmpty()) {
				entity.setBook_title(null);
			}
			else {
				entity.setBook_title(domain.getBook_title());
			}
			
			if (domain.getPlace() == null || domain.getPlace().isEmpty()) {
				entity.setPlace(null);
			}
			else {
				entity.setPlace(domain.getPlace());
			}
			
			if (domain.getPublisher() == null || domain.getPublisher().isEmpty()) {
				entity.setPublisher(null);
			}
			else {
				entity.setPublisher(domain.getPublisher());
			}
									
			if (domain.getSeries_ed() == null || domain.getSeries_ed().isEmpty()) {
				entity.setSeries_ed(null);
			}
			else {
				entity.setSeries_ed(domain.getSeries_ed());
			}
			
			entity.setModification_date(new Date());
			bookDAO.update(entity);
			modified = true;
			log.info("processReferenceBook/changes processed: " + domain.getRefsKey());
		}
		else {
			log.info("processReferenceBook/no changes processed: " + domain.getRefsKey());
		}
		
		log.info("processReferenceBook/processing successful");
		return modified;
	}
	
}
