package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyDomain extends BaseDomain {

	private String antibodyKey;
	private String antibodyName;
	private String antibodyNote;
	private String antibodyClassKey;
	private String antibodyClass;
	private String antibodyTypeKey;
	private String antibodyType;
	private String organismKey;
	private String organism;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String accID;
	private Integer hasExpression = 0;

	private AntigenDomain antigen;
	private List<AntibodyAliasDomain> aliases;
	private List<AntibodyMarkerDomain> markers;
	private List<MGIReferenceAssocDomain> refAssocs;	
	
}
