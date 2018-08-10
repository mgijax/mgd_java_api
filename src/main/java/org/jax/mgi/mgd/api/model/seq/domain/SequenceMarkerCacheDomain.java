package org.jax.mgi.mgd.api.model.seq.domain;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Sequence Marker Cache Model Object")
public class SequenceMarkerCacheDomain extends BaseDomain {

	private Integer _cache_key;
	//private Integer _sequence_key;
	//private Integer _marker_key;
	//private Integer _organism_key;
	//private Integer _refs_key;
	//private Integer _qualifier_key;
	//private Integer _sequenceprovider_key;
	//private Integer _logicaldb_key;
	//private Integer _marker_Type_key;
	//private Integer _biotypeconflict_key;
	private String accid;
	private String rawbiotype;
	//private String createdBy;
	//private String modifiedBy;
	private Date annotation_date;
	private Date creation_date;
	private Date modification_date;
	
	private String logicalDB;
	private List<String> actualURLs;

}
