package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeTissue;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeSourceDomain extends BaseDomain {

	private String sourceKey;
	private String name;
	private String description;
	private String age;
	private Term segmentType;
	private Term vector;
	private Organism organism;
	private ProbeStrain strain;
	private ProbeTissue tissue;
	private Term gender;
	private AlleleCellLine cellLine;		
	private String createdBy;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}
