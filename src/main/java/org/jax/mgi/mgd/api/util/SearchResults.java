package org.jax.mgi.mgd.api.util;

import java.util.List;

public class SearchResults<T> {
	private static long startTime = System.currentTimeMillis();
	
	public List<T> items;				// list of items returned from a query
	public long total_count = 0;		// total count of items returned by the query (ignoring pagination)
	public long elapsed_ms = 0;			// number of elapsed milliseconds for query results
	
	private SearchResults() {}
	
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
	
	public static void resetTimer() {
		startTime = System.currentTimeMillis();
	}
}
