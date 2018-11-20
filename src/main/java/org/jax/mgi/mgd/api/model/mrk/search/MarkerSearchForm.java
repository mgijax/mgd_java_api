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
	private String accID;
	
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
		if(accID != null && accID != "") { ret.put("accID",  accID); }

		return ret;
	}
	
}
