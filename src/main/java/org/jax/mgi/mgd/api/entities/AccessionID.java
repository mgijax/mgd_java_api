package org.jax.mgi.mgd.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="acc_accession")
public class AccessionID extends Base implements Serializable {
	@Id
	@Column(name="_accession_key")
	public long _accession_key;

	@Column(name="accID")
	public String accID;
	
	@Column(name="preferred")
	public Integer preferred;
	
	@Column(name="private")		// just "private" is a Java reserved word
	public Integer is_private;
	
	@Column(name="_logicaldb_key")
	public Integer _logicaldb_key;
	
	@Column(name="_object_key")
	public Long _object_key;
	
	@Column(name="_mgitype_key")
	public Integer _mgitype_key;
	
	@Column(name="prefixPart")
	public String prefixPart;

	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;

	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	public User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	public User modifiedByUser;
}
