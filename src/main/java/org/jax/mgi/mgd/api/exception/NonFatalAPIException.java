package org.jax.mgi.mgd.api.exception;

/* Is: an APIException that is not fatal (the request may succeed if retried)
 */
public class NonFatalAPIException extends APIException {
	public NonFatalAPIException(String message) {
		super(message);
	}
}
