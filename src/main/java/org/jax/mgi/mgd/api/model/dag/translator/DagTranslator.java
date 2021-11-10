package org.jax.mgi.mgd.api.model.dag.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.dag.domain.DagDomain;
import org.jax.mgi.mgd.api.model.dag.entities.Dag;

public class DagTranslator extends BaseEntityDomainTranslator<Dag, DagDomain> {
	
	@Override
	protected DagDomain entityToDomain(Dag entity) {
		DagDomain domain = new DagDomain();
		
		domain.setDagKey(String.valueOf(entity.get_dag_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setName(entity.getName());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		domain.setMgiType(entity.getMgiType());
		
		return domain;
	}

}
