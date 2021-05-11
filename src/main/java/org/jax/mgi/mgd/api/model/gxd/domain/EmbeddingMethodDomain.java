package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmbeddingMethodDomain extends BaseDomain {

	private String vocabKey = "155";
	private int isSimple = 1;
	private int isPrivate = 0;
	private String name = "GXD Embedding Method";
	private List<TermDomain> terms;
}
