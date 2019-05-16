package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceBookDomain extends BaseDomain {
	
	private String processStatus;	
	private String refsKey;
	private String book_author;
	private String book_title;
	private String place;
	private String publisher;
	private String series_edition;	
	private String creation_date;
	private String modification_date;

}
