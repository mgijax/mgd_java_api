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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor 
@Entity
@Table(name="acc_accession")
public class AccessionID extends EntityBase implements Serializable {
	@Id
	@Column(name="_accession_key")
	private long _accession_key;

	@Column(name="accID")
	private String accID;
	
	@Column(name="preferred")
	private Integer preferred;
	
	@Column(name="private")		// just "private" is a Java reserved word
	private Integer is_private;
	
	@Column(name="_logicaldb_key")
	private Integer _logicaldb_key;
	
	@Column(name="_object_key")
	private Long _object_key;
	
	@Column(name="_mgitype_key")
	private Integer _mgitype_key;
	
	@Column(name="prefixPart")
	private String prefixPart;

	@Column(name="numericPart")
	private Long numericPart;

	@Column(name="creation_date")
	private Date creation_date;
	
	@Column(name="modification_date")
	private Date modification_date;

	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByUser;
}
