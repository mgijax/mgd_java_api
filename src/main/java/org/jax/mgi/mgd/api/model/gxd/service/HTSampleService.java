package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.HTSampleDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.TheilerStageDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.gxd.translator.HTSampleTranslator;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberCellTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberEmapaDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class HTSampleService extends BaseService<HTSampleDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private HTSampleDAO htSampleDAO;
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private TheilerStageDAO theilerStageDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private NoteService noteService;
	
	private HTSampleTranslator translator = new HTSampleTranslator();

	@Transactional
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
		
	@Transactional
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {	
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		log.info("processHTSample/delete");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public HTSampleDomain get(Integer key) {
		log.info("processHTSample/get");
		// get the DAO/entity and translate -> domain
		HTSampleDomain domain = new HTSampleDomain();
		HTSample entity = htSampleDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}

    @Transactional
    public SearchResults<HTSampleDomain> getResults(Integer key) {
        SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
        results.setItem(translator.translate(htSampleDAO.get(key)));
        return results;
    } 

	@Transactional
	public Boolean process(Integer parentKey, Integer experimentTypeKey, List<HTSampleDomain> domain, User user) {
		// process ht sample (create, delete, update)
		
		Boolean modified = false;
		int NS = 114866227;
		int NA = 114866228;
		int TRANSBYARRAY = 20475436;
		int NOTRESOLVED = 20475438;
		Integer RELYES = 20475450;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processHTSample/nothing to process");
			return modified;
		}
	
		for (int i = 0; i < domain.size(); i++) {
			log.info("--- :" + domain.get(i).getProcessStatus());
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if sample name is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used			
				if (domain.get(i).getName() == null || domain.get(i).getName().isEmpty()) {
					continue;
				} 
				
				log.info("processHTSample create");

				HTSample entity = new HTSample();
				
				entity.set_experiment_key(parentKey);
				entity.setOrganism(organismDAO.get(domain.get(i).get_organism_key()));
				entity.setRelevance(termDAO.get(domain.get(i).get_relevance_key()));
				entity.setSex(termDAO.get(domain.get(i).get_sex_key()));
				entity.setGenotype(genotypeDAO.get(domain.get(i).getGenotype_object().get_genotype_key()));
				entity.setName(domain.get(i).getName());
				
				if (domain.get(i).get_celltype_term_key() == null) {
					entity.setCellTypeTerm(null);
				}
				else {
					entity.setCellTypeTerm(termDAO.get(domain.get(i).get_celltype_term_key()));					
				}
				
				if (domain.get(i).get_rnaseqtype_key() == null) {
					log.info("processHTSample get_rnasetype_key is null");
					log.info("processHTSample experimenttype: " + experimentTypeKey);

					// if Experiment Type = transcription profiling by array : RNA-Seq Type = Not Applicable
					if (experimentTypeKey == TRANSBYARRAY) {
						log.info("processHTSample set NA: " + NA);
						entity.setRnaseqtype(termDAO.get(NA));
					}
					// else if Experiment Type = Not Resolved : Not Specified				
					else if (experimentTypeKey == NOTRESOLVED) {
						log.info("processHTSample set NS: " + NS);
						entity.setRnaseqtype(termDAO.get(NS));
					}
					// else if Experiment Type = RNA-Seq and Yes : Not Specified
					else if (domain.get(i).get_relevance_key() == RELYES) {
						log.info("processHTSample set NS: " + NS);						
						entity.setRnaseqtype(termDAO.get(NS));
					}
					// else if Experiment Type = RNA-Seq and !Yes : Not Applicable
					else {
						log.info("processHTSample set NA: " + NA);
						entity.setRnaseqtype(termDAO.get(NA));
					}
				}
				else {
					log.info("processHTSample get_rnasetype_key is NOT null");
					entity.setRnaseqtype(termDAO.get(domain.get(i).get_rnaseqtype_key()));
				}
				
				if (domain.get(i).getAge() == null || domain.get(i).getAge().isEmpty()) {
					entity.setAge(" ");
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);				
				} else {
					entity.setAge(domain.get(i).getAge());
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);	
				}

				// use HTEmapsDomain
				if (domain.get(i).getEmaps_object() == null) {
					entity.setTheilerStage(null);
				} else {
					entity.setTheilerStage(theilerStageDAO.get(domain.get(i).getEmaps_object().get_stage_key()));
				}
				
				if (domain.get(i).getEmaps_object() == null) {
					entity.setEmapaTerm(null);
				} else {
					entity.setEmapaTerm(termDAO.get(domain.get(i).getEmaps_object().get_emapa_term_key()));
				}
								
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				log.info("processHTSample/ updating entity");					
				//htSampleDAO.update(entity);
				htSampleDAO.persist(entity);
				log.info("processHTSample/create processed: " + entity.get_sample_key());					

				// notes handling; runs after sample is created (need sample key)
				// at some point, convert pwi to use getHtNotes format
				if (domain.get(i).getNotes() != null && !domain.get(i).getNotes().isEmpty()) {
					NoteDomain newNoteDomain = new NoteDomain();
					newNoteDomain.setProcessStatus(Constants.PROCESS_CREATE);
					newNoteDomain.setNoteKey("");
					newNoteDomain.setObjectKey(String.valueOf(entity.get_sample_key()));
					newNoteDomain.setMgiTypeKey("43");
					newNoteDomain.setNoteTypeKey("1048");	
					newNoteDomain.setNoteChunk(domain.get(i).getNotes().get(0).getText());
					domain.get(i).setHtNotes(newNoteDomain);
					noteService.process(String.valueOf(entity.get_sample_key()), domain.get(i).getHtNotes(), "43", user);			
				}
			}

			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processHTSample delete");
				HTSample entity = htSampleDAO.get(domain.get(i).get_sample_key());
				htSampleDAO.remove(entity);
				log.info("processHTSample delete successful");
			}

			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processHTSample update");

				HTSample entity = htSampleDAO.get(domain.get(i).get_sample_key());

				entity.set_experiment_key(parentKey);
				entity.setOrganism(organismDAO.get(domain.get(i).get_organism_key()));
				entity.setRelevance(termDAO.get(domain.get(i).get_relevance_key()));
				entity.setRnaseqtype(termDAO.get(domain.get(i).get_rnaseqtype_key()));			
				entity.setSex(termDAO.get(domain.get(i).get_sex_key()));
				entity.setGenotype(genotypeDAO.get(domain.get(i).getGenotype_object().get_genotype_key()));
				entity.setName(domain.get(i).getName());

				if (domain.get(i).get_celltype_term_key() == null) {
					entity.setCellTypeTerm(null);
				}
				else {
					entity.setCellTypeTerm(termDAO.get(domain.get(i).get_celltype_term_key()));					
				}
				
				if (domain.get(i).getAge() == null || domain.get(i).getAge().isEmpty()) {
					entity.setAge(" ");
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);				
				} else {
					entity.setAge(domain.get(i).getAge());
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);	
				}
				// use HTEmapsDomain
				if (domain.get(i).getEmaps_object() == null) {
					entity.setTheilerStage(null);
				} else {
					entity.setTheilerStage(theilerStageDAO.get(domain.get(i).getEmaps_object().get_stage_key()));
				}
				
				if (domain.get(i).getEmaps_object() == null) {
					entity.setEmapaTerm(null);
				} else {
					entity.setEmapaTerm(termDAO.get(domain.get(i).getEmaps_object().get_emapa_term_key()));
				}
			
				// copy getNotes().get(0).getText() -> getHtNotes to use noteService correctly
				// at some point, convert pwi to use getHtNotes format
				if (domain.get(i).getNotes() != null && !domain.get(i).getNotes().isEmpty()) {
					if (domain.get(i).getNotes().get(0).getText() == null || domain.get(i).getNotes().get(0).getText().isEmpty()) {
						domain.get(i).getHtNotes().setProcessStatus(Constants.PROCESS_DELETE);
						domain.get(i).getHtNotes().setNoteChunk(null);				
					}
					else if (domain.get(i).getHtNotes() != null) {
						domain.get(i).getHtNotes().setProcessStatus(Constants.PROCESS_UPDATE);
						domain.get(i).getHtNotes().setNoteChunk(domain.get(i).getNotes().get(0).getText());
					}
					else {
						NoteDomain newNoteDomain = new NoteDomain();
						newNoteDomain.setProcessStatus(Constants.PROCESS_CREATE);
						newNoteDomain.setNoteKey("");
						newNoteDomain.setObjectKey(String.valueOf(entity.get_sample_key()));
						newNoteDomain.setMgiTypeKey("43");
						newNoteDomain.setNoteTypeKey("1048");	
						newNoteDomain.setNoteChunk(domain.get(i).getNotes().get(0).getText());
						domain.get(i).setHtNotes(newNoteDomain);
					}
					noteService.process(String.valueOf(entity.get_sample_key()), domain.get(i).getHtNotes(), "43", user);			
				}

				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				htSampleDAO.update(entity);
				
				log.info("processHTSample/changes processed: " + domain.get(i).get_sample_key());	
			}
			else {
				log.info("processHTSample/no changes processed: " + domain.get(i).get_sample_key());
			}
		}
		
		modified = true;
		log.info("processHTSample/processing successful");
		return modified;
	}

	@Transactional	
	public List<MGISetMemberEmapaDomain> getEmapaHTSampleBySetUser(SlimHTDomain searchDomain) {
		// return 
		// all set members of emapa (_set_key = 1046) + user (searchDomain.getCreatedByKey())
		// union
		// all emapa for given gxd_htsample (searchDomain.getExperimentKey())

		List<MGISetMemberEmapaDomain> results = new ArrayList<MGISetMemberEmapaDomain>();		
		
		// Search mgi_setmembers where _set_key = 1046 (emapa). 
		// Pull in EMAPS stage and _term_key
		String cmd = 
			"\nselect distinct s.sequenceNum, t.term, e._stage_key, s._setmember_key as setMemberKey, s._set_key as setKey, s._object_key as objectKey, s._createdby_key as createdByKey, u.login, a.accid" +
			"\nfrom mgi_setmember s, mgi_setmember_emapa e, voc_term t, mgi_user u, acc_accession a" +
			"\nwhere  s._setmember_key = e._setmember_key" + 
			"\nand s._set_key = 1046" +
			"\nand s._object_key = t._term_key" +
			"\nand t._term_key = a._object_key" +
			"\nand a._logicaldb_key = 169" +
			"\nand a.preferred = 1" +
			"\nand s._createdby_key = u._user_key" +
			"\nand u.login = '" + searchDomain.getCreatedBy() + "'" +		
			"\norder by sequenceNum, term";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberEmapaDomain domain = new MGISetMemberEmapaDomain();
				domain.setProcessStatus("x");
				domain.setSetKey(rs.getString("setKey"));
				domain.setSetMemberKey(rs.getString("setMemberKey"));
				domain.setObjectKey(rs.getString("objectKey"));
				domain.setTerm(rs.getString("term"));
				domain.setStage(rs.getString("_stage_key"));
				domain.setDisplayIt(rs.getString("term"));				
				domain.setCreatedByKey(rs.getString("createdByKey"));
				domain.setCreatedBy(rs.getString("login"));
				domain.setPrimaryid(rs.getString("accid"));
				domain.setIsUsed(false);
				results.add(domain);				
				htSampleDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;

	}

	@Transactional	
	public List<MGISetMemberCellTypeDomain> getCellTypeHTSampleBySetUser(SlimHTDomain searchDomain) {
		// return 
		// all set members of cell type (_set_key = 1059) + user (searchDomain.getCreatedByKey())
		// union
		// all cell types for given gxd_htsample (searchDomain.getExperimentKey())

		List<MGISetMemberCellTypeDomain> results = new ArrayList<MGISetMemberCellTypeDomain>();		
		
		// search mgi_setmembers where _set_key = 1059 (cell type)
		String cmd = "\nselect distinct s.sequenceNum, t.term, s._setmember_key as setMemberKey, s._set_key as setKey, s._object_key as objectKey, s._createdby_key as createdByKey, u.login, a.accid" +
				"\nfrom mgi_setmember s, voc_term t, mgi_user u, acc_accession a" +
				"\nwhere s._set_key = 1059" +
				"\nand s._object_key = t._term_key" +
				"\nand t._term_key = a._object_key" +
				"\nand a._logicaldb_key = 173" +
				"\nand a.preferred = 1" +
				"\nand s._createdby_key = u._user_key" +
				"\nand u.login = '" + searchDomain.getCreatedBy() + "'" +		
				"\norder by sequenceNum, term";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberCellTypeDomain domain = new MGISetMemberCellTypeDomain();
				domain.setProcessStatus("x");
				domain.setSetKey(rs.getString("setKey"));
				domain.setSetMemberKey(rs.getString("setMemberKey"));
				domain.setObjectKey(rs.getString("objectKey"));
				domain.setTerm(rs.getString("term"));
				domain.setDisplayIt(rs.getString("term"));				
				domain.setCreatedByKey(rs.getString("createdByKey"));
				domain.setCreatedBy(rs.getString("login"));
				domain.setPrimaryid(rs.getString("accid"));
				domain.setIsUsed(false);
				results.add(domain);				
				htSampleDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
