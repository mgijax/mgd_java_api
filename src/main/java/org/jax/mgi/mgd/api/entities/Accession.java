package org.jax.mgi.mgd.api.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="acc_accession")
@Inheritance(strategy = InheritanceType.JOINED)
public class Accession extends Base {

	@Id
	public long _accession_key;
	public String accid;
	public long _object_key;
	public String prefixpart;
	public int numericpart;
	
	
}
