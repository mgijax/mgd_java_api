package org.jax.mgi.mgd.api.model.mgi.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Database Info Model Object")
@Table(name="mgi_dbinfo")
public class DatabaseInfo extends BaseEntity {

	@Id
	private String public_version;
	private String product_name;
	private String schema_version;
	private String snp_schema_version;
	private String snp_data_version;
	private String lastdump_date;

}
