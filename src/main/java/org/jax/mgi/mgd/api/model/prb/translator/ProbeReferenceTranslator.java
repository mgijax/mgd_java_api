package org.jax.mgi.mgd.api.model.prb.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAliasDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeReferenceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReference;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeReferenceTranslator extends BaseEntityDomainTranslator<ProbeReference, ProbeReferenceDomain> {

	@Override
	protected ProbeReferenceDomain entityToDomain(ProbeReference entity) {
		
		ProbeReferenceDomain domain = new ProbeReferenceDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setReferenceKey(String.valueOf(entity.get_reference_key()));
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setHasRmap(String.valueOf(entity.getHasRmap()));
		domain.setHasSequence(String.valueOf(entity.getHasSequence()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(entity.getReference().getReferenceCitationCache().getNumericPart());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
//		// accession ids
//		if (entity.getAccessionIds() != null && !entity.getAccessionIds().isEmpty()) {
//			SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
//			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getAccessionIds());
//			domain.setAccessionIds(IteratorUtils.toList(acc.iterator()));
//		}
//
//		// aliases
//		if (entity.getAliases() != null && !entity.getAliases().isEmpty()) {
//			ProbeAliasTranslator aliasTranslator = new ProbeAliasTranslator();
//			Iterable<ProbeAliasDomain> alias = aliasTranslator.translateEntities(entity.getAliases());
//			domain.setAliases(IteratorUtils.toList(alias.iterator()));
//		}
		
		return domain;
	}

}
