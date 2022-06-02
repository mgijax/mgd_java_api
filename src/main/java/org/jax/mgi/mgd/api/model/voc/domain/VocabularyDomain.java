package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel
public class VocabularyDomain extends BaseDomain {

	private String vocabKey;
	// for backward compatibilty with gxd/ht
	//private int _vocab_key;
	private int isSimple;
	private int isPrivate;
	private String name;
	private ReferenceCitationCacheDomain reference; 
	private String creation_date;
	private String modification_date;
	private List<TermDomain> terms;

}