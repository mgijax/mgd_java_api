package org.jax.mgi.mgd.api.model.acc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDbDomain;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDbDomain;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jboss.logging.Logger;


public class LogicalDbTranslator extends BaseEntityDomainTranslator<LogicalDB, LogicalDbDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private ActualDbTranslator adbTranslator = new ActualDbTranslator();
	
	@Override
	protected LogicalDbDomain entityToDomain(LogicalDB entity) {
		LogicalDbDomain domain = new LogicalDbDomain();
		
		domain.setLogicalDBKey(String.valueOf(entity.get_logicaldb_key()));
		domain.setName(entity.getName());
		if (entity.getDescription() != null && !entity.getDescription().isEmpty()) {
		    domain.setDescription(entity.getDescription());
		}
		if (entity.getOrganism() == null ) {
		    domain.setOrganismKey("76");
		    domain.setCommonName("Not Specified");
		}
		else {
			domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
			domain.setCommonName(entity.getOrganism().getCommonname());
		}
		domain.setCreatedBy(entity.getCreatedBy().getLogin().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getActualDBs() != null && !entity.getActualDBs().isEmpty()) {
			Iterable<ActualDbDomain> actualDBs = adbTranslator.translateEntities(entity.getActualDBs());
			domain.setActualDBs(IteratorUtils.toList(actualDBs.iterator()));

		}

		return domain;
	}
}
