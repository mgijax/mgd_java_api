package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryAlleleDomain extends BaseDomain {

	private String markerID;
	private String jnumID;
	private String alleleID;
	private String alleleKey;
	private String symbol;
	private String name;
	private String alleleType;
	private String alleleStatus;	
	private String transmission;
	private String diseases;
	private String pheno;	
	private String synonyms;
	private String attrs;
	
}
