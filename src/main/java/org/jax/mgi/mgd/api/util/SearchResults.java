package org.jax.mgi.mgd.api.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;

/* Note that the most accurate way to measure timings is to use the default constructor before
 * doing your query, then use setItems() to tell the SearchResults about your result set.  Otherwise,
 * the timing will only reflect minimal work within this object, after you've already found your
 * result set.
 */
public class SearchResults<T> {
	private long startTime = 0;			// time of object creation (used to measure timings)
	public List<T> items;				// list of items returned from a query
	public long total_count = 0;		// total count of items returned by the query (ignoring pagination)
	public long all_match_count = 0;	// total count of items that could be returned by the query
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
		total_count = items.size();
		all_match_count = total_count;
		measureTime();
	}
	
	public SearchResults(List<T> items, long total_count) {
		startTime = System.currentTimeMillis();
		this.items = items;
		this.total_count = total_count;
		all_match_count = total_count;
		measureTime();
	}
	
	public SearchResults(Iterable<T> items) {
		startTime = System.currentTimeMillis();
		this.items = IteratorUtils.toList(items.iterator());
		total_count = this.items.size();
		all_match_count = total_count;
		measureTime();
	}

	public void setItems(List<T> items) {
		this.items = items;
		total_count = items.size();
		if (all_match_count < total_count) {
			all_match_count = total_count;
		}
		measureTime();
	}
	
	public void setItem(T item) {
		items = new ArrayList<T>();
		items.add(item);
		total_count = items.size();
		if (all_match_count < total_count) {
			all_match_count = total_count;
		}
		measureTime();
	}
	
	public void setAllMatchCount(long allMatches) {
		all_match_count = allMatches;
	}
	
	public void setError(String error, String message, int status_code) {
		this.error = error;
		this.message = message;
		this.status_code = status_code;
		measureTime();
	}
	
	private void measureTime() {
		elapsed_ms = System.currentTimeMillis() - startTime;
	}
}
