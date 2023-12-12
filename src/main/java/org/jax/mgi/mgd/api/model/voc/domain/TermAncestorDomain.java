package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Term Ancestor Domain")
public class TermAncestorDomain extends BaseDomain {

	// specifically for the pwi/genotype detail page
	// for each term key, a list of ancestor term keys
	// see TermService.getAncestorKeys()
	
	private int termKey;
	private List<Integer> ancestors = new ArrayList<Integer>();
}
