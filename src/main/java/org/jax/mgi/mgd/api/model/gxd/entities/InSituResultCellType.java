package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "InSitu Result Cell Type Model Object")
@Table(name="gxd_isresultcelltype")
public class InSituResultCellType extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_isresultcelltype_generator")
	@SequenceGenerator(name="gxd_isresultcelltype_generator", sequenceName = "gxd_isresultcelltype_seq", allocationSize=1)
	@Schema(name="primary key")
	private int _resultcelltype_key;
	private int _result_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_celltype_term_key", referencedColumnName="_term_key")
	private Term celltypeTerm;

}
