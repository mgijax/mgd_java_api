package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyClassDomain extends BaseDomain {

	private String vocabKey;
	private int isSimple;
	private int isPrivate;
	private String name;
	private String creation_date;
	private String modification_date;
	private List<TermDomain> terms;
}
