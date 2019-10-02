package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Term Domain")
public class TermDomain extends BaseDomain {

	private String termKey;
	private String term;
	private String vocabKey;
	private String vocabName;	
	private String abbreviation;
	private String note;
	private String sequenceNum;
	private String isObsolete;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private List<AccessionDomain> accessionIds;
	
	// Added so validators can specify to include obsolete
	// terms if desired; default is to exclude
	// this value will be empty coming back out from the Service
	private Boolean includeObsolete = Boolean.FALSE;
		
}
