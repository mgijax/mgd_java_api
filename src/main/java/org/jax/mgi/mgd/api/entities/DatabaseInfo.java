package org.jax.mgi.mgd.api.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Database Info Model Object")
@Table(name="mgi_dbinfo")
public class DatabaseInfo extends EntityBase {

	@Id
	private String public_version;
	private String lastdump_date;
	private String product_name;
	private String schema_version;
	private String snp_schema_version;
	private String snp_data_version;
	
}
