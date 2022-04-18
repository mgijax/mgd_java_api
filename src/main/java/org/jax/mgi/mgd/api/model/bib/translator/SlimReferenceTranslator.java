package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

public class SlimReferenceTranslator extends BaseEntityDomainTranslator<Reference, SlimReferenceDomain> {

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	
	@Override
	protected SlimReferenceDomain entityToDomain(Reference entity) {

		SlimReferenceDomain domain = new SlimReferenceDomain();
		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setJnumid(entity.getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));	
		domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		domain.setTitle(entity.getTitle());
		domain.setJournal(entity.getJournal());
		domain.setYear(String.valueOf(entity.getYear()));

		// first mgi accession id only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			//domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.setMgiid(acc.iterator().next().getAccID());		
		}

		// non-mgi accession ids
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			List<AccessionDomain> editAccessionIds = new ArrayList<AccessionDomain>();
			editAccessionIds.addAll(IteratorUtils.toList(acc.iterator()));
			for (int i = 0; i < editAccessionIds.size(); i++) {
				if (editAccessionIds.get(i).getLogicaldbKey().equals("29")) {
					domain.setPubmedid(editAccessionIds.get(i).getAccID());
				}
				else if (editAccessionIds.get(i).getLogicaldbKey().equals("65")) {
					domain.setDoiid(editAccessionIds.get(i).getAccID());
				}								
			}
		}
		
		// used by validateJnumImage
		domain.setJournalLicenses(null);
		domain.setCopyright("");
		domain.setSelectedJournalLicense("");
		domain.setNeedsDXDOIid(false);

		return domain;
	}

}
