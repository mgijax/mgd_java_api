package org.jax.mgi.mgd.api.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public abstract class BaseEntityDomainTranslator<E extends BaseEntity, D extends BaseDomain> {

	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	// to copy what teleuse/ei does
    protected SimpleDateFormat dateFormatNoTime = new SimpleDateFormat("yyyy-MM-dd");
    
	public D translate(E entity) {
		return translate(entity, 1);
	}
	public D translate(E entity, int depth) {
		return entityToDomain(entity, depth);
	}

	public Iterable<D> translateEntities(Iterable<E> entities) {
		return translateEntities(entities, 1);
	}

	public Iterable<D> translateEntities(Iterable<E> entities, int translationDepth) {
		ArrayList<D> domains = new ArrayList<D>();
		for(E entity: entities) {
			if(entity != null) {
				domains.add(entityToDomain(entity, translationDepth));
			}
		}
		return domains;
	}

	protected abstract D entityToDomain(E entity, int translationDepth);
	
}
