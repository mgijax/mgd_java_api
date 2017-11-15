package org.jax.mgi.mgd.api.exception;

/* Is: an APIException that is fatal (the request cannot succeed if retried)
 */
public class FatalAPIException extends APIException {
	public FatalAPIException(String message) {
		super(message);
	}
}
