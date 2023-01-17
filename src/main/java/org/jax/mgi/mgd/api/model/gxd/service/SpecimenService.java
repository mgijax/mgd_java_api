package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.SpecimenDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummarySpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Specimen;
import org.jax.mgi.mgd.api.model.gxd.translator.SpecimenTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class SpecimenService extends BaseService<SpecimenDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private SpecimenDAO specimenDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private InSituResultService insituresultService;
//	@Inject
//	private TermService termService;
	
	private SpecimenTranslator translator = new SpecimenTranslator();				

	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<SpecimenDomain> create(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<SpecimenDomain> update(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<SpecimenDomain> delete(Integer key, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SpecimenDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		SpecimenDomain domain = new SpecimenDomain();
		if (specimenDAO.get(key) != null) {
			domain = translator.translate(specimenDAO.get(key));
		}
		specimenDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<SpecimenDomain> getResults(Integer key) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setItem(translator.translate(specimenDAO.get(key)));
		specimenDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<SpecimenDomain> domain, User user) {
		// process specimens (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processSpecimen/nothing to process");
			return modified;
		}
		
		// vocabulary keys		
		int embeddingNS = 107080589;
		int fixationNS = 107080597;
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if specimenLabel is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
			if (domain.get(i).getSpecimenLabel() == null || domain.get(i).getSpecimenLabel().isEmpty()) {
				continue;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				log.info("processSpecimen create");

				Specimen entity = new Specimen();

				entity.set_assay_key(parentKey);
				entity.setSequenceNum(domain.get(i).getSequenceNum());				
				entity.setSpecimenLabel(domain.get(i).getSpecimenLabel());
				
				//defaults
				
				// Not Specified = 106849865
				if (domain.get(i).getEmbeddingKey() == null || domain.get(i).getEmbeddingKey().isEmpty()) {
					entity.setEmbeddingMethod(termDAO.get(embeddingNS));
				}
				else {
					entity.setEmbeddingMethod(termDAO.get(Integer.valueOf(domain.get(i).getEmbeddingKey())));
				}

				// Not Specified = 106849873
				if (domain.get(i).getFixationKey() == null || domain.get(i).getFixationKey().isEmpty()) {
					entity.setFixationMethod(termDAO.get(fixationNS));
				}
				else {
					entity.setFixationMethod(termDAO.get(Integer.valueOf(domain.get(i).getFixationKey())));
				}
				
				if (domain.get(i).getGenotypeKey() == null || domain.get(i).getGenotypeKey().isEmpty()) {
					entity.setGenotype(genotypeDAO.get(-1));
				}
				else {
					entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
				}
				
				if (domain.get(i).getSex() == null || domain.get(i).getSex().isEmpty()) {
					entity.setSex("Not Specified");
				}
				else {
					entity.setSex(domain.get(i).getSex());
				}
				
				if (domain.get(i).getHybridization() == null || domain.get(i).getHybridization().isEmpty()) {
					entity.setHybridization("Not Specified");
				}
				else {
					entity.setHybridization(domain.get(i).getHybridization());
				}
				
				String newAge = null;
				if (!domain.get(i).getAgePrefix().isEmpty()) {
					newAge = domain.get(i).getAgePrefix();
				}
				if (domain.get(i).getAgeStage() != null && !domain.get(i).getAgeStage().isEmpty()) {
					newAge = newAge + " " + domain.get(i).getAgeStage();
				}
				entity.setAge(newAge);
				entity.setAgeMin(-1);
				entity.setAgeMax(-1);		

				if (domain.get(i).getAgeNote() != null && !domain.get(i).getAgeNote().isEmpty()) {
					entity.setAgeNote(domain.get(i).getAgeNote());
				}
				else {
					entity.setAgeNote(null);					
				}	
				
				if (domain.get(i).getSpecimenNote() != null && !domain.get(i).getSpecimenNote().isEmpty()) {
					entity.setSpecimenNote(domain.get(i).getSpecimenNote());
				}
				else {
					entity.setSpecimenNote(null);					
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				specimenDAO.persist(entity);
				
				// process gxd_insituresult
				log.info("processSpecimen/insitu results");																		
				if (domain.get(i).getSresults() != null && !domain.get(i).getSresults().isEmpty()) {
					if (insituresultService.process(entity.get_specimen_key(), domain.get(i).getSresults(), user)) {
						modified = true;
					}
				}
				
				log.info("processSpecimen/MGI_resetAgeMinMax");																						
				String cmd = "select count(*) from MGI_resetAgeMinMax ('GXD_Specimen'," + entity.get_specimen_key() + ")";
				log.info("cmd: " + cmd);
				Query query = specimenDAO.createNativeQuery(cmd);
				query.getResultList();
				
				modified = true;
				log.info("processSpecimen/create processed: " + entity.get_specimen_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processSpecimen delete");			
				Specimen entity = specimenDAO.get(Integer.valueOf(domain.get(i).getSpecimenKey()));
				specimenDAO.remove(entity);
				modified = true;
				log.info("processSpecimen delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processSpecimen update");

				Specimen entity = specimenDAO.get(Integer.valueOf(domain.get(i).getSpecimenKey()));
			
				entity.set_assay_key(parentKey);
				entity.setEmbeddingMethod(termDAO.get(Integer.valueOf(domain.get(i).getEmbeddingKey())));
				entity.setFixationMethod(termDAO.get(Integer.valueOf(domain.get(i).getFixationKey())));
				entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setSpecimenLabel(domain.get(i).getSpecimenLabel());
				entity.setSex(domain.get(i).getSex());
				entity.setHybridization(domain.get(i).getHybridization());
				
				String newAge = null;
				if (!domain.get(i).getAgePrefix().isEmpty()) {
					newAge = domain.get(i).getAgePrefix();
				}
				if (domain.get(i).getAgeStage() != null && !domain.get(i).getAgeStage().isEmpty()) {
					newAge = newAge + " " + domain.get(i).getAgeStage();
				}
				entity.setAge(newAge);
				entity.setAgeMin(-1);
				entity.setAgeMax(-1);				

				if (domain.get(i).getAgeNote() != null && !domain.get(i).getAgeNote().isEmpty()) {
					entity.setAgeNote(domain.get(i).getAgeNote());
				}
				else {
					entity.setAgeNote(null);					
				}
								
				if (domain.get(i).getSpecimenNote() != null && !domain.get(i).getSpecimenNote().isEmpty()) {
					entity.setSpecimenNote(domain.get(i).getSpecimenNote());
				}
				else {
					entity.setSpecimenNote(null);					
				}
				
				entity.setModification_date(new Date());

				// process gxd_insituresult
				if (domain.get(i).getSresults() != null && !domain.get(i).getSresults().isEmpty()) {
					if (insituresultService.process(Integer.valueOf(domain.get(i).getSpecimenKey()), domain.get(i).getSresults(), user)) {
						modified = true;
					}
				}
				
				specimenDAO.update(entity);
								
				String cmd = "select count(*) from MGI_resetAgeMinMax ('GXD_Specimen'," + entity.get_specimen_key() + ")";
				log.info("cmd: " + cmd);
				Query query = specimenDAO.createNativeQuery(cmd);
				query.getResultList();
				
				modified = true;
				log.info("processSpecimen/changes processed: " + domain.get(i).getSpecimenKey());	
			}
			else {
				log.info("processSpecimen/no changes processed: " + domain.get(i).getSpecimenKey());
			}
		}
		
		log.info("processSpecimen/processing successful");
		return modified;
	}

	@Transactional	
	public List<SummarySpecimenDomain> getSpecimenByRef(String jnumid) {
		// return list of specimen domains by reference jnum id

		List<SummarySpecimenDomain> results = new ArrayList<SummarySpecimenDomain>();
		
		String cmd = "\nselect distinct s._specimen_key, m.symbol, a.accid, s.specimenLabel," +
			"\ns.sex, s.age, s.hybridization," +
			"\nt1.term as embeddingMethodTerm," +
			"\nt2.term as fixationTerm," +
			"\nt3.assaytype," +
			"\ns.ageNote, s.specimenNote, ss.strain, n.note as alleleDetailNote" +
			"\nfrom bib_citation_cache aa, gxd_assay g, gxd_specimen s, mrk_marker m, acc_accession a," +
			"\nvoc_term t1, voc_term t2, gxd_assaytype t3," +
			"\ngxd_genotype gs left outer join mgi_note n on (gs._genotype_key = n._object_key and n._notetype_key = 1016), prb_strain ss" +
			"\nwhere aa.jnumid = '" + jnumid + "'" +
			"\nand aa._refs_key = g._refs_key" +
			"\nand m._marker_key = g._marker_key" +
			"\nand g._assay_key = s._assay_key" +
			"\nand g._assay_key = a._object_key" +
			"\nand a._mgitype_key = 8" +
			"\nand a._logicaldb_key = 1" +
			"\nand s._embedding_key = t1._term_key" +
			"\nand s._fixation_key = t2._term_key" +
			"\nand g._assaytype_key = t3._assaytype_key" +
			"\nand s._genotype_key = gs._genotype_key" +
			"\nand gs._strain_key = ss._strain_key" +
			"\norder by specimenLabel, symbol, accid";

		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummarySpecimenDomain domain = new SummarySpecimenDomain();
				domain.setSpecimenKey(rs.getString("_specimen_key"));
				domain.setAssayID(rs.getString("accid"));
				domain.setAssayType(rs.getString("assaytype"));
				domain.setEmbeddingMethod(rs.getString("embeddingMethodTerm"));
				domain.setFixationMethod(rs.getString("fixationTerm"));
				domain.setGenotypeBackground(rs.getString("strain"));
				domain.setGenotypeAllelePairs(rs.getString("alleleDetailNote"));
				domain.setSpecimenLabel(rs.getString("specimenLabel"));
				domain.setSex(rs.getString("sex"));
				domain.setAge(rs.getString("age"));
				domain.setAgeNote(rs.getString("ageNote"));
				domain.setHybridization(rs.getString("hybridization"));
				domain.setSpecimenNote(rs.getString("specimenNote"));
			results.add(domain);
				specimenDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return results;
	}		
}
