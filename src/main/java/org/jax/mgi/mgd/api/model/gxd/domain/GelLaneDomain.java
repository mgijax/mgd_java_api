package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GelLaneDomain extends BaseDomain {

	private Integer _gellane_key;
	private Integer sequenceNum;
	private String laneLabel;
	private String sampleAmount;
	private String sex;
	private String age;
	private Integer ageMin;
	private Integer ageMax;
	private String ageNote;
	private String laneNote;
	private Date creation_date;
	private Date modification_date;
	
}
