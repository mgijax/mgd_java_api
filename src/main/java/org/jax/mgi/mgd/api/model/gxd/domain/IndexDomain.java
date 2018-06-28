package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IndexDomain extends BaseDomain {

	private Integer _index_key;
	private String jnumID;
	//private ReferenceDomain reference;
	//private MarkerDomain marker;
	private String priority;
	private String conditionalMutants;
	private String comments;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
	//public void setReference(ReferenceDomain reference) {
		// TODO Auto-generated method stub
	//	this.reference = reference;
		
	//}
	
	

}
