package org.jax.mgi.mgd.api.model;

import java.util.ArrayList;

public abstract class BaseEntityDomainMapTranslator<E extends BaseEntity, D extends BaseDomain> {

	public E translate(D domain) {
		return domainToEntity(domain);
	}

	public D translate(E entity) {
		return entityToDomain(entity);
	}

	public Iterable<E> translateDomains(Iterable<D> domains) {
		ArrayList<E> entities = new ArrayList<E>();
		for(D domain: domains) {
			entities.add(domainToEntity(domain));
		}
		return entities;
	}

	public Iterable<D> translateEntities(Iterable<E> entities) {
		ArrayList<D> domains = new ArrayList<D>();
		for(E entity: entities) {
			domains.add(entityToDomain(entity));
		}
		return domains;
	}

	protected abstract D entityToDomain(E entity);
	protected abstract E domainToEntity(D domain);
}
