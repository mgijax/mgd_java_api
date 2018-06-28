package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Index;

public class IndexTranslator extends BaseEntityDomainTranslator<Index, IndexDomain> {

	//private MarkerTranslator translator = new MarkerTranslator();
	private ReferenceTranslator refTranslator = new ReferenceTranslator();
	@Override
	protected IndexDomain entityToDomain(Index entity, int translationDepth) {
		
		
		IndexDomain domain = new IndexDomain();
		domain.set_index_key(entity.get_index_key());
		domain.setComments(entity.getComments());
		domain.setPriority(entity.getPriority().getTerm());
		domain.setConditionalMutants(entity.getConditionalMutants().getTerm());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
	
		
		if(translationDepth > 0) {
			domain.setJnumID(refTranslator.translate(entity.getReference()).jnumid);
			//domain.setReference(refTranslator.translate(entity.getReference(), translationDepth - 1));
			//domain.setMarker(translator.translate(entity.getMarker(), translationDepth - 1));

				
		}
		return domain;
	}

	@Override
	protected Index domainToEntity(IndexDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
