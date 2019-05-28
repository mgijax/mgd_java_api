package org.jax.mgi.mgd.api.model.img.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneAssocDAO;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssoc;
import org.jax.mgi.mgd.api.model.img.translator.ImagePaneAssocTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ImagePaneAssocService extends BaseService<ImagePaneAssocDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ImagePaneAssocDAO imagePaneAssocDAO;
	@Inject
	private ImagePaneDAO imagePaneDAO;
	@Inject
	private MGITypeDAO mgiTypeDAO;
	
	private ImagePaneAssocTranslator translator = new ImagePaneAssocTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> create(ImagePaneAssocDomain domain, User user) {
		// not used
		return null;
	}
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> update(ImagePaneAssocDomain domain, User user) {
		// not used
		return null;
	}

	@Transactional
	public ImagePaneAssocDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ImagePaneAssocDomain domain = new ImagePaneAssocDomain();
		if (imagePaneAssocDAO.get(key) != null) {
			domain = translator.translate(imagePaneAssocDAO.get(key));
		}
		return domain;
	}

	@Transactional
	public SearchResults<ImagePaneAssocDomain> getResults(Integer key) {
		// not used
		return null;
	}
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> delete(Integer key, User user) {
		// not used
		return null;
	}

	@Transactional	
	public List<ImagePaneAssocDomain> search(Integer key) {
		// using searchDomain fields, generate SQL command
		
		List<ImagePaneAssocDomain> results = new ArrayList<ImagePaneAssocDomain>();

		String cmd = "\nselect * from img_imagepane_assoc"
				+ "\nwhere _imagepane_key = " + key
				+ "\norder by _assoc_key";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ImagePaneAssocDomain domain = new ImagePaneAssocDomain();	
				domain = translator.translate(imagePaneAssocDAO.get(rs.getInt("_assoc_key")));
				imagePaneAssocDAO.clear();
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
	public Boolean process(String parentKey, List<ImagePaneAssocDomain> domain, User user) {
		// process image pane (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processImagePaneAssoc");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processImagePaneAssoc/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processImagePaneAssoc create");
				ImagePaneAssoc entity = new ImagePaneAssoc();	
				entity.setImagePane(imagePaneDAO.get(Integer.valueOf(parentKey)));				
				entity.setMgiType(mgiTypeDAO.get(Integer.valueOf(domain.get(i).getMgiTypeKey())));
				entity.set_object_key(Integer.valueOf(get(i).getObjectKey()));
				entity.setIsPrimary(domain.get(i).getIsPrimary());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				imagePaneAssocDAO.persist(entity);
				modified = true;
				log.info("processImagePaneAssoc/create/returning results");					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processImagePaneAssoc delete");
				ImagePaneAssoc entity = imagePaneAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				imagePaneAssocDAO.remove(entity);
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processImagePaneAssoc update");
				Boolean isUpdated = false;
				ImagePaneAssoc entity = imagePaneAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
								
				if (!entity.getIsPrimary().equals(domain.get(i).getIsPrimary())) {
					entity.setIsPrimary(domain.get(i).getIsPrimary());
					isUpdated = true;
				}
				
				if (isUpdated) {
					log.info("processImagePaneAssoc modified == true");
					entity.setModification_date(new Date());
					imagePaneAssocDAO.update(entity);
					modified = true;
					log.info("processImagePaneAssoc/changes processed: " + domain.get(i).getAssocKey());
				}
				else {
					log.info("processImagePaneAssoc/no changes processed: " + domain.get(i).getAssocKey());
				}
			}
			else {
				log.info("processImagePaneAssoc/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processImagePaneAssoc/processing successful");
		return modified;
	}
	
}
