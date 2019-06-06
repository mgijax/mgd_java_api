package org.jax.mgi.mgd.api.model.img.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImageDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jboss.logging.Logger;

public class ImageTranslator extends BaseEntityDomainTranslator<Image, ImageDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private NoteTranslator noteTranslator = new NoteTranslator();	
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private ImagePaneTranslator imagePaneTranslator = new ImagePaneTranslator();
	
	@Override
	protected ImageDomain entityToDomain(Image entity) {
		
		ImageDomain domain = new ImageDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
			
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setImageClassKey(String.valueOf(entity.getImageClass().get_term_key()));
		domain.setImageClass(entity.getImageClass().getTerm());
		domain.setImageTypeKey(String.valueOf(entity.getImageType().get_term_key()));
		domain.setImageType(entity.getImageType().getTerm());
		domain.setFigureLabel(entity.getFigureLabel());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getXDim() != null) {
			domain.setXDim(String.valueOf(entity.getXDim()));
		}
		
		if (entity.getYDim() != null) {
			domain.setYDim(String.valueOf(entity.getYDim()));
		}
				
		// reference
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());

		// at most one captionNote
		if (entity.getCaptionNote() != null && !entity.getCaptionNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getCaptionNote());
			domain.setCaptionNote(note.iterator().next());
		}
		
		// at most one copyrightNote
		if (entity.getCopyrightNote() != null && !entity.getCopyrightNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getCopyrightNote());
			domain.setCopyrightNote(note.iterator().next());
		}
		
		// at most one privateCuratorialNote
		if (entity.getPrivateCuratorialNote() != null && !entity.getPrivateCuratorialNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getPrivateCuratorialNote());
			domain.setPrivateCuratorialNote(note.iterator().next());
		}
		
		// at most one externalLinkNote
		if (entity.getExternalLinkNote() != null && !entity.getExternalLinkNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getExternalLinkNote());
			domain.setExternalLinkNote(note.iterator().next());
		}
				
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}

		// accession ids editable
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			domain.setEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		
		// accession ids non-editable 
		if (entity.getNonEditAccessionIds() != null && !entity.getNonEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getNonEditAccessionIds());
			domain.setNonEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getNonEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		
		// image panes 
		if (entity.getImagePanes() != null && !entity.getImagePanes().isEmpty()) {
			Iterable<ImagePaneDomain> imagePane = imagePaneTranslator.translateEntities(entity.getImagePanes());
			domain.setImagePanes(IteratorUtils.toList(imagePane.iterator()));
			//paneLabel may be null
			if (domain.getImagePanes().get(0).getPaneLabel() != null) {
				domain.getImagePanes().sort(Comparator.comparing(ImagePaneDomain::getPaneLabel));
			}
		}
		
		// may have 1 thumbnail
		if (entity.getThumbnailImage() != null ) {
			ImageTranslator imageTranslator = new ImageTranslator();		
			domain.setThumbnailImage(imageTranslator.translate(entity.getThumbnailImage()));
		}
				
		return domain;
	}

}
