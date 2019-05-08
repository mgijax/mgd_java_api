package org.jax.mgi.mgd.api.model;

import java.text.SimpleDateFormat;

public abstract class BaseEntityDomainTranslator<E extends BaseEntity, D extends BaseDomain> {

	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	// to copy what teleuse/ei does
    protected SimpleDateFormat dateFormatNoTime = new SimpleDateFormat("yyyy-MM-dd");
    
	public D translate(E entity) {
		return translate(entity);
	}

	public Iterable<D> translateEntities(Iterable<E> entities) {
		return translateEntities(entities);
	}

	protected abstract D entityToDomain(E entity);
	
}
