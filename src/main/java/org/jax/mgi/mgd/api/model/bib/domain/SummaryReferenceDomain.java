package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryReferenceDomain extends BaseDomain {

	// a slim version of ReferenceDomain used by "getByAllele" and "getByMarker" endpoints
	// not to be used when editing purposes
	// to be used for returning search results
	
	public String refsKey;
	public String jnumID;
	public String jnum;
	public String short_citation;
	public String authors;
	public String title;	
	public String journal;
	public String year;
	public String vol;
	public String mgiid;	
	public String pubmedid;
	public String referencetype;
	public String referenceAbstract;
	public Boolean hasAllele = false;
	public Boolean hasAssay = false;
	public Boolean hasAssayResult = false;
	public Boolean hasAssaySpecimen = false;
	public Boolean hasAntibody = false;
	public Boolean hasGXDImage = false;
	public Boolean hasGXDIndex = false;	
	public Boolean hasMarker = false;	
	public Boolean hasProbe = false;
	
}
