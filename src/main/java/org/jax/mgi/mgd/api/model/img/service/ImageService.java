package org.jax.mgi.mgd.api.model.img.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.img.dao.ImageDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImageDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jax.mgi.mgd.api.model.img.translator.ImageSubmissionTranslator;
import org.jax.mgi.mgd.api.model.img.translator.ImageTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ImageService extends BaseService<ImageDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ImageDAO imageDAO;
	@Inject
	private ImageDAO thumbnailDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	
	@Inject
	private NoteService noteService;
	@Inject
	private ImagePaneService imagePaneService;
	@Inject
	private ImagePaneAssocService imagePaneAssocService;
	@Inject
	private AccessionService accessionService;
	
	private ImageTranslator translator = new ImageTranslator();
	private ImageSubmissionTranslator submissionTranslator = new ImageSubmissionTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "9";
	private String fullSizeImageKey = "1072158";
	private String thumbnailImageKey = "1072159";
	private String expressionClassKey = "6481781";
	private String phenotypeClassKey = "6481782";
	
	@Transactional
	public SearchResults<ImageDomain> create(ImageDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// thumbnailEntity will only be created if necessary
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		Image entity = new Image();
		
		log.info("processImage/create");
		
		// copyright/DXDOI validation
		//if (domain.getCopyrightNote() != null) {
		//	if (domain.getCopyrightNote().getNoteChunk().contains("DXDOI(||)")) {
		//		log.info("processImage/create/DXDOI missing");
		//		results.setError("Failed : Copyright/DXDOI error", "Copyright is missing the DXDOI identifier", Constants.HTTP_SERVER_ERROR);
		//		return results;		
		//	}
		//}
		
		// if null, default = Phenotype
		if (domain.getImageClassKey() == null || domain.getImageClassKey().isEmpty()) {
			domain.setImageClassKey(phenotypeClassKey);
		}
		
		// if A&P (not Expression), then create Thumbnail too
		if (!domain.getImageClassKey().equals(expressionClassKey)) {
			Image thumbnailEntity = new Image();
			thumbnailEntity.setImageType(termDAO.get(Integer.valueOf(thumbnailImageKey)));
			thumbnailEntity.setImageClass(termDAO.get(Integer.valueOf(domain.getImageClassKey())));
			thumbnailEntity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
			thumbnailEntity.setFigureLabel(domain.getFigureLabel());
			// thumbnailKey is null
			thumbnailEntity.setCreatedBy(user);
			thumbnailEntity.setCreation_date(new Date());
			thumbnailEntity.setModifiedBy(user);
			thumbnailEntity.setModification_date(new Date());
			log.info("processImage/create/thumbnailDAO");
			thumbnailDAO.persist(thumbnailEntity);
			
			// thumbnail image pane
			imagePaneService.process(String.valueOf(thumbnailEntity.get_image_key()), domain.getImagePanes(), user);
			
			// thumbnail copyright
			noteService.process(String.valueOf(thumbnailEntity.get_image_key()), domain.getCopyrightNote(), mgiTypeKey, "1023", user);

			// set full-size thumbail key = new thumbnail primary key
			entity.setThumbnailImage(thumbnailDAO.get(thumbnailEntity.get_image_key()));
		}
		
		// create Full Size (1072158)		
		entity.setImageClass(termDAO.get(Integer.valueOf(domain.getImageClassKey())));			
		entity.setImageType(termDAO.get(Integer.valueOf(fullSizeImageKey)));
		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
		entity.setFigureLabel(domain.getFigureLabel()); 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		log.info("processImage/create/imageDAO");
		imageDAO.persist(entity);
		
		// process all notes
		log.info("processImage/notes");
		noteService.process(String.valueOf(entity.get_image_key()), domain.getCaptionNote(), mgiTypeKey, "1024", user);
		noteService.process(String.valueOf(entity.get_image_key()), domain.getCopyrightNote(), mgiTypeKey, "1023", user);
		noteService.process(String.valueOf(entity.get_image_key()), domain.getPrivateCuratorialNote(), mgiTypeKey, "1025", user);
		noteService.process(String.valueOf(entity.get_image_key()), domain.getExternalLinkNote(), mgiTypeKey, "1039", user);

		// process image pane
		log.info("processImage/pane label");
		imagePaneService.process(String.valueOf(entity.get_image_key()), domain.getImagePanes(), user);
			
		// process image pane associations
		log.info("processImage/pane associations");
		for (int i = 0; i < domain.getImagePanes().size(); i++) {
			imagePaneAssocService.process(domain.getImagePanes().get(i).getImagePaneKey(), domain.getImagePanes().get(i).getPaneAssocs(), user);
		}
		
		// return entity translated to domain
		log.info("processImage/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<ImageDomain> update(ImageDomain domain, User user) {

		// update existing entity object from in-coming domain
		
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		Image entity = imageDAO.get(Integer.valueOf(domain.getImageKey()));
		Image thumbnailEntity = new Image();
		Boolean modified = false;
		String mgiTypeName = "Image";
		
		log.info("processImage/update");

		// may have thumbnail
		if (domain.getImageTypeKey().equals(fullSizeImageKey) 
				&& domain.getThumbnailImage() != null) {
			thumbnailEntity = imageDAO.get(Integer.valueOf(domain.getThumbnailImage().getImageKey()));
		}
		
		// copyright/DXDOI validation
		if (domain.getCopyrightNote() != null) {
			if (domain.getCopyrightNote().getNoteChunk().contains("DXDOI(||)")) {
				log.info("processImage/update/DXDOI missing");
				results.setError("Failed : Copyright/DXDOI error", "Copyright is missing the DXDOI identifier", Constants.HTTP_SERVER_ERROR);
				return results;	
			}
		}
		
		if (!String.valueOf(entity.getReference().get_refs_key()).equals(domain.getRefsKey())) {
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
			modified = true;
		}
		
		if (!entity.getFigureLabel().equals(domain.getFigureLabel())) {
			entity.setFigureLabel(domain.getFigureLabel());
			modified = true;
			
			//changing full size figure label also changes thumbnail figure label
			if (domain.getImageTypeKey().equals(fullSizeImageKey)
					&& domain.getThumbnailImage() != null) {
				thumbnailEntity.setFigureLabel(domain.getFigureLabel());
			}			
		}

		if (!String.valueOf(entity.getImageClass().get_term_key()).equals(domain.getImageClassKey())) {
			entity.setImageClass(termDAO.get(Integer.valueOf(domain.getImageClassKey())));
			modified = true;
			
			// changing full size image class also changes thumbnail image class
			if (domain.getImageTypeKey().equals(fullSizeImageKey)
					&& domain.getThumbnailImage() != null) {
				thumbnailEntity.setImageClass(termDAO.get(Integer.valueOf(domain.getImageClassKey())));
			}
		}
		
		// process all notes
		if (noteService.process(domain.getImageKey(), domain.getCaptionNote(), mgiTypeKey, "1024", user)) {
			modified = true;
		}
		if (noteService.process(domain.getImageKey(), domain.getCopyrightNote(), mgiTypeKey, "1023", user)) {
			modified = true;
		}
		if (noteService.process(domain.getImageKey(), domain.getPrivateCuratorialNote(), mgiTypeKey, "1025", user)) {
			modified = true;
		}
		if (noteService.process(domain.getImageKey(), domain.getExternalLinkNote(), mgiTypeKey, "1039", user)) {
			modified = true;
		}

		// process image pane
		if (imagePaneService.process(domain.getImageKey(), domain.getImagePanes(), user)) {
			modified = true;
		}

		// process image pane associations 
		// if full size image
		if (domain.getImageTypeKey().equals(fullSizeImageKey)) {
			// use the first image pane only (image entity does the ordering)
			if (imagePaneAssocService.process(domain.getImagePanes().get(0).getImagePaneKey(), domain.getImagePanes().get(0).getPaneAssocs(), user)) {
				modified = true;
			}
		}
		
		// process editable accession ids (ex. PIX:)
		if (domain.getEditAccessionIds() != null && !domain.getEditAccessionIds().isEmpty()) {
			if (accessionService.process(domain.getImageKey(), domain.getEditAccessionIds(), mgiTypeName, user)) {
				modified = true;
			}
		}
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			imageDAO.update(entity);
			if (domain.getImageTypeKey().equals(fullSizeImageKey)
					&& domain.getThumbnailImage() != null) {
				thumbnailEntity.setModification_date(new Date());
				thumbnailEntity.setModifiedBy(user);
				thumbnailDAO.update(thumbnailEntity);
			}
			log.info("processImage/changes processed: " + domain.getImageKey());
		}
		else {
			log.info("processImage/no changes processed: " + domain.getImageKey());
		}
				
		// return entity translated to domain
		log.info("processImage/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processImage/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<ImageDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		Image entity = imageDAO.get(key);
		results.setItem(translator.translate(imageDAO.get(key)));
		imageDAO.remove(entity);
		return results;
	}

	@Transactional
	public ImageDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ImageDomain domain = new ImageDomain();
		if (imageDAO.get(key) != null) {
			domain = translator.translate(imageDAO.get(key));
		}
		return domain;
	}

	@Transactional
	public SearchResults<ImageDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		results.setItem(translator.translate(imageDAO.get(key)));
		return results;
	}
	
	@Transactional	
	public SearchResults<ImageDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		String cmd = "select count(*) as objectCount from img_image";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional	
	public List<SlimImageDomain> search(ImageDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimImageDomain> results = new ArrayList<SlimImageDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct i._image_key, i.jnum, i.figureLabel, i.imageType"
				+ ", concat(i.jnumID,'; ',i.imageType,'; ',i.figureLabel) as imageDisplay";
		String from = "from img_image_view i";
		String where = "where i.figureLabel is not null";
		String 	orderBy = "order by i.jnum, i.figureLabel, i.imageType";			
		String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
	
		Boolean from_imagepane = false;		
		Boolean from_captionNote = false;
		Boolean from_copyrightNote = false;
		Boolean from_privateCuratorialNote = false;
		Boolean from_externalLinkNote = false;
		Boolean from_accession = false;
		Boolean from_editAccession = false;
		Boolean from_noneditAccession = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("i", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getImageKey() != null && !searchDomain.getImageKey().isEmpty()) {
			where = where + "\nand i._image_key = " + searchDomain.getImageKey();
		}
		if (searchDomain.getImageClassKey() != null && !searchDomain.getImageClassKey().isEmpty()) {
			where = where + "\nand i._imageclass_key = " + searchDomain.getImageClassKey();
		}
		if (searchDomain.getImageTypeKey() != null && !searchDomain.getImageTypeKey().isEmpty()) {
			where = where + "\nand i._imagetype_key = " + searchDomain.getImageTypeKey();
		}		
		if (searchDomain.getXDim() != null && !searchDomain.getXDim().isEmpty()) {
			where = where + "\nand i.xDim = " + searchDomain.getXDim();
		}
		if (searchDomain.getYDim() != null && !searchDomain.getYDim().isEmpty()) {
			where = where + "\nand i.yDim = " + searchDomain.getYDim();
		}			
		if (searchDomain.getFigureLabel() != null && !searchDomain.getFigureLabel().isEmpty()) {
			value = searchDomain.getFigureLabel().replace("'",  "''");
			where = where + "\nand i.figureLabel ilike '" + value + "'";
		}
		if (searchDomain.getImagePanes() != null) {
			if (searchDomain.getImagePanes().get(0).getPaneLabel() != null
				&& !searchDomain.getImagePanes().get(0).getPaneLabel().isEmpty()) {
			value = searchDomain.getImagePanes().get(0).getPaneLabel().replace("'",  "''");
			value = value.replace("_",  "\\_");
			where = where + "\nand p.paneLabel ilike '" + value + "'";
			from_imagepane = true;
			}
		}
		
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
		
		// notes
		if (searchDomain.getCaptionNote() != null && !searchDomain.getCaptionNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getCaptionNote().getNoteChunk().replace("'",  "''");
			log.info(searchDomain.getCaptionNote().getNoteChunk());
			value = value.replace("\\", "\\\\");		
			where = where + "\nand note1._notetype_key = 1024 and note1.note ilike '" + value + "'" ;
			from_captionNote = true;
		}
		if (searchDomain.getCopyrightNote() != null && !searchDomain.getCopyrightNote().getNoteChunk().isEmpty() 
				&& searchDomain.getCopyrightNote().getNoteChunk().contains("%")) {
			value = searchDomain.getCopyrightNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note2._notetype_key = 1023 and note2.note ilike '" + value + "'" ;
			from_copyrightNote = true;
		}
		if (searchDomain.getPrivateCuratorialNote() != null && !searchDomain.getPrivateCuratorialNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getPrivateCuratorialNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note3._notetype_key = 1025 and note3.note ilike '" + value + "'" ;
			from_privateCuratorialNote = true;
		}
		if (searchDomain.getExternalLinkNote() != null  && !searchDomain.getExternalLinkNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getExternalLinkNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note4._notetype_key = 1039 and note4.note ilike '" + value + "'" ;
			from_externalLinkNote = true;
		}		
				
		// accession id
		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().get(0).getAccID().isEmpty()) {
			String mgiid = searchDomain.getMgiAccessionIds().get(0).getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand a.accID ilike '" + mgiid + "'";
			from_accession = true;
		}
		// else thumbnail accession id
		else if (searchDomain.getThumbnailImage() != null) {
			if (searchDomain.getThumbnailImage().getMgiAccessionIds() != null 
					&& !searchDomain.getThumbnailImage().getMgiAccessionIds().get(0).getAccID().isEmpty()) {
				String mgiid = searchDomain.getThumbnailImage().getMgiAccessionIds().get(0).getAccID().toUpperCase();
				if (!mgiid.contains("MGI:")) {
					mgiid = "MGI:" + mgiid;
				}
				where = where + "\nand a.accID ilike '" + mgiid + "'";
				from_accession = true;
			}
		}
				
		// editable accession ids
		if (searchDomain.getEditAccessionIds() != null) {
			if (searchDomain.getEditAccessionIds().get(0).getAccID() != null 
					&& !searchDomain.getEditAccessionIds().get(0).getAccID().isEmpty()) {
				where = where + "\nand acc1.accID ilike '" +  searchDomain.getEditAccessionIds().get(0).getAccID() + "'";
				from_editAccession = true;
			}
			if (searchDomain.getEditAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getEditAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
				where = where + "\nand acc1._logicaldb_key = " + searchDomain.getEditAccessionIds().get(0).getLogicaldbKey();
				from_editAccession = true;
			}
		}
		
		// non-editable accession ids
		if (searchDomain.getNonEditAccessionIds() != null) {
			if (searchDomain.getNonEditAccessionIds().get(0).getAccID() != null 
					&& !searchDomain.getNonEditAccessionIds().get(0).getAccID().isEmpty()) {
				where = where + "\nand acc2.accID ilike '" +  searchDomain.getNonEditAccessionIds().get(0).getAccID() + "'";
				from_noneditAccession = true;
			}
			if (searchDomain.getNonEditAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getNonEditAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
				where = where + "\nand acc2._logicaldb_key = " + searchDomain.getNonEditAccessionIds().get(0).getLogicaldbKey();
				from_noneditAccession = true;
			}			
		}
				
		// use views to match the teleuse implementation

		if (from_imagepane == true) {
			from = from + ", img_imagepane p";
			where = where + "\nand i._image_key = p._image_key";
		}
		if (from_captionNote == true) {
			from = from + ", mgi_note_image_view note1";
			where = where + "\nand i._image_key = note1._object_key";
		}
		if (from_copyrightNote == true) {
			from = from + ", mgi_note_image_view note2";
			where = where + "\nand i._image_key = note2._object_key";
		}	
		if (from_privateCuratorialNote == true) {
			from = from + ", mgi_note_image_view note3";
			where = where + "\nand i._image_key = note3._object_key";
		}	
		if (from_externalLinkNote == true) {
			from = from + ", mgi_note_image_view note4";
			where = where + "\nand i._image_key = note4._object_key";
		}			
		if (from_accession == true) {
			from = from + ", img_image_acc_view a";
			where = where + "\nand i._image_key = a._object_key" 
					+ "\nand a._mgitype_key = 9";
		}		
		if (from_editAccession == true) {
			from = from + ", img_image_acc_view acc1";
			where = where + "\nand acc1._logicaldb_key in (19)" +
					"\nand i._image_key = acc1._object_key";
		}
		if (from_noneditAccession == true) {
			from = from + ", img_image_acc_view acc2";
			where = where + "\nand acc2._logicaldb_key not in (1, 19)" +
					"\nand i._image_key = acc2._object_key";		
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimImageDomain domain = new SlimImageDomain();
				domain.setImageKey(rs.getString("_image_key"));
				domain.setImageDisplay(rs.getString("imageDisplay"));
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
	public List<ImageSubmissionDomain> searchImagePaneByJnum(ImageSubmissionDomain searchDomain) {
		// search for all image panes by specific Jnum
		
		List<ImageSubmissionDomain> results = new ArrayList<ImageSubmissionDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct i._image_key, i.jnum, i.imageClass, i.imageType";
		String from = "from img_image_view i";
		String where = "where i.figureLabel is not null";
		String orderBy = "order by i.jnum, i.imageClass, i.imageType";
		String value;
			
		if (searchDomain.getImageKey() != null && !searchDomain.getImageKey().isEmpty()) {
			where = where + "\nand i._image_key = " + searchDomain.getImageKey();
		}	
		if (searchDomain.getImageClassKey() != null && !searchDomain.getImageClassKey().isEmpty()) {
			where = where + "\nand i._imageclass_key = " + searchDomain.getImageClassKey();
		}	
		if (searchDomain.getImageTypeKey() != null && !searchDomain.getImageTypeKey().isEmpty()) {
			where = where + "\nand i._imagetype_key = " + searchDomain.getImageTypeKey();
		}

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
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ImageSubmissionDomain domain = new ImageSubmissionDomain();
				domain = submissionTranslator.translate(imageDAO.get(rs.getInt("_image_key")));
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
