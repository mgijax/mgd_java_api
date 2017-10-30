package org.jax.mgi.mgd.api.util;

import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

/* Purpose: provide Java integration of full-text searching in postgres
 * Notes: adapted from http://java-talks.blogspot.com/2014/04/use-postgresql-full-text-search-with-hql.html
 */
public class PostgresFTSFunction implements SQLFunction {

	@Override
	public Type getReturnType(Type arg0, Mapping arg1) throws QueryException {
		return new BooleanType();
	}

	@Override
	public boolean hasArguments() {
		return true;
	}

	@Override
	public boolean hasParenthesesIfNoArguments() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String render(Type outputType, List args, SessionFactoryImplementor factory) throws QueryException {
		if (args == null) {
			throw new IllegalArgumentException("args parameter cannot be null");
		}
	    if ((args.size() < 2) || (args.size() > 3)) {
	        throw new IllegalArgumentException("args must contain 2 or 3 arguments");
	    }
	 
	    String fragment = null;
	    String ftsConfig = null;
	    String field = null;
	    String value = null;

	    if(args.size() == 3) {
	        ftsConfig = (String) args.get(0);
	        field = (String) args.get(1);
	        value = (String) args.get(2);
	        fragment = field + " @@ to_tsquery(" + ftsConfig + ", " + value + ")";
	    } else {
	    	field = (String) args.get(0);
	        value = (String) args.get(1);
	        fragment = field + " @@ to_tsquery(" + value + ")";
	    }
	    return fragment;
	}
}
