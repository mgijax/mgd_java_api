package org.jax.mgi.mgd.api.util;

import java.util.ArrayList;
import java.util.List;

/* Note that the most accurate way to measure timings is to use the default constructor before
 * doing your query, then use setItems() to tell the SearchResults about your result set.  Otherwise,
 * the timing will only reflect minimal work within this object, after you've already found your
 * result set.
 */
public class SearchResults<T> {
	private long startTime;				// time of object creation (used to measure timings)
	public List<T> items;				// list of items returned from a query
	public long total_count = 0;		// total count of items returned by the query (ignoring pagination)
	public long elapsed_ms = 0;			// number of elapsed milliseconds for query results
	public String error;				// error code
	public String message;				// more explanatory error message
	public int status_code = Constants.HTTP_OK;		// HTTP status code value
	
	public SearchResults() {
		startTime = System.currentTimeMillis();
	}
	
	public SearchResults(List<T> items) {
		startTime = System.currentTimeMillis();
		this.items = items;
		this.total_count = items.size();
		this.measureTime();
	}
	
	public SearchResults(List<T> items, long total_count) {
		startTime = System.currentTimeMillis();
		this.items = items;
		this.total_count = total_count;
		this.measureTime();
	}
	
	public void setItems(List<T> items) {
		this.items = items;
		this.total_count = this.items.size();
		this.measureTime();
	}
	
	public void setItem(T item) {
		this.items = new ArrayList<T>();
		this.items.add(item);
		this.total_count = this.items.size();
		this.measureTime();
	}
	
	public void setError(String error, String message, int status_code) {
		this.error = error;
		this.message = message;
		this.status_code = status_code;
		this.measureTime();
	}
	
	private void measureTime() {
		this.elapsed_ms = System.currentTimeMillis() - startTime;
	}
}
