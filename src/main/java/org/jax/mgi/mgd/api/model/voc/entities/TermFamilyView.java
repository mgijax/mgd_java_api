package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "voc_termfamily_view")
public class TermFamilyView extends BaseEntity {

	@Id
	@Schema(name = "primary key") private int _term_key;
	private int _vocab_key;
	private String searchid;
	private String accid;
	private String term;
	private String abbreviation;
	private String note;
	private Integer sequenceNum;
	private Integer isObsolete;
	private int _creatdby_key;
	private int _modifiedby_key;
	private Date creation_date;
	private Date modification_date;

}
