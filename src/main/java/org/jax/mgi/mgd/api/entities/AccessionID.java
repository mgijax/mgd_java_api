package org.jax.mgi.mgd.api.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="acc_accession")
public class AccessionID extends Base implements Serializable {
	@Id
	@Column(name="_accession_key")
	public int _accession_key;

	@Column(name="accID")
	public String accID;
	
	@Column(name="preferred")
	public Integer preferred;
	
	@Column(name="private")		// just "private" is a Java reserved word
	public Integer is_private;
	
	@Column(name="_logicaldb_key")
	public Integer _logicaldb_key;
	
	@Column(name="_object_key")
	public Integer _object_key;
	
	@Column(name="_mgitype_key")
	public Integer _mgitype_key;
	
	@Column(name="prefixPart")
	public String prefixPart;
}
