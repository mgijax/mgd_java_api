package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryReferenceDomain extends BaseDomain {

	// a slim version of ReferenceDomain used by "getByAllele" and "getByMarker" endpoints
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String accID;
	private int offset;
	private int limit;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String authors;
	private String primaryAuthor;
	private String title;	
	private String journal;
	private String year;
	private String vol;
	private String mgiid;	
	private String pubmedid;
	private String doiid;
	private String referencetype;
	private String referenceAbstract;
	private Boolean hasAllele = false;
	private Boolean hasAssay = false;
	private Boolean hasAssayResult = false;
	private Boolean hasAssaySpecimen = false;
	private Boolean hasAntibody = false;
	private Boolean hasGXDImage = false;
	private Boolean hasGXDIndex = false;	
	private Boolean hasMarker = false;	
	private Boolean hasProbe = false;
	private Boolean hasMapping = false;
	
}
