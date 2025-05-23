package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GelRowDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelRowDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelRow;
import org.jax.mgi.mgd.api.model.gxd.translator.GelRowTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class GelRowService extends BaseService<GelRowDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GelRowDAO gelRowDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private GelBandService gelBandService;
	
	private GelRowTranslator translator = new GelRowTranslator();				

	@Transactional
	public SearchResults<GelRowDomain> create(GelRowDomain domain, User user) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelRowDomain> update(GelRowDomain domain, User user) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelRowDomain> delete(Integer key, User user) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GelRowDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GelRowDomain domain = new GelRowDomain();
		if (gelRowDAO.get(key) != null) {
			domain = translator.translate(gelRowDAO.get(key));
		}
		gelRowDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GelRowDomain> getResults(Integer key) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setItem(translator.translate(gelRowDAO.get(key)));
		gelRowDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<GelRowDomain> rowDomain, List<GelLaneDomain> laneDomain, User user) {
		// process gel row (create, delete, update)
		
		Boolean modified = false;
		String cmd;
		Query query;
		String note = "";
		
		if (rowDomain == null || rowDomain.isEmpty()) {
			log.info("processGelRow/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < rowDomain.size(); i++) {
			
			// if gel row is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
			if (rowDomain.get(i).getGelUnitsKey() == null || rowDomain.get(i).getGelUnitsKey().isEmpty()) {
				continue;
			}
			
			if (rowDomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {

				log.info("processGelRow create");

				GelRow entity = new GelRow();

				entity.set_assay_key(parentKey);
				entity.setGelUnits(termDAO.get(Integer.valueOf(rowDomain.get(i).getGelUnitsKey())));
				entity.setSequenceNum(rowDomain.get(i).getSequenceNum());
				
				if (rowDomain.get(i).getSize() != null) {	
					entity.setSize(rowDomain.get(i).getSize());
				}
				else {
					entity.setSize(null);
				}
				
				if (rowDomain.get(i).getRowNote() != null && !rowDomain.get(i).getRowNote().isEmpty()) {
					note = DecodeString.setDecodeToLatin9(rowDomain.get(i).getRowNote());
					note = note.replace("''", "'");
					entity.setRowNote(note);
				}
				else {
					entity.setRowNote(null);					
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				gelRowDAO.persist(entity);

				// process gxd_gellane/gxd_gelband
				if (laneDomain != null && !laneDomain.isEmpty()) {
					log.info("processGelRow/call getBandService");
					for (int j = 0; j < laneDomain.size(); j++) {
						if (laneDomain.get(j).getGelControlKey() != null && !laneDomain.get(j).getGelControlKey().isEmpty()) {
							modified = gelBandService.process(entity.get_gelrow_key(), Integer.valueOf(laneDomain.get(j).getGelLaneKey()), rowDomain.get(i).getSequenceNum(), laneDomain.get(j).getGelBands(), user);
						}
					}
				}
				
				modified = true;
				log.info("processGelRow/create processed: " + entity.get_gelrow_key());					
			}
			else if (rowDomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGelRow delete");
				GelRow entity = gelRowDAO.get(Integer.valueOf(rowDomain.get(i).getGelRowKey()));
				gelRowDAO.remove(entity);
				modified = true;
				log.info("processGelRow delete successful");
			}
			else if (rowDomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGelRow update");

				GelRow entity = gelRowDAO.get(Integer.valueOf(rowDomain.get(i).getGelRowKey()));
			
				entity.set_assay_key(parentKey);
				entity.setGelUnits(termDAO.get(Integer.valueOf(rowDomain.get(i).getGelUnitsKey())));
				entity.setSequenceNum(rowDomain.get(i).getSequenceNum());

				if (rowDomain.get(i).getSize() != null) {	
					entity.setSize(rowDomain.get(i).getSize());
				}
				else {
					entity.setSize(null);
				}
				
				if (rowDomain.get(i).getRowNote() != null && !rowDomain.get(i).getRowNote().isEmpty()) {
					note = DecodeString.setDecodeToLatin9(rowDomain.get(i).getRowNote());
					note = note.replace("''", "'");
					entity.setRowNote(note);				}
				else {
					entity.setRowNote(null);					
				}
				
				entity.setModification_date(new Date());

				// process gxd_gellane/gxd_gelband
				if (laneDomain != null && !laneDomain.isEmpty()) {
					for (int j = 0; j < laneDomain.size(); j++) {
						if (laneDomain.get(j).getGelControlKey() != null && !laneDomain.get(j).getGelControlKey().isEmpty()) {
							modified = gelBandService.process(Integer.valueOf(rowDomain.get(i).getGelRowKey()), Integer.valueOf(laneDomain.get(j).getGelLaneKey()), rowDomain.get(i).getSequenceNum(), laneDomain.get(j).getGelBands(), user);
						}
					}
				}
				
				gelRowDAO.update(entity);
				modified = true;
				log.info("processGelRow/changes processed: " + rowDomain.get(i).getGelRowKey());	
			}
			else {
				log.info("processGelRow/no changes processed: " + rowDomain.get(i).getGelRowKey());
			}
		}
		
		// process order reset
		cmd = "select count(*) from MGI_resetSequenceNum ('GXD_GelRow'," + parentKey + "," + user.get_user_key() + ")";
		log.info("processGelRow/process order reset: " + cmd);
		query = gelRowDAO.createNativeQuery(cmd);
		query.getResultList();
		
		log.info("processGelRow/processing successful");
		return modified;
	}
	    
}
