package org.jax.mgi.mgd.api.model.dag.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.dag.domain.DagDomain;
import org.jax.mgi.mgd.api.model.dag.domain.DagEdgeDomain;
import org.jax.mgi.mgd.api.model.dag.entities.Dag;
import org.jax.mgi.mgd.api.model.dag.entities.DagEdge;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

public class DagEdgeTranslator extends BaseEntityDomainTranslator<DagEdge, DagEdgeDomain> {
	
	@Override
	protected DagEdgeDomain entityToDomain(DagEdge entity) {
		
		DagNodeTranslator nodeTranslator = new DagNodeTranslator();
		
		DagEdgeDomain domain = new DagEdgeDomain();
		
		domain.setEdgeKey(String.valueOf(entity.get_edge_key()));
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getDag() != null ) {  //&& !entity.getDag().isEmpty()) {
			DagTranslator dagTranslator = new DagTranslator();
			domain.setDagDomain(dagTranslator.translate(entity.getDag()));
		}
		
		if (entity.getParentNode() != null) {
			domain.setParentNode(nodeTranslator.translate(entity.getParentNode()));
		}
		
		if (entity.getParentNode() != null) {
			domain.setChildNode(nodeTranslator.translate(entity.getChildNode()));
				
		}
		
		if (entity.getLabel() !=  null) {
			DagLabelTranslator labelTranslator = new DagLabelTranslator();
			domain.setLabel(labelTranslator.translate(entity.getLabel()));
		}
		
		return domain;
	}

}
