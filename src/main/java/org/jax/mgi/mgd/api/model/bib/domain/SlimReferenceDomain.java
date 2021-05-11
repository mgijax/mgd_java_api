package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimReferenceDomain extends BaseDomain {

	// a slim version of ReferenceDomain 
	// not to be used when editing purposes
	// to be used for returning search results
	
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String journal;
	private String year;
	public String mgiid;	
	public String doiid;
	public String pubmedid;
	
	// used by validateJnumImage
	private List<TermDomain> journalLicenses;
	private String copyright;
	private String selectedJournalLicense;
	private Boolean needsDXDOIid = false;
}
