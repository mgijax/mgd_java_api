package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;

public class AssayTranslator extends BaseEntityDomainTranslator<Assay, AssayDomain> {

	@Override
	protected AssayDomain entityToDomain(Assay entity) {

		AssayDomain domain = new AssayDomain();

		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());	
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		if (entity.getAntibodyPrep() != null) {
			domain.setAntibodyPrepKey(String.valueOf(entity.getAntibodyPrep().get_antibodyprep_key()));
			domain.setAntibodyPrepName(entity.getAntibodyPrep().getAntibody().getAntibodyName());
			domain.setAntibodyPrepSecondaryKey(String.valueOf(entity.getAntibodyPrep().getSecondary().get_secondary_key()));
			domain.setAntibodyPrepSecondary(entity.getAntibodyPrep().getSecondary().getSecondary());
			domain.setAntibodyPrepLabelKey(String.valueOf(entity.getAntibodyPrep().getLabel().get_label_key()));
			domain.setAntibodyPrepLabel(entity.getAntibodyPrep().getLabel().getLabel());
			if (entity.getAntibodyPrep().getAntibody().getMgiAccessionIds() != null && !entity.getAntibodyPrep().getAntibody().getMgiAccessionIds().isEmpty()) {
				domain.setAntibodyAccID(entity.getMgiAccessionIds().get(0).getAccID());
			}
		}

		if (entity.getAntibodyPrep() != null) {
			domain.setProbePrepKey(String.valueOf(entity.getProbePrep().get_probeprep_key()));
			domain.setProbePrepType(entity.getProbePrep().getType());
			domain.setProbePrepSenseKey(String.valueOf(entity.getProbePrep().getProbeSense().get_sense_key()));
			domain.setProbePrepSense(entity.getProbePrep().getProbeSense().getSense());
			domain.setProbePrepLabelKey(String.valueOf(entity.getProbePrep().getLabel().get_label_key()));
			domain.setProbePrepLabel(entity.getProbePrep().getLabel().getLabel());
			domain.setProbePrepVisualizationKey(String.valueOf(entity.getProbePrep().getVisualizationMethod().get_visualization_key()));
			domain.setProbePrepVisualiation(entity.getProbePrep().getVisualizationMethod().getVisualization());
			if (entity.getProbePrep().getProbe().getMgiAccessionIds() != null && !entity.getProbePrep().getProbe().getMgiAccessionIds().isEmpty()) {
				domain.setProbeAccID(entity.getMgiAccessionIds().get(0).getAccID());
			}
		
		}
		
		if (entity.getImagePane() != null) {
			domain.setImagePaneKey(String.valueOf(entity.getImagePane().get_imagepane_key()));
			domain.setImagePaneLabel(entity.getImagePane().getPaneLabel());
		}
		
		if (entity.getReporterGene() != null) {
			domain.setReporterGeneKey(String.valueOf(entity.getReporterGene().get_term_key()));
			domain.setReporterGene(entity.getReporterGene().getTerm());
		}
		
		return domain;
	}

}
