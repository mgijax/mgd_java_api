package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

/* Is: a domain object that represents only the limited set of data needed to display a single reference
 * 	on the summary page
 * Has: fields needed to display a reference in the PWI's reference summary table
 * Does: serves as a data-transfer object between the API and the PWI
 */
// has-pdf flag, status for each of five groups
public class LTReferenceSummaryDomain extends BaseDomain {
	public String refsKey;
	public String title;
	public String jnumid;
	public String doiid;
	public String pubmedid;
	public String mgiid;
	public String short_citation;
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String pro_status;
	public String qtl_status;
	public String tumor_status;
	public String haspdf;

	/***--- constructors ---***/

	/* empty constructor - ready for population from JSON */
	public LTReferenceSummaryDomain() {}
}
