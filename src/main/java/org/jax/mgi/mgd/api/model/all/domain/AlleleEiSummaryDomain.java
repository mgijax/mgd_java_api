package org.jax.mgi.mgd.api.model.all.domain;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Allele EI Summary Model Object")
public class AlleleEiSummaryDomain extends BaseDomain {

	private Map<String, HashMap> summaryAlleles;        
}
