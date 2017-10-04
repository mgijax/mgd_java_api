package org.jax.mgi.mgd.api.translators;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.entities.EntityBase;
import org.jboss.logging.Logger;

public abstract class EntityDomainTranslator<E extends EntityBase, D extends DomainBase> {

	private Logger log = Logger.getLogger(getClass());
	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

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
