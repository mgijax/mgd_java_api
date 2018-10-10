package org.jax.mgi.mgd.api.util;

import java.io.IOException;


/* Is: a class that runs the pgdbutilities/bin/ei/markerWithdrawal.csh
 * with the appropriate parameters
 * 
 * markerWithdrawal = shell script in $PG_DBUTILS/bin/ei that will get executed
 * server = server name (ex. bhmgidevdb01)
 * db = database name (ex. prod)
 * user = user name (ex. mgd_dbo)
 * pwd = password file; (ex. /usr/local/mgi/live/dbutils/pgdbutilities/.pgpass_1)
 *
 */
public class MarkerWithdrawal {
   			
	/***--- instance variables ---***/
	private String markerWithdrawal = null;
    private String server = null;
    private String db = null;
    private String user = null;
    private String pwd = null; 
    
    // Constructor
    public MarkerWithdrawal() {
    	
    	this.markerWithdrawal = System.getProperty("swarm.ds.markerwithdrawal");
    	this.server = System.getProperty("swarm.ds.dbserver");
        this.db = System.getProperty("swarm.ds.dbname");
        this.user = System.getProperty("swarm.ds.username");
        this.pwd = System.getProperty("swarm.ds.dbpasswordfile");
    }

	public void doWithdrawal(String eventKey,String eventReasonKey,String oldKey,String refKey,String addAsSynonym,String newName, String newSymbols, String newKey) throws IOException, InterruptedException {

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
		if (eventKey.equals("2")) {
			command = command + " --newName='" + newName + "'";
			command = command + " --newSymbols='" + newSymbols + "'";
		}
		
		// mrk_event = merge
		if (eventKey.equals("3") || eventKey.equals("4")) {
			command = command + " --newKey=" + newKey;
		}
		
		System.out.println("COMMAND: " + command);
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
		
		return;
	}
}
