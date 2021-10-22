package org.jax.mgi.mgd.api.model.img.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageSummaryDomain extends BaseDomain {
	
	private String imageKey;
	private String accID;
	private String figurePaneLabel;

//	add to domain as needed
//	private String _accession_key;	
//	private String prefixPart;
//	private String numericPart;
//	private String _logicaldb_key;
//	private String _object_key;
//	private String _mgitype_key;
//	private String isPrivate;	
//	private String preferred;
//	private String mgiID;
//	private String subtype;
//	private String description;
//	private Date creation_date;
//	private Date modification_date;
	
}
