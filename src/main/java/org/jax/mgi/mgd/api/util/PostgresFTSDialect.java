package org.jax.mgi.mgd.api.util;

import org.hibernate.dialect.PostgreSQLDialect;

/* Purpose: extend standard Postgres dialect to include full-text searching as function fts()
 * Notes: adapted from http://java-talks.blogspot.com/2014/04/use-postgresql-full-text-search-with-hql.html
 */
@SuppressWarnings("deprecation")
public class PostgresFTSDialect extends PostgreSQLDialect {
	public PostgresFTSDialect() {
		registerFunction("fts", new PostgresFTSFunction());
	}
}
