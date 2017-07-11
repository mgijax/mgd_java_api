package org.jax.mgi.mgd.api.util;

import java.util.List;

public class SearchResults<T> {
	public List<T> items;				// list of items returned from a query
	public long total_count = 0;		// total count of items returned by the query (ignoring pagination)
	
	public SearchResults() {}
	
	public SearchResults(List<T> items) {
		this.items = items;
		this.total_count = items.size();
	}
	
	public SearchResults(List<T> items, long total_count) {
		this.items = items;
		this.total_count = total_count;
	}
}
