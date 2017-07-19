package org.jax.mgi.mgd.api.util;

import java.util.ArrayList;
import java.util.List;

public class SearchResults<T> {
	private static long startTime = System.currentTimeMillis();
	
	public List<T> items;				// list of items returned from a query
	public long total_count = 0;		// total count of items returned by the query (ignoring pagination)
	public long elapsed_ms = 0;			// number of elapsed milliseconds for query results
	public String error;				// error code
	public String message;				// more explanatory error message
	public int status_code = Constants.HTTP_OK;		// HTTP status code value
	
	public SearchResults() {}
	
	public SearchResults(List<T> items) {
		this.items = items;
		this.total_count = items.size();
		this.elapsed_ms = System.currentTimeMillis() - startTime;
	}
	
	public SearchResults(List<T> items, long total_count) {
		this.items = items;
		this.total_count = total_count;
		this.elapsed_ms = System.currentTimeMillis() - startTime;
	}
	
	public void setItem(T item) {
		this.items = new ArrayList<T>();
		this.items.add(item);
		this.total_count = this.items.size();
		this.elapsed_ms = System.currentTimeMillis() - startTime;
	}
	
	public static void resetTimer() {
		startTime = System.currentTimeMillis();
	}
	
	public void setError(String error, String message, int status_code) {
		this.error = error;
		this.message = message;
		this.status_code = status_code;
	}
}
