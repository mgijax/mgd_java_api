package org.jax.mgi.mgd.api.model.dag.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.dag.domain.DagDomain;
import org.jax.mgi.mgd.api.model.dag.domain.DagLabelDomain;
import org.jax.mgi.mgd.api.model.dag.domain.DagNodeDomain;
import org.jax.mgi.mgd.api.model.dag.entities.Dag;
import org.jax.mgi.mgd.api.model.dag.entities.DagLabel;
import org.jax.mgi.mgd.api.model.dag.entities.DagNode;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.SpecimenTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

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
			
		return domain;
	}

}
