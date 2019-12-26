package org.jax.mgi.mgd.api.model.img.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleService;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneAssocDAO;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImageDomain;
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
	@Inject
	private AlleleService alleleService;
	
	private ImagePaneAssocTranslator translator = new ImagePaneAssocTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> create(ImagePaneAssocDomain domain, User user) {
		SearchResults<ImagePaneAssocDomain> results = new SearchResults<ImagePaneAssocDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> update(ImagePaneAssocDomain domain, User user) {
		SearchResults<ImagePaneAssocDomain> results = new SearchResults<ImagePaneAssocDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> delete(Integer key, User user) {
		SearchResults<ImagePaneAssocDomain> results = new SearchResults<ImagePaneAssocDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ImagePaneAssocDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ImagePaneAssocDomain domain = new ImagePaneAssocDomain();
		if (imagePaneAssocDAO.get(key) != null) {
			domain = translator.translate(imagePaneAssocDAO.get(key));
		}
		imagePaneAssocDAO.clear();
		return domain;
	}
	
	@Transactional
	public SearchResults<ImagePaneAssocDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ImagePaneAssocDomain> results = new SearchResults<ImagePaneAssocDomain>();
		results.setItem(translator.translate(imagePaneAssocDAO.get(key)));
		imagePaneAssocDAO.clear();
		return results;
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
		// process image pane associations (create, delete, update)
		
		String imagePaneKey = null;
		Boolean modified = false;
		
		log.info("processImagePaneAssoc");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processImagePaneAssoc/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {

			// if parentKey is null, then use object key 
			if (parentKey == null || parentKey.isEmpty()) {
				imagePaneKey = domain.get(i).getImagePaneKey();
			}
			else {
				imagePaneKey = parentKey;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {

				if (imagePaneKey == null || imagePaneKey.isEmpty()) {
					continue;
				}
				
				log.info("processImagePaneAssoc create");
							
				ImagePaneAssoc entity = new ImagePaneAssoc();	
				entity.setImagePane(imagePaneDAO.get(Integer.valueOf(imagePaneKey)));				
				entity.setMgiType(mgiTypeDAO.get(Integer.valueOf(domain.get(i).getMgiTypeKey())));
				entity.set_object_key(Integer.valueOf(domain.get(i).getObjectKey()));
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());

				if (domain.get(i).getIsPrimary() == null || domain.get(i).getIsPrimary().isEmpty()) {
					entity.setIsPrimary(0);
				}
				else {
					entity.setIsPrimary(Integer.valueOf(domain.get(i).getIsPrimary()));
				}			

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
								
				if (!(String.valueOf(entity.getIsPrimary()).equals(domain.get(i).getIsPrimary()))) {
					entity.setIsPrimary(Integer.valueOf(domain.get(i).getIsPrimary()));
					isUpdated = true;
				}
				
				if (isUpdated) {
					log.info("processImagePaneAssoc modified == true");
					entity.setModifiedBy(user);
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

	@Transactional
	public void deleteAlleleAssoc(ImageDomain imageDomain, User user) {
		// delete existing allele/image pane associations
		// called from imageService()

		// image type must = full size
		if (imageDomain.getImageType().equals("Thumbnail")) {
			return;
		}
		
		// image class must = phenotype or molecular		
		if (imageDomain.getImageClass().equals("Expression")) {
			return;
		}
		
		if (imageDomain.getImagePanes().get(0).getPaneAssocs() != null) {
			List<ImagePaneAssocDomain> assocDomain = imageDomain.getImagePanes().get(0).getPaneAssocs();	
			for (int i = 0; i < assocDomain.size(); i++) {
				if (assocDomain.get(i).getMgiTypeKey().equals("11")) {
					log.info("updateAlleleAssoc/delete: " + assocDomain.get(i).getAssocKey());
					ImagePaneAssoc entity = imagePaneAssocDAO.get(Integer.valueOf(assocDomain.get(i).getAssocKey()));
					imagePaneAssocDAO.remove(entity);
				}
			}
		}
	}
	
	@Transactional
	public SearchResults<ImageDomain> updateAlleleAssoc(ImageDomain imageDomain, User user) {
		// update image pane associations for _mgitype_key = 11 (allele)
		// called from imageService()
		
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		String captionNote = "";
		String allelePattern = "\\\\AlleleSymbol\\(([^|)]+)\\|[01]\\)";
		
		log.info("updateAlleleAssoc/start");
		
		// image type must = full size
		if (imageDomain.getImageType().equals("Thumbnail")) {
			return results;
		}
		
		// image class must = phenotype or molecular		
		if (imageDomain.getImageClass().equals("Expression")) {
			return results;
		}
				
		// caption must exist
		if (imageDomain.getCaptionNote() == null) {
			return results;
		}
		
		// caption must not be empty
		if (imageDomain.getCaptionNote().getNoteChunk().isEmpty()) {
			return results;
		}
		
		captionNote = imageDomain.getCaptionNote().getNoteChunk();
		Pattern p;
		p = Pattern.compile(allelePattern);
		Matcher m = p.matcher(captionNote);
		List<String> mgiIds = new ArrayList<String>();
		while (m.find()) {
			if (!mgiIds.contains(m.group(1))) {
				mgiIds.add(m.group(1));
			}
	    }
	    log.info(mgiIds);
    	List<SlimAlleleDomain> aresults = new ArrayList<SlimAlleleDomain>();
    	aresults = alleleService.validateAlleleByMGIIds(mgiIds);

		// add all image pane association objects
		for (int i = 0; i < aresults.size(); i++) {
			log.info("updateAlleleAssoc/create: " + aresults.get(i).getAlleleKey());
			ImagePaneAssoc entity = new ImagePaneAssoc();	
			entity.setImagePane(imagePaneDAO.get(Integer.valueOf(imageDomain.getImagePanes().get(0).getImagePaneKey())));				
			entity.setMgiType(mgiTypeDAO.get(11));
			entity.set_object_key(Integer.valueOf(aresults.get(i).getAlleleKey()));
			entity.setIsPrimary(1);			
			entity.setCreatedBy(user);
			entity.setCreation_date(new Date());
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());
			imagePaneAssocDAO.persist(entity);
		}
		
		log.info("updateAlleleAssoc/end");
		return results;
	}

}
