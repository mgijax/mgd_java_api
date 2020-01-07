package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DenormMarkerAnnotDomain extends BaseDomain {

	private String markerKey;
	private String markerDisplay;
	private String markerStatusKey;
	private String markerStatus;	
	private String markerTypeKey;
	private String markerType;	
	private String accID;
	private List<NoteDomain> goNote;	
	private List<DenormAnnotationDomain> annots;
	private Boolean allowEditTerm = false;
	private Integer orderBy = 0;	
}
