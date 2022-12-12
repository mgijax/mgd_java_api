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
import org.jax.mgi.mgd.api.model.img.domain.SummaryImagePaneDomain;
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
		
		String cmd = "\nselect i._refs_key, p._imagepane_key, concat(figureLabel,paneLabel) as figurepaneLabel "
				+ "\nfrom img_imagepane p, img_image i"
				+ "\nwhere i._refs_key = " + key
				+ "\nand i._imageclass_key = 6481781"
				+ "\nand i._imagetype_key = 1072158"
				+ "\nand i._image_key = p._image_key"
				+ "\norder by figurepaneLabel"
				+ "\nlimit 2500";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				GXDImagePaneDomain domain = new GXDImagePaneDomain();
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setImagePaneKey(rs.getString("_imagepane_key"));
				domain.setFigurepaneLabel(rs.getString("figurepaneLabel"));
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
	public List<SummaryImagePaneDomain> getSummaryByReference(Integer key) {
		// return list of full size image panes by reference (_refs_key)
		// class = Expression
		// type = Full Size
	
		List<SummaryImagePaneDomain> results = new ArrayList<SummaryImagePaneDomain>();
		
		String cmd = "\n(select i._refs_key, c.jnumid, p._imagepane_key, i.figureLabel, p.paneLabel, s.specimenLabel,"
				+ "\na1.accid as imageid, a2.accid as assayid,"
				+ "\na3.accid as markerid, m.symbol,"
				+ "\nt.assayType"
				+ "\nfrom bib_citation_cache c, img_imagepane p, img_image i,"
				+ "\ngxd_insituresultimage gri, gxd_insituresult gr, gxd_specimen s, gxd_assay a, gxd_assaytype t,"
				+ "\nacc_accession a1, acc_accession a2,"
				+ "\nacc_accession a3, mrk_marker m"
				+ "\nwhere c._refs_key = " + key
				+ "\nand c._refs_key = i._refs_key"
				+ "\nand i._imageclass_key = 6481781"
				+ "\nand i._imagetype_key = 1072158"
				+ "\nand i._image_key = p._image_key"
				+ "\nand p._imagepane_key = gri._imagepane_key"
				+ "\nand gri._result_key = gr._result_key"
				+ "\nand gr._specimen_key = s._specimen_key"
				+ "\nand s._assay_key = a._assay_key"
				+ "\nand a._assaytype_key = t._assaytype_key"
				+ "\nand i._image_key = a1._object_key"
				+ "\nand a1._mgitype_key = 9"
				+ "\nand a1._logicaldb_key = 1"
				+ "\nand a._assay_key = a2._object_key"
				+ "\nand a2._mgitype_key = 8"
				+ "\nand a2._logicaldb_key = 1"
				+ "\nand a._marker_key = a3._object_key"
				+ "\nand a3._mgitype_key = 2"
				+ "\nand a3._logicaldb_key = 1"
				+ "\nand a3.preferred = 1"
				+ "\nand a._marker_key = m._marker_key"
				+ "\nunion"
				+ "\nselect i._refs_key, c.jnumid, p._imagepane_key, i.figureLabel, p.paneLabel, null,"
				+ "\na1.accid as imageid, a2.accid as assayid,"
				+ "\na3.accid as markerid, m.symbol,"
				+ "\nt.assayType"
				+ "\nfrom bib_citation_cache c, img_imagepane p, img_image i,"
				+ "\ngxd_assay a, gxd_assaytype t,"
				+ "\nacc_accession a1, acc_accession a2,"
				+ "\nacc_accession a3, mrk_marker m"
				+ "\nwhere c._refs_key = " + key
				+ "\nand c._refs_key = i._refs_key"
				+ "\nand i._imageclass_key = 6481781"				
				+ "\nand i._imagetype_key = 1072158"
				+ "\nand i._image_key = p._image_key"
				+ "\nand p._imagepane_key = a._imagepane_key"
				+ "\nand a._assaytype_key = t._assaytype_key"
				+ "\nand i._image_key = a1._object_key"
				+ "\nand a1._mgitype_key = 9"
				+ "\nand a1._logicaldb_key = 1"
				+ "\nand a._assay_key = a2._object_key"
				+ "\nand a2._mgitype_key = 8"
				+ "\nand a2._logicaldb_key = 1"
				+ "\nand a._marker_key = a3._object_key"
				+ "\nand a3._mgitype_key = 2"
				+ "\nand a3._logicaldb_key = 1"
				+ "\nand a3.preferred = 1"
				+ "\nand a._marker_key = m._marker_key"
				+ "\n)order by figureLabel, paneLabel";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryImagePaneDomain domain = new SummaryImagePaneDomain();
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setImagePaneKey(rs.getString("_imagepane_key"));
				domain.setJnumID(rs.getString("jnumID"));
				domain.setFigureLabel(rs.getString("figureLabel"));
				domain.setPaneLabel(rs.getString("paneLabel"));
				domain.setSpecimenLabel(rs.getString("specimenLabel"));
				domain.setImageID(rs.getString("imageid"));
				domain.setAssayID(rs.getString("assayid"));
				domain.setMarkerID(rs.getString("markerid"));
				domain.setMarkerSymbol(rs.getString("symbol"));
				domain.setAssayType(rs.getString("assaytype"));
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
