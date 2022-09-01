package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimReferenceByDomain extends BaseDomain {

	// a slim version of ReferenceDomain used by "getByAllele" and "getByMarker" endpoints
	// not to be used when editing purposes
	// to be used for returning search results
	
	public String refsKey;
	public String jnumid;
	public String jnum;
	public String short_citation;
	public String title;	
	public String journal;
	public String year;
	public String mgiid;	
	public String pubmedid;
	public String vol;
	public String referencetype;
	public String referenceAbstract;

}
