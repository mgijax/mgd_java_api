package org.jax.mgi.mgd.api.model.img.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.img.dao.ImageDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.translator.ImageSubmissionTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ImageSubmissionService extends BaseService<ImageSubmissionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ImageDAO imageDAO;

	private ImageSubmissionTranslator translator = new ImageSubmissionTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ImageSubmissionDomain> create(ImageSubmissionDomain domain, User user) {
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}
	
	
	@Transactional
	public SearchResults<ImageSubmissionDomain> update(ImageSubmissionDomain domain, User user) {
		// call stored procedure IMG_setPDO()
		// 1: associate pix id with image (via acc_accession)
		// 2: update the img_image.xdim, ydim

		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		
		log.info("processImage/create");

		String cmd = "select count(*) from IMG_setPDO ("
				+ user.get_user_key().intValue()
				+ ")";
			
		log.info("cmd: " + cmd);
		Query query = imageDAO.createNativeQuery(cmd);
		query.getResultList();
			
		log.info("processImage/createPixAssociation/returning results");
		results = getResults(Integer.valueOf(results.items.get(0).getImageKey()));
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
		return domain;
	}

	@Transactional
	public SearchResults<ImageSubmissionDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results.setItem(translator.translate(imageDAO.get(key)));
		return results;
	}
	
	@Transactional	
	public List<ImageSubmissionDomain> search(ImageSubmissionDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		// return full size images only
		
		List<ImageSubmissionDomain> results = new ArrayList<ImageSubmissionDomain>();

		// building SQL command : select + from + where + orderBy
		String cmd = "";
		String select = "select distinct i._image_key, i.figureLabel, i.imageType";
		String from = "from img_image_view i";
		String where = "where i._imagetype_key = 1072158";
		String orderBy = "order by i.figureLabel, i.imageType";
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

}
