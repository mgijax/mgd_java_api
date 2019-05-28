package org.jax.mgi.mgd.api.model.img.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;
import org.jax.mgi.mgd.api.model.img.translator.ImagePaneTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ImagePaneService extends BaseService<ImagePaneDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ImagePaneDAO imagePaneDAO;
	
	private ImagePaneTranslator translator = new ImagePaneTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ImagePaneDomain> create(ImagePaneDomain domain, User user) {
		// not used
		return null;
	}
	
	@Transactional
	public SearchResults<ImagePaneDomain> update(ImagePaneDomain domain, User user) {
		// not used
		return null;
	}

	@Transactional
	public ImagePaneDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ImagePaneDomain domain = new ImagePaneDomain();
		if (imagePaneDAO.get(key) != null) {
			domain = translator.translate(imagePaneDAO.get(key));
		}
		return domain;
	}

	@Transactional
	public SearchResults<ImagePaneDomain> getResults(Integer key) {
		// not used
		return null;
	}
	
	@Transactional
	public SearchResults<ImagePaneDomain> delete(Integer key, User user) {
		// not used
		return null;
	}

	@Transactional	
	public List<ImagePaneDomain> search(Integer key) {
		// using searchDomain fields, generate SQL command
		
		List<ImagePaneDomain> results = new ArrayList<ImagePaneDomain>();

		String cmd = "\nselect * from img_imagepane"
				+ "\nwhere _image_key = " + key
				+ "\norder by _imagepane_key";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ImagePaneDomain domain = new ImagePaneDomain();	
				domain = translator.translate(imagePaneDAO.get(rs.getInt("_imagepane_key")));
				imagePaneDAO.clear();
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
	public Boolean process(String parentKey, List<ImagePaneDomain> domain, User user) {
		// process image pane (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processImagePane");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processImagePane/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processImagePane create");
				ImagePane entity = new ImagePane();	
				entity.set_image_key(Integer.valueOf(parentKey));
				entity.setPaneLabel(domain.get(i).getPaneLabel());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				imagePaneDAO.persist(entity);
				modified = true;
				log.info("processImagePane/create/returning results");					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processImagePane delete");
				ImagePane entity = imagePaneDAO.get(Integer.valueOf(domain.get(i).getImagePaneKey()));
				imagePaneDAO.remove(entity);
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processImagePane update");
				Boolean isUpdated = false;
				ImagePane entity = imagePaneDAO.get(Integer.valueOf(domain.get(i).getImagePaneKey()));

				if (entity.getPaneLabel() == null && 
						(domain.get(i).getPaneLabel() != null || !domain.get(i).getPaneLabel().isEmpty())) {
					entity.setPaneLabel(domain.get(i).getPaneLabel());
					isUpdated = true;
				}
				else if (entity.getPaneLabel() != null && 
						(domain.get(i).getPaneLabel() == null || domain.get(i).getPaneLabel().isEmpty())) {
					entity.setPaneLabel(null);
					isUpdated = true;
				}
				else if (!entity.getPaneLabel().equals(domain.get(i).getPaneLabel())) {
					entity.setPaneLabel(domain.get(i).getPaneLabel());
					isUpdated = true;
				}
				
				if (isUpdated) {
					log.info("processImagePane modified == true");
					entity.setModification_date(new Date());
					imagePaneDAO.update(entity);
					modified = true;
					log.info("processImagePane/changes processed: " + domain.get(i).getImagePaneKey());
				}
				else {
					log.info("processImagePane/no changes processed: " + domain.get(i).getImagePaneKey());
				}
			}
			else {
				log.info("processImagePane/no changes processed: " + domain.get(i).getImagePaneKey());
			}
		}
		
		log.info("processImagePane/processing successful");
		return modified;
	}
	
}
