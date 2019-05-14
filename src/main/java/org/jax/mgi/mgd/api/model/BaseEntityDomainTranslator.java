package org.jax.mgi.mgd.api.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public abstract class BaseEntityDomainTranslator<E extends BaseEntity, D extends BaseDomain> {

	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	// to copy what teleuse/ei does
    protected SimpleDateFormat dateFormatNoTime = new SimpleDateFormat("yyyy-MM-dd");
    
    // to translate 1 Entity to 1 Domain
	public D translate(E entity) {
		return entityToDomain(entity);
	}

	// to translate list of Entities to list of Domains (see entities/OneToMany)
	public Iterable<D> translateEntities(Iterable<E> entities) {
		ArrayList<D> domains = new ArrayList<D>();
		for(E entity: entities) {
			if(entity != null) {
				domains.add(entityToDomain(entity));
			}
		}
		return domains;
	}

	protected abstract D entityToDomain(E entity);

}
