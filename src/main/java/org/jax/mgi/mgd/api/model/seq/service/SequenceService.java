package org.jax.mgi.mgd.api.model.seq.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.dao.SequenceDAO;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SummarySeqDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class SequenceService extends BaseService<SequenceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private SequenceDAO sequenceDAO;

	//private SequenceTranslator translator = new SequenceTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<SequenceDomain> create(SequenceDomain object, User user) {
		SearchResults<SequenceDomain> results = new SearchResults<SequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<SequenceDomain> update(SequenceDomain object, User user) {
		SearchResults<SequenceDomain> results = new SearchResults<SequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<SequenceDomain> delete(Integer key, User user) {
		SearchResults<SequenceDomain> results = new SearchResults<SequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SequenceDomain get(Integer key) {
		// NOT IMPLEMENTED		
		// get the DAO/entity and translate -> domain
		SequenceDomain domain = new SequenceDomain();
//		if (sequenceDAO.get(key) != null) {
//			domain = translator.translate(sequenceDAO.get(key));
//		}
		sequenceDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<SequenceDomain> getResults(Integer key) {
        SearchResults<SequenceDomain> results = new SearchResults<SequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        sequenceDAO.clear();
        return results;
    }

	@Transactional	
	public List<SequenceDomain> search() {
		// NOT IMPLEMENTED

		List<SequenceDomain> results = new ArrayList<SequenceDomain>();

//		String cmd = "select * from seq_sequenc";
//		log.info(cmd);
//
//		try {
//			ResultSet rs = sqlExecutor.executeProto(cmd);
//			while (rs.next()) {
//				SequenceDomain domain = new SequenceDomain();
//				domain = translator.translate(sequenceDAO.get(rs.getInt("_marker_type_key")));
//				sequenceDAO.clear();
//				results.add(domain);
//			}
//			sqlExecutor.cleanup();
//		}
//		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	

	public String getSequenceByMarkerSQL(String accid, int offset, int limit, boolean returnCount) {
	    String cmd;
	    if (returnCount) {
		cmd = "\nselect count(*) as total_count" + 
			"\nfrom seq_marker_cache s, acc_accession aa" + 
	    		"\nwhere s._marker_key = aa._object_key" + 
			"\nand aa._mgitype_key = 2" + 
			"\nand aa._logicaldb_key = 1" + 
			"\nand aa.accid = '" + accid + "'"; 
	    } else {
	    	cmd = "select " +
		"\n  s._sequence_key, " +
		"\n  s.accid, " +
		"\n  REPLACE(sa.url,'@@@@', s.accid) as url, " +
		"\n  t1.term as sequenceType, " +
		"\n  ss.length, " +
		"\n  ss.description, " +
		"\n  pss.strain, " +
		"\n  array_to_string(array_agg(DISTINCT concat(m.symbol, '|', aa.accid)), ','::text) AS markers" +
		"\nfrom " +
		"\n  seq_marker_cache s, " +
		"\n  acc_logicaldb sd, " +
		"\n  acc_actualdb sa, " +
		"\n  voc_term t1, " +
		"\n  seq_sequence ss, " +
		"\n  mrk_marker m, " +
		"\n  seq_source_assoc sr, " +
		"\n  prb_source pso, " +
		"\n  prb_strain pss, " +
		"\n  acc_accession aa," +
		"\n  seq_marker_cache s2, " +
		"\n  acc_accession aa2" +
		"\nwhere s._sequencetype_key = t1._term_key" +
		"\nand s._logicaldb_key = sd._logicaldb_key" +
		"\nand s._sequence_key = ss._sequence_key" +
		"\nand s._marker_key = m._marker_key" +
		"\nand s._organism_key = 1 " +
		"\nand s._sequence_key = sr._sequence_key" +
		"\nand s._logicaldb_key = sa._logicaldb_key" +
		"\nand not (sa._logicaldb_key in (13,41) and sa.name != 'UniProt')" +
		"\nand not (sa._logicaldb_key = 9 and sa.name != 'GenBank')" +
		"\nand sr._source_key = pso._source_key" +
		"\nand pso._strain_key = pss._strain_key" +
		"\nand m._marker_key = aa._object_key" +
		"\nand aa._mgitype_key = 2 " +
		"\nand aa._logicaldb_key = 1" +
		"\nand aa.preferred = 1" +
		"\nand s._sequence_key = s2._sequence_key" +
		"\nand s2._marker_key = aa2._object_key" +
		"\nand aa2._mgitype_key = 2" +
		"\nand aa2._logicaldb_key = 1" +
		"\nand aa2.accid = '" + accid + "'" +
		"\ngroup by s._sequence_key, s.accid, sd.name, sa.url, t1.term, t1.sequencenum, ss.length, ss.description, pss.strain" +
		"";
		cmd = addPaginationSQL(cmd, "t1.sequencenum, sd.name, ss.length desc", offset, limit);
	    }
	    return cmd;
	}

	@Transactional	
	public SearchResults<SummarySeqDomain> getSequenceByMarker(String accid, int offset, int limit) {
		// return list of sequence domains by reference marker id

		SearchResults<SummarySeqDomain> results = new SearchResults<SummarySeqDomain>();
		List<SummarySeqDomain> summaryResults = new ArrayList<SummarySeqDomain>();
		
		String cmd = getSequenceByMarkerSQL(accid, offset, limit, true);
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getLong("total_count");
				results.offset = offset;
				results.limit = limit;
				sequenceDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		cmd = getSequenceByMarkerSQL(accid, offset, limit, false);
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummarySeqDomain domain = new SummarySeqDomain();
				domain.setAccID(rs.getString("accid"));
				domain.setUrl(rs.getString("url"));
				domain.setSequenceType(rs.getString("sequenceType"));
				domain.setLength(rs.getString("length"));
				domain.setStrain(rs.getString("strain"));
				domain.setMarkers(rs.getString("markers"));
				domain.setDescription(rs.getString("description"));
				summaryResults.add(domain);
				sequenceDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		results.items = summaryResults;
		return results;
	}

	public Response downloadSequenceByMarker (String accid) {
	    String cmd = getSequenceByMarkerSQL(accid, -1, -1, false);
	    return download(cmd, getTsvFileName("getSequenceByMarker", accid), new ReferenceFormatter());
	}

	public static class ReferenceFormatter implements TsvFormatter {
		public String format (ResultSet obj) {
            		String[][] cols = {
                		{"ID",             "accID"},
                		{"Type",           "sequenceType"},
                		{"Length",     	   "length"},
                		{"Strain/Species", "strain"},
                		{"Description",    "description"},
                		{"Markers",         "markers"}
			};
            		return formatTsvHelper(obj, cols);
		}
	}

}
