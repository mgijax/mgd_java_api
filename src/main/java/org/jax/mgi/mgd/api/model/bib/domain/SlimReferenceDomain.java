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
	
	public String refsKey;
	public String jnumid;
	public String jnum;
	public String short_citation;
	public String title;	
	public String journal;
	public String year;
	public String mgiid;	
	public String doiid;
	public String pubmedid;

	// used by lit triage
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String pro_status;
	public String qtl_status;
	public String tumor_status;
	public String haspdf;
	
	// used by validateJnumImage
	public List<TermDomain> journalLicenses;
	public String copyright;
	public String selectedJournalLicense;
	public Boolean needsDXDOIid = false;
}
