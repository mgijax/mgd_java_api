package org.jax.mgi.mgd.api.model.mrk.search;

import java.util.HashMap;
import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseSearchForm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ApiModel("Marker Search Form")
public class MarkerSearchForm extends BaseSearchForm {

	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String cmOffset;
	private Integer markerStatusKey;
	private Integer markerTypeKey;
	private String editorNote;
	private String sequenceNote;
	private String revisionNote;
	private String strainNote;
	private String locationNote;
	private String createdBy;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	// marker accession ids
	private String accID;
	
	// history
	private String historySymbol;
	private String historyName;
	private String historyEventDate;
	private String historyRefKey;
	private String historyShortCitation;
	private String historyEvent;
	private String historyEventReason;
	private String historyModifiedBy;
	
	// synonym
	private String synonymName;
	private String synonymRefKey;
	
	// reference
	private String refAssocRefKey;
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();

		if(symbol != null && symbol != "") { ret.put("symbol", symbol); }
		if(name != null && name != "") { ret.put("name", name); }
		if(chromosome != null && chromosome != "") { ret.put("chromosome", chromosome); }
		if(cytogeneticOffset != null && cytogeneticOffset != "") { ret.put("cytogeneticOffset", cytogeneticOffset); }
		if(cmOffset != null && cmOffset != "") { ret.put("cmOffset", cmOffset); }
		if(markerStatusKey != null) { ret.put("markerStatusKey", markerStatusKey); }
		if(markerTypeKey != null) { ret.put("markerTypeKey", markerTypeKey); }
		if(editorNote != null && editorNote != "") { ret.put("editorNote", editorNote); }
		if(sequenceNote != null && sequenceNote != "") { ret.put("sequenceNote", sequenceNote); }
		if(revisionNote != null && revisionNote != "") { ret.put("revisionNote", revisionNote); }
		if(strainNote != null && strainNote != "") { ret.put("strainNote", strainNote); }
		if(locationNote != null && locationNote != "") { ret.put("locationNote", locationNote); }
		if(createdBy != null && createdBy != "") { ret.put("createdBy",  createdBy); }
		if(modifiedBy != null && modifiedBy != "") { ret.put("modifiedBy",  modifiedBy); }
		if(creation_date != null) { ret.put("creation_date",  creation_date); }
		if(modification_date != null) { ret.put("modification_date",  modification_date); }
		
		// marker accession ids
		if(accID != null && accID != "") { ret.put("accID",  accID); }

		// history
		if(historySymbol != null && historySymbol != "") { ret.put("historySymbol",  historySymbol); }
		if(historyName != null && historyName != "") { ret.put("historyName",  historyName); }
		if(historyEventDate != null && historyEventDate != "") { ret.put("historyEventDate",  historyEventDate); }
		if(historyRefKey != null && historyRefKey != "") { ret.put("historyRefKey",  historyRefKey); }
		if(historyShortCitation != null && historyShortCitation != "") { ret.put("historyShortCitation",  historyShortCitation); }
		if(historyEvent != null && historyEvent != "") { ret.put("historyEvent",  historyEvent); }
		if(historyEventReason != null && historyEventReason != "") { ret.put("historyEventReason",  historyEventReason); }
		if(historyModifiedBy != null && historyModifiedBy != "") { ret.put("historyModifiedBy",  historyModifiedBy); }

		// synonym
		if(synonymName != null && synonymName != "") { ret.put("synonymName",  synonymName); }
		if(synonymRefKey != null && synonymRefKey != "") { ret.put("synonymRefKey",  synonymRefKey); }

		// reference
		if(refAssocRefKey != null && refAssocRefKey != "") { ret.put("refAssocRefKey",  refAssocRefKey); }

		return ret;
	}
	
}
