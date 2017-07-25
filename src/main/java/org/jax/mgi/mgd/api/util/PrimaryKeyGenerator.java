package org.jax.mgi.mgd.api.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/* Is: a generator of new (integer) primary key values for a table
 * Has: a table name and an integer value of the current highest key
 * Does: interrogates the database to find the current highest key for a table, generates new
 * 	keys in memory, and periodically double-checks to pull the latest from the database when
 * 	there are periods of inactivity (like overnight when the PWI is down -- we'd want to pull
 * 	the latest keys from the db in the morning, in case loads ran overnight and added rows).
 */
public class PrimaryKeyGenerator {
	private static long timeout = 10 * 60 * 1000;				// ten minute timeout (in ms)

	@PersistenceContext(unitName="primary")	
	protected EntityManager entityManager;
	
	private static Map<String, PrimaryKeyGenerator> cache = new HashMap<String, PrimaryKeyGenerator>();
	
	private String dbTable;						// name of model class for which we're managing the key
	private String keyName;						// name of primary key field
	private long key = 0;						// current maximum primary key for this table
	private long nextRefreshTime = 0;			// time (in ms) when to next refresh key from database
	
	private PrimaryKeyGenerator(String dbTable, String keyName) {
		this.dbTable = dbTable;
		this.keyName = keyName;
	}
	
	public long getNextKey() {
		if (System.currentTimeMillis() > this.nextRefreshTime) {
			TypedQuery<Long> q1 = entityManager.createQuery("select max(" + keyName + ") from " + dbTable, Long.class);
			this.key = q1.getSingleResult();
		}
		this.nextRefreshTime = System.currentTimeMillis() + timeout;
		this.key = this.key + 1;
		return this.key;
	}
	
	public static PrimaryKeyGenerator getPrimaryKeyGenerator(String dbTable, String keyName) {
		if (!cache.containsKey(dbTable)) {
			cache.put(dbTable, new PrimaryKeyGenerator(dbTable, keyName));
		}
		return cache.get(dbTable);
	}
}
