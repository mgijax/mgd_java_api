package org.jax.mgi.mgd.api.model.dag.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.dag.domain.DagEdgeDomain;
import org.jax.mgi.mgd.api.model.dag.domain.DagNodeDomain;
import org.jax.mgi.mgd.api.model.dag.entities.DagNode;

public class DagNodeTranslator extends BaseEntityDomainTranslator<DagNode, DagNodeDomain> {
	
	@Override
	protected DagNodeDomain entityToDomain(DagNode entity) {
		DagNodeDomain domain = new DagNodeDomain();
		
		domain.setNodeKey(String.valueOf(entity.get_node_key()));
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getDag() != null ) {  //&& !entity.getDag().isEmpty()) {
			DagTranslator dagTranslator = new DagTranslator();
			domain.setDagDomain(dagTranslator.translate(entity.getDag()));
		}

		if (entity.getLabel() != null) {
			DagLabelTranslator labelTranslator = new DagLabelTranslator();
			domain.setLabel(labelTranslator.translate(entity.getLabel()));
		}

		// parent edges
		if (entity.getParentEdges() != null && !entity.getParentEdges().isEmpty()) {
			DagEdgeTranslator pedgeTranslator = new DagEdgeTranslator();
			Iterable<DagEdgeDomain> p = pedgeTranslator.translateEntities(entity.getParentEdges());
			domain.setParentEdges(IteratorUtils.toList(p.iterator()));
		}

		// child edges
		if (entity.getChildEdges() != null && !entity.getChildEdges().isEmpty()) {
			DagEdgeTranslator cedgeTranslator = new DagEdgeTranslator();
			Iterable<DagEdgeDomain> c = cedgeTranslator.translateEntities(entity.getChildEdges());
			domain.setChildEdges(IteratorUtils.toList(c.iterator()));
		}
		
		return domain;
	}
}
