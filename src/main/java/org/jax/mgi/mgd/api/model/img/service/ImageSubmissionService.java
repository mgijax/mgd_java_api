package org.jax.mgi.mgd.api.model.img.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.img.dao.ImageDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.translator.ImageSubmissionTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class ImageSubmissionService extends BaseService<ImageSubmissionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ImageDAO imageDAO;

	private ImageSubmissionTranslator translator = new ImageSubmissionTranslator();

	@Transactional
	public SearchResults<ImageSubmissionDomain> create(ImageSubmissionDomain domain, User user) {
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}
	
	@Transactional
	public SearchResults<ImageSubmissionDomain> update(ImageSubmissionDomain domain, User user) {
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;	
	}
		
	@Transactional
	public SearchResults<ImageSubmissionDomain> delete(Integer key, User user) {
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ImageSubmissionDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ImageSubmissionDomain domain = new ImageSubmissionDomain();
		if (imageDAO.get(key) != null) {
			domain = translator.translate(imageDAO.get(key));
		}
		imageDAO.clear();
		return domain;
	}

	@Transactional
	public SearchResults<ImageSubmissionDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results.setItem(translator.translate(imageDAO.get(key)));
		imageDAO.clear();
		return results;
	}

	@Transactional	
	public List<ImageSubmissionDomain> search(ImageSubmissionDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		// return full size images only
		
		List<ImageSubmissionDomain> results = new ArrayList<ImageSubmissionDomain>();

		// building SQL command : select + from + where + orderBy
		String cmd = "";
		String select = "select distinct i._image_key, i.figureLabel, i.imageClass";
		String from = "from img_image_view i";
		String where = "where i._image_key is not null";
		String orderBy = "order by i.figureLabel, i.imageClass";
		String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
	
		// image reference
		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand i._Refs_key = " + searchDomain.getRefsKey();
		}
		else if (searchDomain.getJnumid() != null && !searchDomain.getJnumid().isEmpty()) {
			String jnumid = searchDomain.getJnumid().toUpperCase();
			if (!jnumid.contains("J:")) {
				jnumid = "J:" + jnumid;
			}
			where = where + "\nand i.jnumid = '" + jnumid + "'";
		}
		if (searchDomain.getShort_citation() != null && !searchDomain.getShort_citation().isEmpty()) {
			value = searchDomain.getShort_citation().replace("'",  "''");
			where = where + "\nand i.short_citation ilike '" + value + "'";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ImageSubmissionDomain domain = new ImageSubmissionDomain();
				domain = translator.translate(imageDAO.get(rs.getInt("_image_key")));
				imageDAO.clear();
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
	public SearchResults<ImageSubmissionDomain> submit(MultipartFormDataInput input) {
		// submit chosen files to pixeldb folder (in swarm)
		// call stored procedure to attach files (pixid) to image stub
		// and to set xdim, ydim coordinates
		
		log.info("imageSubmission/submit/begin");
		
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();		
		Map<String, List<InputPart>> form = input.getFormDataMap();

		for (String key: form.keySet()) {

			log.info("imageSubmission/submit/key: " + key);
			
			if (key.startsWith("imageKey_")) {
				
				try {
					
					String[] array = key.split("_");
					String imageKey = array[1];
					
					// open pixeldb counter
					BufferedReader inCounter = new BufferedReader(new FileReader(pixeldbCounter));
					String nextPixKey = inCounter.readLine();
					String imageFile = pixeldb + "/" + nextPixKey + ".jpg";
					inCounter.close();
										
					// save file to pixeldb directory
					log.info("imageSubmission/submit: fileUtils/copy/begin");
					InputPart inputPart = form.get("file_" + imageKey).get(0);
					File outPix = new File(imageFile);
					log.info("imageSubmission/submit: fileUtils/copy/file: " + outPix.getAbsolutePath());

					// save as InputStream
					InputStream is = inputPart.getBody(InputStream.class, null);
					FileUtils.copyInputStreamToFile(is, outPix);
					log.info("imageSubmission/submit: fileUtils/copy/end");
				
					// update pixeldb counter
					log.info("imageSubmission/submit: pixeldb counter/begin");
					Integer newPixKey = (Integer.valueOf(nextPixKey)) + 1;
					BufferedWriter outCounter = new BufferedWriter(new FileWriter(pixeldbCounter));
					outCounter.append(String.valueOf(newPixKey) + "\n");
					outCounter.close();
					log.info("imageSubmission/submit: pixeldb counter/end");

					// call stored procedure IMG_setPDO()
					// 1: associate pix id with image (via acc_accession)
					// 2: update the img_image.xdim, ydim
					log.info("imageSubmission/submit: dimensions/begin");
					BufferedImage bi = ImageIO.read(outPix);				
					String cmd = "select count(*) from IMG_setPDO (" 
						+ nextPixKey + "," 
						+ bi.getWidth() + "," 
						+ bi.getHeight() + ","
						+ imageKey + ")";
					log.info("cmd: " + cmd);
					Query query = imageDAO.createNativeQuery(cmd);
					query.getResultList();
					log.info("imageSubmission/submit: dimensions/end");
				
				} catch (Exception e) {
					results.setError(Constants.LOG_FAIL_IMAGESUBMISSION, null, Constants.HTTP_SERVER_ERROR);					
					e.printStackTrace();
				}
			}
		}
		
		log.info("imageSubmission/submit/end");
		return results;
	}	
}
