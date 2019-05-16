package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ReferenceBookTranslator extends BaseEntityDomainTranslator<ReferenceBook, ReferenceBookDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected ReferenceBookDomain entityToDomain(ReferenceBook entity) {

		ReferenceBookDomain domain = new ReferenceBookDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setBook_author(entity.getBook_author());
		domain.setBook_title(entity.getBook_title());
		domain.setPlace(entity.getPlace());
		domain.setPublisher(entity.getPublisher());
		domain.setSeries_edition(entity.getSeries_edition());	
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
