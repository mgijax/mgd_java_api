package org.jax.mgi.mgd.api.model.img.domain;

import java.io.File;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageSubmissionFileDomain extends BaseDomain {
	
	private String imageKey;
	private File fileJPG;
	private String fileName;

}
