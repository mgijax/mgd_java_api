package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "InSitu Result Image View Entity Object")
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
	private Integer pixID;
	private Integer xDim;
	private Integer yDim;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
	private Date creation_date;
	private Date modification_date;
	
	// in view but not needed at this time	
//	private int _Specimen_key;
//	private String sequenceNum; 
//	private String paneLabel;


}
