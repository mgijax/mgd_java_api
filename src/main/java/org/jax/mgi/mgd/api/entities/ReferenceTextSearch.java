package org.jax.mgi.mgd.api.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Table specifically for text searching full text of references")
@Table(name="bib_textsearch")
public class ReferenceTextSearch extends EntityBase {
	@Id
	@Column(name="_refs_key")
	private int _refs_key;

	@Column(name="refText")
	private String refText;
}
