package org.jax.mgi.mgd.api.util;

import java.util.ArrayList;
import java.util.List;

/* Has: convenience methods for making lists
 */
public class ListMaker<T> {
	public List<T> toList(T item) {
		List<T> myList = new ArrayList<T>();
		myList.add(item);
		return myList;
	}
}
