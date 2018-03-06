package org.jax.mgi.mgd.api.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class BaseEntity implements Serializable {
	protected SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
	public BaseEntity() { }
}
