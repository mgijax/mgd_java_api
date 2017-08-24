package org.jax.mgi.mgd.api.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Transient;

public class Base implements Serializable {
	
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
	
}
