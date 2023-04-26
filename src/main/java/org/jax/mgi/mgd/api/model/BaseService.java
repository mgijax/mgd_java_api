package org.jax.mgi.mgd.api.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseService<D extends BaseDomain> {

	protected Logger log = Logger.getLogger(getClass());

	public interface TsvFormatter {
	    public String format (ResultSet obj);
	}

	public Response download(final String cmd, final String fileName, TsvFormatter formatter) {
		log.info(cmd);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(os));
				String firstLine = formatter.format(null);
				if (firstLine != null) {
					writer.write(firstLine);
				}
				try {
					ResultSet rs = (new SQLExecutor()).executeProto(cmd);
					while (rs.next()) {
						writer.write(formatter.format(rs));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				writer.flush();
			}
		};
		
		return Response
			.ok(stream)
			.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
			.build();
	}

	protected static String addPaginationSQL(String cmd, String orderBy, int offset, int limit) {

		if (orderBy != null) {
			cmd = cmd + "\norder by " + orderBy;
		}
		if (offset >= 0) {
			cmd = cmd + "\noffset " + offset;
		}
		if (limit > 0) {
			cmd = cmd + "\nlimit " + limit;
		}		
		return cmd;
	}

        /*
         * Helper function available to subclasses implementing formatTsv.
         *
         * Formats an object as a row in a tab-delimited output.
         * Caller passes the object and a list of columns. 
         * Returns a string.
         * To get a formatted header line containing the column labels,
         * pass null as the object.
         * Args:
         *   obj - the object to format, or null
         *   cols - a list of column specifiers. Each item is
         *        a list of 2 strings, specifying the column
         *        label and the correcponding object field.
         *        Example: [["ID", "accID"], ["Gene symbol", "symbol"], ["Gene name", "name"]]
         * Uses reflection to get the named field from the object. 
         * If an exception occurs (e.g., NoSuchFieldException) for any field, the
         * field's value is empty.
         */
        protected static String formatTsvHelper (ResultSet obj, String[][] cols) {
            String separator = "\t";
            String terminator = "\n";
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < cols.length; i++) {
                String colName = cols[i][0];
                String fieldName = cols[i][1];

                String val = "";
                if (obj != null) {
		    try {
			val = obj.getString(fieldName);
			if (val == null) val = "";
		    }
		    catch (Exception e) {
		        val = "ERR";
		    }
                } else {
                    val = colName;
                }
                if (i > 0) b.append(separator);
                val = val.replaceAll("\t", " ").replaceAll("\n"," ");
                b.append(val);
            }
            b.append(terminator);
            return b.toString();
        }

        /* 
         * Formats an query row as a TSV row for the indicated endpoint.
         * OVERRIDE this function if you want to provide TSV downloads.
         */
        protected String formatTsv (ResultSet obj) {
            return "Not implemented\n";
        }

        /*
         * Override this function if desired to generate a name for the downloaded file.
         * Default file name is "<endpoint>.<arg>.tsv"
         */
        protected String getTsvFileName (String endpoint, String arg) {
	    if (arg == null) {
		return endpoint + ".tsv";
	    } else {
		return endpoint + "." + arg + ".tsv";
	    }
        }
	protected ObjectMapper mapper = new ObjectMapper();
	
	public abstract SearchResults<D> create(D object, User user) throws APIException;
	public abstract SearchResults<D> update(D object, User user);
	public abstract SearchResults<D> delete(Integer key, User user);
	public abstract D get(Integer key);
	public abstract SearchResults<D> getResults(Integer key);

}
