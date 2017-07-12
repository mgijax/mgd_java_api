package org.jax.mgi.mgd.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="acc_accession")
public class Accession extends Base {

	@Id
	public long _accession_key;
	public String accid;
	public String prefixpart;
	public long numericpart;
	public long _logicaldb_key;
	public long _object_key;
	public long _mgitype_key;
	@Column(name="private")
	public long isPrivate;
	public long preferred;

}
