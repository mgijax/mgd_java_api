package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.ProbePrepDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.ProbePrep;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ProbePrepTranslator extends BaseEntityDomainTranslator<ProbePrep, ProbePrepDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ProbePrepDomain entityToDomain(ProbePrep entity) {

		ProbePrepDomain domain = new ProbePrepDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setProbePrepKey(String.valueOf(entity.get_probeprep_key()));
		domain.setProbeKey(String.valueOf(entity.getProbe().get_probe_key()));
		domain.setProbeName(entity.getProbe().getName());
		domain.setProbeAccID(entity.getProbe().getMgiAccessionIds().get(0).getAccID());
		domain.setProbeSenseKey(String.valueOf(entity.getProbeSense().get_sense_key()));
		domain.setProbeSenseName(entity.getProbeSense().getSense());
		domain.setLabelKey(String.valueOf(entity.getLabel().get_label_key()));
		domain.setLabelName(entity.getLabel().getLabel());
		domain.setVisualizationMethodKey(String.valueOf(entity.getVisualizationMethod().get_visualization_key()));
		domain.setVisualiationMethod(entity.getVisualizationMethod().getVisualization());
		domain.setPrepType(entity.getType());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
