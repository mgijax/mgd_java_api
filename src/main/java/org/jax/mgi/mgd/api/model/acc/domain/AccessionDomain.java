package org.jax.mgi.mgd.api.model.acc.domain;

import java.util.Date;

import javax.persistence.Column;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Accession Model Object")
public class AccessionDomain extends BaseDomain {

	private int _accession_key;
	private String accID;
	private String prefixPart;
	private Integer numericPart;
	private Integer _logicaldb_key;
	private Integer _object_key;
	private Integer _mgitype_key;
	@Column(name="private")		// just "private" is a Java reserved word
	private Integer is_private;
	private Integer preferred;
	private String createdByUser;
	private String modifiedByUser;
	private Date creation_date;
	private Date modification_date;
}
