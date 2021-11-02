package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIKeyValueDomain extends BaseDomain {

	private String _keyvalue_key;
	private String _object_key;
	private String _mgitype_key;
	private String sequenceNum;
	private String key;
	private String value;
	
}   	