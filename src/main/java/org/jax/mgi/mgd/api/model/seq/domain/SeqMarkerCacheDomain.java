package org.jax.mgi.mgd.api.model.seq.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "SeqMarker Model Object")
public class SeqMarkerCacheDomain extends BaseDomain {

	private Integer cacheKey;
	private String accID;
	private String rawbiotype;
	private String createdBy;
	private String modifiedBy;
	private Date annotation_date;
	private Date creation_date;
	private Date modification_date;

}
