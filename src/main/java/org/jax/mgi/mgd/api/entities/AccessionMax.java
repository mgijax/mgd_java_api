package org.jax.mgi.mgd.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Singleton;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Singleton
@Entity
@Table(name="acc_accessionmax")
public class AccessionMax extends Base implements Serializable {
	@Id
	@Column(name="prefixPart")
	public String prefixPart;

	@Column(name="maxNumericPart")
	public Long maxNumericPart;
	
	@Column(name="creation_date")
	public Date creation_date;
	
	@Column(name="modification_date")
	public Date modification_date;
}
