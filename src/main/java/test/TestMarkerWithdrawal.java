package test;

import java.io.IOException;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.util.MarkerWithdrawal;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;

// rename : eventKey=2, eventReasonKey=1, oldKey=27555 (Zan), refKey=22864, 
//			addAsSynonym=1,
//          newName="Zan new name", newSymbos = "Zan-new",
//			newKey=""
//
// merge :  eventKey=3, eventReasonKey=2, oldKey=55661 (0610009K14Rik), refKey=22864, 
//			addAsSynonym=1,
//			newName="", newSymbos = "",
//			newKey="52348" (0610025J13Rik)
//
// delete : eventKey=6, eventReasonKey=-1, oldKey=55675 (0910001E24Rik), refKey=22864, 
//			addAsSynonym=1,
//			newName="", newSymbos = "",
//			newKey=""
//
// allele of: eventKey=4, eventReasonKey=-1, oldKey=14881 (z), refKey=22864,
//		addAsSynonym=1,
//		newName="", newSymbos = "",
//		newKey="27555" (Zan-new)
//

public class TestMarkerWithdrawal
{
    public static void main( String[] args ) throws APIException, IOException, InterruptedException, ConfigException, DBException { 

    	System.out.println("********************");
    	System.out.println("in class TestMarkerWithdarwal");
    	System.out.println(System.getProperty("swarm.markerWithdrawal"));
    	System.out.println("********************");
    	
    	MarkerWithdrawal mwRename = new MarkerWithdrawal();
        mwRename.doWithdrawal("2", "1", "27555", "22864", "1", "Zan new5 name", "Zan-new-5", "");
        
        //MarkerWithdrawal mwMerge = new MarkerWithdrawal();
        //mwMerge.doWithdrawal("3", "2", "55661", "22864", "1", "", "", "52348");
        
        //MarkerWithdrawal mwDelete = new MarkerWithdrawal();
        //mwDelete.doWithdrawal("6", "-1", "55675", "22864", "1", "", "", "");
        
        //MarkerWithdrawal mwAlleleOf = new MarkerWithdrawal();
        //mwAlleleOf.doWithdrawal("4", "-1", "14881", "22864", "1", "", "", "27555");
        
    }
}
