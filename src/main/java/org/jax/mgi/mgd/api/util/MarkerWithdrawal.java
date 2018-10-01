package org.jax.mgi.mgd.api.util;

import java.io.IOException;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.shr.unix.RunCommand;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.config.Configurator;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManager;

/* Is: a class that runs the pgdbutilities/bin/ei/markerWithdrawal.csh
 * with the appropriate parameters
 * 
 * server = server name (ex. bhmgidevdb01)
 * db = database name (ex. prod)
 * user = user name (ex. mgd_dbo)
 * pwd = password file; (ex. /usr/local/mgi/live/dbutils/pgdbutilities/.pgpass_1)
 *
 */
public class MarkerWithdrawal extends Configurator {
   			
	/***--- instance variables ---***/
	private String markerWithdrawal = null;
    private String server = null;
    private String db = null;
    private String user = null;
    private String pwd = null; 
    
    // Constructor
    public MarkerWithdrawal() throws ConfigException, DBException {
    	
    	SQLDataManager sqlMgr = new SQLDataManager();

    	this.markerWithdrawal = getConfigString("PG_DBUTILS") + "/bin/ei/markerWithdrawal.csh";
        this.server = sqlMgr.getServer();
        this.db = sqlMgr.getDatabase();
        this.user = sqlMgr.getUser();
        this.pwd = sqlMgr.getPasswordFile();
    }

	public void doWithdrawal(String eventKey,String eventReasonKey,String oldKey,String refKey,String addAsSynonym,String newName, String newSymbols, String newKey) throws APIException, IOException, InterruptedException {

		String command = this.markerWithdrawal;
        command = command + " -S" + this.server;
        command = command + " -D" + this.db;
        command = command + " -U" + this.user;
        command = command + " -P" + this.pwd;
		command = command + " --eventKey=" + eventKey;
		command = command + " --eventReasonKey=" + eventReasonKey;
		command = command + " --oldKey=" + oldKey;
		command = command + " --refKey=" + refKey;
		command = command + " --addAsSynonym=" + addAsSynonym;
		
		// mrk_event = rename
		if (eventKey == "2") {
			command = command + " --newName='" + newName + "'";
			command = command + " --newSymbols='" + newSymbols + "'";
		}
		
		// mrk_event = merge
		if (eventKey == "3" || eventKey == "4") {
		command = command + " --newKey=" + newKey;
		}
		
		System.out.println(command);
		RunCommand runner = RunCommand.runCommand(command);
		int ec = runner.getExitCode();
		if(ec != 0) {
			 System.err.println("doWithdrawal: returned ec != 0");
			 System.err.println(runner.getStdErr());
			 System.err.println("doWithdrawal: failed");
		}
		else {
			 System.out.println("doWithdrawal: returns ec == 0");
			 //System.out.println(runner.getStdOut());
			 System.out.println("doWithdrawal: successful");
		}
	}
}
