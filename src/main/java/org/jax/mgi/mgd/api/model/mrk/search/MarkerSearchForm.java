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
	
	@Override
	public Map<String, Object> getSearchFields() {
		Map<String, Object> ret = new HashMap<>();

		if(symbol != null) { ret.put("symbol", symbol); }
		if(name != null) { ret.put("name", name); }
		if(chromosome != null) { ret.put("chromosome", chromosome); }
		if(cytogeneticOffset != null) { ret.put("cytogeneticOffset", cytogeneticOffset); }
		if(cmOffset != null) { ret.put("cmOffset", cmOffset); }
		if(markerStatusKey != null) { ret.put("markerStatusKey", markerStatusKey); }
		if(markerTypeKey != null) { ret.put("markerTypeKey", markerTypeKey); }
		if(editorNote != null) { ret.put("editorNote", editorNote); }
		if(sequenceNote != null) { ret.put("sequenceNote", sequenceNote); }
		if(revisionNote != null) { ret.put("revisionNote", revisionNote); }
		if(strainNote != null) { ret.put("strainNote", strainNote); }
		if(locationNote != null) { ret.put("locationNote", locationNote); }

		return ret;
	}
	
}
