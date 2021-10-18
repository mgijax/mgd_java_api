package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.InSituResultImageDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultImageDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultImageViewDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultImage;
import org.jax.mgi.mgd.api.model.gxd.translator.InSituResultImageTranslator;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class InSituResultImageService extends BaseService<InSituResultImageDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private InSituResultImageDAO insituimageDAO;
	@Inject
	private ImagePaneDAO imagePaneDAO;

	private InSituResultImageTranslator translator = new InSituResultImageTranslator();				

	@Transactional
	public SearchResults<InSituResultImageDomain> create(InSituResultImageDomain domain, User user) {
		SearchResults<InSituResultImageDomain> results = new SearchResults<InSituResultImageDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultImageDomain> update(InSituResultImageDomain domain, User user) {
		SearchResults<InSituResultImageDomain> results = new SearchResults<InSituResultImageDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultImageDomain> delete(Integer key, User user) {
		SearchResults<InSituResultImageDomain> results = new SearchResults<InSituResultImageDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public InSituResultImageDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		InSituResultImageDomain domain = new InSituResultImageDomain();
		if (insituimageDAO.get(key) != null) {
			domain = translator.translate(insituimageDAO.get(key));
		}
		insituimageDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<InSituResultImageDomain> getResults(Integer key) {
		SearchResults<InSituResultImageDomain> results = new SearchResults<InSituResultImageDomain>();
		results.setItem(translator.translate(insituimageDAO.get(key)));
		insituimageDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<InSituResultImageViewDomain> domain, User user) {
		// process in situ result image panes (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processInSituResultsImage/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if result is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
			if (domain.get(i).getImagePaneKey() == null || domain.get(i).getImagePaneKey().isEmpty()) {
				continue;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processInSituResultsImage create");

				InSituResultImage entity = new InSituResultImage();

				entity.set_result_key(parentKey);
				entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.get(i).getImagePaneKey())));				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				
				insituimageDAO.persist(entity);
				
				modified = true;
				log.info("processInSituResultsImage/create processed: " + entity.get_resultimage_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processInSituResultsImage delete");
				InSituResultImage entity = insituimageDAO.get(Integer.valueOf(domain.get(i).getResultImageKey()));
				insituimageDAO.remove(entity);
				modified = true;
				log.info("processInSituResultsImage delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processInSituResultsImage update");

				InSituResultImage entity = insituimageDAO.get(Integer.valueOf(domain.get(i).getResultImageKey()));
				
				entity.set_result_key(parentKey);
				entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.get(i).getImagePaneKey())));
				entity.setModification_date(new Date());
				
				insituimageDAO.update(entity);
				modified = true;
				log.info("processInSituResultsImage/changes processed: " + domain.get(i).getResultImageKey());	
			}
			else {
				log.info("processInSituResultsImage/no changes processed: " + domain.get(i).getResultImageKey());
			}
		}
		
		log.info("processInSituResultsImage/processing successful");
		return modified;
	}

}
