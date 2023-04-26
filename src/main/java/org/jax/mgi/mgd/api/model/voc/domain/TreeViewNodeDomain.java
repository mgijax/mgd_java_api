package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;
import java.lang.StringBuffer;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Tree View Node domain")
public class TreeViewNodeDomain extends BaseDomain {

	private String termKey;
	private String parentKey;

	private String id;
	private String label;
	private boolean ex;
	private String oc;
	private List<TreeViewNodeDomain> children;

	public String toJson () {
		StringBuffer b = new StringBuffer();
		b.append("{");
		b.append("\"id\":\"" + id + "\"");
		b.append(",");
		b.append("\"termKey\":\"" + termKey + "\"");
		b.append(",");
		b.append("\"label\":\"" + label + "\"");
		b.append(",");
		if (children == null) {
			b.append("\"ex\":" + ex );
		} else {
			if ("open".equals(oc)) {
			    b.append("\"oc\":\"" + oc + "\"");
			    b.append(",");
			}
			b.append("\"children\":[");
			String sep = "";
			for (TreeViewNodeDomain c : children) {
				b.append(sep);
				b.append(c.toJson());
				sep = ",";
			}
			b.append("]");
		}
		b.append("}");
		return b.toString();
	}
}
