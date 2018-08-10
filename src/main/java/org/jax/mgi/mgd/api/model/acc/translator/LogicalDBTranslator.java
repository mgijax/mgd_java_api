package org.jax.mgi.mgd.api.model.acc.translator;

import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;

public class LogicalDBTranslator extends BaseEntityDomainTranslator<LogicalDB, LogicalDBDomain> {
	
	@Override
	protected LogicalDBDomain entityToDomain(LogicalDB entity, int translationDepth) {
		LogicalDBDomain domain = new LogicalDBDomain();

		//domain.set_logicaldb_key(entity.get_logicaldb_key());
		//domain.setName(entity.getName());
		//domain.setDescription(entity.getDescription());
		//domain.setCreatedBy(entity.getCreatedBy().getName());
		//domain.setModifiedBy(entity.getModifiedBy().getName());
		//domain.setCreation_date(entity.getCreation_date());
		//domain.setModification_date(entity.getModification_date());

		if(translationDepth > 0) {
			//List<String> actualdbs = new ArrayList<String>();
			//for (ActualDB adb : entity.getActualDBs()) {
				//actualdbs.add(adb.getUrl());
			//}
			//domain.setActualdbs(actualdbs);
		}

		return domain;
	}

	@Override
	protected LogicalDB domainToEntity(LogicalDBDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}
