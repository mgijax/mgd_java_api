package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Term/Ancestor Domain")
public class TermAncestorDomain extends BaseDomain {

	// specifically for the pwi/genotype detail page
	// for each term key, a list of ancestor term keys
	// see TermService.getAncestorKeys()
	
	private int termKey;
	private List<Integer> ancestors = new ArrayList<Integer>();
}
