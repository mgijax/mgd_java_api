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
import org.jax.mgi.mgd.api.model.img.domain.GXDImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImagePaneDomain;
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
		SearchResults<ImagePaneDomain> results = new SearchResults<ImagePaneDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ImagePaneDomain> update(ImagePaneDomain domain, User user) {
		SearchResults<ImagePaneDomain> results = new SearchResults<ImagePaneDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ImagePaneDomain> delete(Integer key, User user) {
		SearchResults<ImagePaneDomain> results = new SearchResults<ImagePaneDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ImagePaneDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ImagePaneDomain domain = new ImagePaneDomain();
		if (imagePaneDAO.get(key) != null) {
			domain = translator.translate(imagePaneDAO.get(key));
		}
		imagePaneDAO.clear();
		return domain;
	}

	@Transactional
	public SearchResults<ImagePaneDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ImagePaneDomain> results = new SearchResults<ImagePaneDomain>();
		results.setItem(translator.translate(imagePaneDAO.get(key)));
		imagePaneDAO.clear();
		return results;
	}

	@Transactional	
	public List<ImagePaneDomain> search(Integer key) {
		// using searchDomain fields, generate SQL command
		
		List<ImagePaneDomain> results = new ArrayList<ImagePaneDomain>();

		String cmd = "\nselect * from img_imagepane"
				+ "\nwhere _imagepane_key = " + key
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
	public List<GXDImagePaneDomain> getGXDByReference(Integer key) {
		// return list of full size image panes by reference (_refs_key)
		// class = Expression
		// type = Full Size
	
		List<GXDImagePaneDomain> results = new ArrayList<GXDImagePaneDomain>();

		// skip
/*		 _refs_key |  jnumid  |                          short_citation
		 -----------+----------+-------------------------------------------------------------------
		      94290 | J:93300  | Blackshaw S, PLoS Biol 2004 Oct;2(9):E247
		     102744 | J:101679 | Deltagen Inc, MGI Direct Data Submission 2005;():
		     154591 | J:153498 | Diez-Roux G, PLoS Biol 2011;9(1):e1000582
		      81462 | J:80501  | Gitton Y, Nature 2002 Dec 5;420(6915):586-90
		      92242 | J:91257  | Gray PA, Science 2004 Dec 24;306(5705):2255-2257
		     172505 | J:171409 | GUDMAP Consortium, www.gudmap.org 2004;():
		     141558 | J:140465 | Guo G, Dev Cell 2010 Apr 20;18(4):675-85
		     229658 | J:228563 | Koscielny G, Nucleic Acids Res 2014 Jan;42(Database issue):D802-9
		     227123 | J:226028 | Lewandowski JP, Dev Biol 2015 Oct 1;406(1):92-103
		     163316 | J:162220 | Magdaleno S, PLoS Biol 2006 Apr;4(4):e86
		     372824 | J:279207 | Mager J, MGI Direct Data Submission 2019;():
		      81463 | J:80502  | Reymond A, Nature 2002 Dec 5;420(6915):582-6
		     158912 | J:157819 | Shimogori T, Nat Neurosci 2010;13(6):767-75
		      86101 | J:85124  | Sousa-Nunes R, Genome Res 2003 Dec;13(12):2609-20
		     142384 | J:141291 | Tamplin OJ, BMC Genomics 2008;9(1):511
		     144871 | J:143778 | Tamplin OJ, Dev Biol 2011 Dec 15;360(2):415-25
		     216584 | J:215487 | Thompson CL, Neuron 2014 Jul 16;83(2):309-23
		     124081 | J:122989 | Visel A, Nucleic Acids Res 2004 Jan 1;32(Database issue):D552-6
		 (18 rows)
*/
	
		String cmd = "\nselect p._imagepane_key, concat(i.figureLabel,p.paneLabel) as figurePaneLabel"
				+ "\nfrom img_imagepane p, img_image i"
				+ "\nwhere i._refs_key = " + key
				+ "\nand i._ImageClass_key = 6481781"
				+ "\nand i._ImageType_key = 1072158"
				+ "\nand i._image_key = p._image_key"	
				+ "\nand i._refs_key not in (81462,81463,86101,92242,94290,102744,124081,141558,142384,144871,154591,158912,"
			    + 		"163316,172505,216584,227123,229658,372824)"
				+ "\norder by figurePaneLabel";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				GXDImagePaneDomain domain = new GXDImagePaneDomain();
				domain.setImagePaneKey(rs.getString("_imagepane_key"));
				domain.setFigurePaneLabel(rs.getString("figurePaneLabel"));
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
				
			// null paneLabel only applicable for 1st paneLabel
			// else, do nothing
			if (i > 0 && (domain.get(i).getPaneLabel() == null 
					|| domain.get(i).getPaneLabel().isEmpty())) {
				continue;
			}
			
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
				ImagePane entity = imagePaneDAO.get(Integer.valueOf(domain.get(i).getImagePaneKey()));

				if (domain.get(i).getPaneLabel() == null || domain.get(i).getPaneLabel().isEmpty()) {
					entity.setPaneLabel(null);
				}	
				else {
					entity.setPaneLabel(domain.get(i).getPaneLabel());
				}

				entity.setModification_date(new Date());
				imagePaneDAO.update(entity);
				modified = true;
				log.info("processImagePane/changes processed: " + domain.get(i).getImagePaneKey());
			}
			else {
				log.info("processImagePane/no changes processed: " + domain.get(i).getImagePaneKey());
			}
		}
		
		log.info("processImagePane/processing successful");
		return modified;
	}

	@Transactional	
	public List<SlimImagePaneDomain> validateImagePane(SlimImagePaneDomain searchDomain) {
		// use SlimImagePaneDomain to return list of validated image panes
		// returns empty list of values if validation fails

		List<SlimImagePaneDomain> results = new ArrayList<SlimImagePaneDomain>();

		String cmd = "\nselect p._imagepane_key, i.figureLabel, a1.accID as mgiID, a2.accID as pixID, t2.term as imageClass"
				+ "\nfrom IMG_ImagePane p, IMG_Image i, ACC_Accession a1, ACC_Accession a2, VOC_Term t1, VOC_Term t2"
				+ "\nwhere p._Image_key = i._Image_key" 
				+ "\nand p._Image_key = a1._Object_key" 
				+ "\nand a1._MGIType_key = 9"
				+ "\nand p._Image_key = a2._Object_key"
				+ "\nand a2._MGIType_key = 9"
				+ "\nand a2._LogicalDB_key = 19"
				+ "\nand i._ImageType_key = t1._Term_key"
				+ "\nand t1.term = 'Full Size'"
				+ "\nand i._ImageClass_key = t2._Term_key";

		if (searchDomain.getMgiID() != null && !searchDomain.getMgiID().isEmpty()) {
			String mgiID = searchDomain.getMgiID().replace("mgi", "MGI");
			cmd = cmd + "\nand a1.accID = '" + mgiID + "'";
		}
		
		if (searchDomain.getPixID() != null && !searchDomain.getPixID().isEmpty()) {
			String pixID = searchDomain.getPixID().replace("pix", "PIX");
			cmd = cmd + "\nand a2.accID = '" + pixID + "'";
		}

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimImagePaneDomain domain = new SlimImagePaneDomain();							
				domain.setImagePaneKey(rs.getString("_imagepane_key"));
				domain.setFigureLabel(rs.getString("figureLabel"));
				domain.setImageClass(rs.getString("imageClass"));
				domain.setMgiID(rs.getString("mgiID"));
				domain.setPixID(rs.getString("pixID"));				
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
