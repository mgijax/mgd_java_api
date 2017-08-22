package org.jax.mgi.mgd.api.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Database Info Model Object")
@Table(name="mgi_dbinfo")
public class DatabaseInfo extends Base {

	@Id
	public String public_version;
	public String lastdump_date;
	public String product_name;
	public String schema_version;
	public String snp_schema_version;
	public String snp_data_version;
	
}
