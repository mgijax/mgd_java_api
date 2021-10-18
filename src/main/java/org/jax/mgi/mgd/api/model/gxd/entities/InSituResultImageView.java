package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "InSitu Result Image View Entity Object")
@Table(name="gxd_isresultimage_view")
public class InSituResultImageView extends BaseEntity {
	
	// this view contain a concatenated "figureLabel" + "paneLabel"
	
	@Id
	private int _resultimage_key;
	private int _result_key;
	private int _imagepane_key;
	private int _image_key;
	private String figurepaneLabel;
	private String accID;
	private Date creation_date;
	private Date modification_date;
	
	// in view but not needed at this time	
//	private int _Specimen_key;
//	private String sequenceNum; 
//	private String paneLabel;
//	private int xDim;
//	private int yDim;

}
