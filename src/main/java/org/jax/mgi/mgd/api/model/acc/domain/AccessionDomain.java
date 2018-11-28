package org.jax.mgi.mgd.api.model.acc.domain;

import javax.persistence.Column;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Accession Domain")
public class AccessionDomain extends BaseDomain {

	private int _accession_key;
	private String accessionKey;
	private String logicaldbKey;
	private String objectKey;
	private String mgiTypeKey;
	
	private String accID;
	private String prefixPart;
	private Integer numericPart;
	@Column(name="private")		// just "private" is a Java reserved word
	private Integer is_private;
	private Integer preferred;
	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}
