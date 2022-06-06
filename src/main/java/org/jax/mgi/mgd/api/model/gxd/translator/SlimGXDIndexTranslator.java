package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndex;
import org.jboss.logging.Logger;

public class SlimGXDIndexTranslator extends BaseEntityDomainTranslator<GXDIndex, SlimGXDIndexDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimGXDIndexDomain entityToDomain(GXDIndex entity) {

		SlimGXDIndexDomain domain = new SlimGXDIndexDomain();
		domain.setIndexKey(String.valueOf(entity.get_index_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getNumericPart()));
		domain.setShort_citation(entity.getReference().getShort_citation());
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());		
		domain.setMarkerName(entity.getMarker().getName());
		domain.setMarkerChromosome(entity.getMarker().getChromosome());
		domain.setMarkerAccID(entity.getMarker().getMgiAccessionIds().get(0).getAccID());
		domain.setIndexDisplay(domain.getMarkerSymbol() + " " + domain.getJnumid() + " " + domain.getShort_citation());
		
		return domain;
	}

}
