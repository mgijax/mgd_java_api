package org.jax.mgi.mgd.api.model.dag.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.dag.domain.DagEdgeDomain;
import org.jax.mgi.mgd.api.model.dag.entities.DagEdge;

public class DagEdgeTranslator extends BaseEntityDomainTranslator<DagEdge, DagEdgeDomain> {
	
	@Override
	protected DagEdgeDomain entityToDomain(DagEdge entity) {
				
		DagEdgeDomain domain = new DagEdgeDomain();
		
		domain.setEdgeKey(String.valueOf(entity.get_edge_key()));
		domain.setDagKey(String.valueOf(entity.get_dag_key()));
//		domain.setParentKey(String.valueOf(entity.get_parent_key()));
//		domain.setChildKey(String.valueOf(entity.get_child_key()));
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		domain.setParentKey(String.valueOf(entity.getParentTerm().get_term_key()));
		domain.setParentTerm(entity.getParentTerm().getTerm());
		domain.setChildKey(String.valueOf(entity.getChildTerm().get_term_key()));
		domain.setChildTerm(entity.getChildTerm().getTerm());
		
		if (entity.getLabel() !=  null) {
			DagLabelTranslator labelTranslator = new DagLabelTranslator();
			domain.setLabel(labelTranslator.translate(entity.getLabel()));
		}
		
		return domain;
	}

}
