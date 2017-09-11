package org.jax.mgi.mgd.api.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference/Marker Association Model Object")
@Table(name="mrk_reference")
public class ReferenceMarkerAssociation extends EntityBase {

	// need to define composite primary key class
	@Embeddable
	public static class ReferenceMarkerCompositeKey implements Serializable {
		private static final long serialVersionUID = 1L;

		protected int _refs_key;
		protected int _marker_key;
		
		public ReferenceMarkerCompositeKey() {
			this._refs_key = 0;
			this._marker_key = 0;
		}
		
		public void set_marker_key(int _marker_key) {
			this._marker_key = _marker_key;
		}
		
		public void set_refs_key(int _refs_key) {
			this._refs_key = _refs_key;
		}
		
		public ReferenceMarkerCompositeKey(int _refs_key, int _marker_key) {
			this._refs_key = _refs_key;
			this._marker_key = _marker_key;
		}
	}
	
	@EmbeddedId
	private ReferenceMarkerCompositeKey keys;

	@Column(name="jnumid")
	private String jnumID;

	@OneToOne (targetEntity=AccessionID.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key", referencedColumnName="_object_key", insertable=false, updatable=false)
	@Where(clause="_mgitype_key = 2 and _logicaldb_key = 1 and preferred = 1")
	private AccessionID markerID;
}
