package org.jax.mgi.mgd.api.model.mld.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExptsDomain extends BaseDomain {

	private String exptKey;
	private String exptType;
	private Integer tag;
	private String chromosome;
	private String refsKey;
	private String jnumid;
	private Integer jnum;
	private String short_citation;
	private String creation_date;
	private String modification_date;
	private String accID;
	
	private List<SlimAccessionDomain> mgiAccessionIds;
	private MappingNoteDomain referenceNote;	
    private List<ExptMarkerDomain> markers;
    private ExptNoteDomain exptNote;
}
