package org.jax.mgi.mgd.api.model.mgi.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoteTypeDomain extends BaseDomain {

	private String noteTypeKey;
	private String mgiTypeKey;
	private String noteType;
	private String isPrivate;
	private String createdByKey;
	private String modifiedByKey;
	private String creation_date;
	private String modification_date;
}   	