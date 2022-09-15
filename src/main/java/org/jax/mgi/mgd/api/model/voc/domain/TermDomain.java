package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Term Domain")
public class TermDomain extends BaseDomain {

	private String processStatus;
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
	private List<AccessionDomain> accessionSecondaryIds;

	// Added so validators can specify to include obsolete
	// terms if desired; default is to exclude
	// this value will be empty coming back out from the Service
	private Boolean includeObsolete = Boolean.FALSE;

	// for GO-terms only
	private String goDagAbbrev;
	
	// for all synonyms
	private List<MGISynonymDomain> synonyms;
	
	// for GORel synonyms only
	private List<MGISynonymDomain> goRelSynonyms;
	
	// for cell type term synonyms only
	private List<MGISynonymDomain> celltypeSynonyms;
	
	// translated at end of GET and search
	private List<TermDomain> dagParents;
	
	// set at the end of GET and search
	private String cellTypeAnnotCount;
}

