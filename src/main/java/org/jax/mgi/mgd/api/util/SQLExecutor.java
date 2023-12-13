package org.jax.mgi.mgd.api.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.jboss.logging.Logger;

/**
 * The SQLExecutor class knows how to create connections and execute queries
 * against a given database.
 */
public class SQLExecutor {

	public Properties props = new Properties();
	protected Connection conMGD = null;

	private Date start;
	private Date end;

	protected Logger log = Logger.getLogger(getClass());

	protected String username;
	protected String password;
	protected String mgdJDBCUrl;

	/**
	 * The default constructor pulls in connection information from the property
	 * files.
	 * 
	 */

	public SQLExecutor(String mgdJDBCUrl, String username, String password) {
		this.mgdJDBCUrl = mgdJDBCUrl;
		this.username = username;
		this.password = password;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets up the connection to the MGD Database.
	 * 
	 * @throws SQLException
	 */

	private void getMGDConnection() throws SQLException {
		conMGD = DriverManager.getConnection(mgdJDBCUrl, username, password);
		conMGD.setAutoCommit(false);
	}

	/**
	 * Clean up the connections to the database, if they have been initialized.
	 * 
	 * @throws SQLException
	 */

	public void cleanup() throws SQLException {
		if (conMGD != null) {
			conMGD.close();
			conMGD = null;
		}
	}

	/**
	 * Execute a statement against MGD (where that statement has no rows returned),
	 * setting up the connection if needed.
	 * 
	 */
	public void executeUpdate(String cmd) {

		try {
			if (conMGD == null) {
				getMGDConnection();
			}

			java.sql.Statement stmt = conMGD.createStatement();
			start = new Date();
			stmt.executeUpdate(cmd);
			end = new Date();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}

	/*
	 * execute any SQL that does not return a result
	 */
	public void executeVoid(String sql) {
		try {
			if (conMGD == null)
				getMGDConnection();
			java.sql.Statement stmt = conMGD.createStatement();
			start = new Date();
			stmt.execute(sql);
			end = new Date();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Execute a query against MGD, setting up the connection if needed.
	 * 
	 * @param query
	 * @return
	 */
	public ResultSet executeProto(String query) {
		return executeProto(query, 10000);
	}

	/**
	 * Execute a query against MGD, setting up the connection if needed. Use a
	 * cursor to return 'cursorLimit' results at a time.
	 * 
	 * @param query
	 * @return
	 */
	public ResultSet executeProto(String query, int cursorLimit) {

		ResultSet set;

		try {
			if (conMGD == null) {
				getMGDConnection();
			}

			java.sql.Statement stmt = conMGD.createStatement();
			if (cursorLimit > 0) {
				stmt.setFetchSize(cursorLimit);
			}
			// start = new Date();
			// log.info("start executeQuery:" + start);
			set = stmt.executeQuery(query);
			// end = new Date();
			// log.info("end executeQuery:" + end);
			return set;
		} catch (Exception e) {
			e.printStackTrace();
			// System.exit(1);
			return null;
		}
	}

	/**
	 * Return the timing of the last query.
	 * 
	 * @return
	 */

	public long getTiming() {
		return end.getTime() - start.getTime();
	}

	/*
	 * returns a formatted timestamp as a string, showing the last query's execution
	 * time in ms. format: "(n ms)"
	 */
	public String getTimestamp() {
		return getTiming() + " ms";
	}

	@Override
	public String toString() {
		return "SQLExecutor[username=" + username + ",password=" + password + ",url=" + mgdJDBCUrl + "]";
	}

}
