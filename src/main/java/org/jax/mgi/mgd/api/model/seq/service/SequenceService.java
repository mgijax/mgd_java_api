package org.jax.mgi.mgd.api.model.seq.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

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

	@Transactional	
	public SearchResults<SummarySeqDomain> getSequenceByMarker(String accid, int offset, int limit) {
		// return list of sequence domains by reference marker id

		SearchResults<SummarySeqDomain> results = new SearchResults<SummarySeqDomain>();
		List<SummarySeqDomain> summaryResults = new ArrayList<SummarySeqDomain>();
		
		String cmd = "\nselect count(*) as total_count" + 
				"\nfrom seq_marker_cache s, acc_accession aa" + 
				"\nwhere s._marker_key = aa._object_key" + 
				"\nand aa._mgitype_key = 2" + 
				"\nand aa.accid = '" + accid + "'"; 
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
		
		cmd = "\nselect distinct s._sequence_key, s.accid, t1.term as sequenceType, ss.length, ss.description, m.symbol, pss.strain, aa.accid as markerAccid" + 
				"\nfrom seq_marker_cache s, voc_term t1, seq_sequence ss, mrk_marker m, seq_source_assoc sr, prb_source pso, prb_strain pss, acc_accession aa" + 
				"\nwhere s._sequencetype_key = t1._term_key" + 
				"\nand s._sequence_key = ss._sequence_key" + 
				"\nand s._marker_key = m._marker_key" + 
				"\nand s._organism_key = 1" + 
				"\nand s._sequence_key = sr._sequence_key" + 
				"\nand sr._source_key = pso._source_key" +
				"\nand pso._strain_key = pss._strain_key" +
				"\nand m._marker_key = aa._object_key" + 
				"\nand aa._mgitype_key = 2" + 
				"\nand aa.accid = '" + accid + "'"; 
		
        cmd = cmd + "\norder by s._sequence_key";

		if (offset >= 0) {
            cmd = cmd + "\noffset " + offset;
		}
        if (limit > 0) {
        	cmd = cmd + "\nlimit " + limit;
        }
	
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummarySeqDomain domain = new SummarySeqDomain();
				domain.setAccID(rs.getString("accid"));
				domain.setSequenceType(rs.getString("sequenceType"));
				domain.setLength(rs.getString("length"));
				domain.setStrain(rs.getString("strain"));
				domain.setMarkerSymbol(rs.getString("symbol"));
				domain.setMarkerAccID(rs.getString("markerAccid"));				
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
		
}
