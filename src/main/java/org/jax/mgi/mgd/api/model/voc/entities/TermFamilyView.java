package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="voc_termfamily_view")
public class TermFamilyView extends BaseEntity {

	@Id
    @ApiModelProperty(value="primary key")
	private int _term_key;
	private int _vocab_key;
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