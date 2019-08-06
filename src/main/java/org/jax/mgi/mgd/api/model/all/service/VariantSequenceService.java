package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.all.dao.VariantSequenceDAO;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.entities.VariantSequence;
import org.jax.mgi.mgd.api.model.all.translator.VariantSequenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VariantSequenceService extends BaseService<VariantSequenceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private VariantSequenceDAO variantSequenceDAO;
	@Inject
	private TermDAO termDAO;
	
	// translate an entity to a domain to return in the results
	private VariantSequenceTranslator translator = new VariantSequenceTranslator();
	
	// to process Sequence accession IDs
	@Inject
	AccessionService accessionService = new AccessionService();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VariantSequenceDomain> create(VariantSequenceDomain domain, User user) {
		SearchResults<VariantSequenceDomain> results = new SearchResults<VariantSequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<VariantSequenceDomain> update(VariantSequenceDomain object, User user) {
		SearchResults<VariantSequenceDomain> results = new SearchResults<VariantSequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<VariantSequenceDomain> delete(Integer key, User user) {
		SearchResults<VariantSequenceDomain> results = new SearchResults<VariantSequenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public VariantSequenceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		VariantSequenceDomain domain = new VariantSequenceDomain();
		if (variantSequenceDAO.get(key) != null) {
			domain = translator.translate(variantSequenceDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<VariantSequenceDomain> getResults(Integer key) {
        SearchResults<VariantSequenceDomain> results = new SearchResults<VariantSequenceDomain>();
        results.setItem(translator.translate(variantSequenceDAO.get(key)));
        return results;
    }

	public List<VariantSequenceDomain> search(VariantSequenceDomain searchDomain) {

		List<VariantSequenceDomain> results = new ArrayList<VariantSequenceDomain>();

		// building SQL command : select + from + where + orderBy
		String cmd = "";		
		String select = "select vs.*, t1.term, u1.login as createdBy, u2.login as modifiedBy";
		String from = "from all_variant_sequence vs, voc_term t1, mgi_user u1, mgi_user u2";
		String where = "where vs._sequence_type_key = t1._term_key"
					+ "\nand vs._createdby_key = u1._user_key"
					+ "\nand vs._modifiedby_key = u2._user_key";
		String orderBy = "order by t1.term";
		String limit = Constants.SEARCH_RETURN_LIMIT;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("vs", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getVariantSequenceKey() != null && !searchDomain.getVariantSequenceKey().isEmpty()) {
			where = where + "\nand vs._variantsequence_key = " + searchDomain.getVariantSequenceKey();
		}
		if (searchDomain.getVariantKey() != null && !searchDomain.getVariantKey().isEmpty()) {
			where = where + "\nand vs._variant_key = " + searchDomain.getVariantKey();
		}		
		if (searchDomain.getSequenceTypeKey() != null && !searchDomain.getSequenceTypeKey().isEmpty()) {
			where = where + "\nand vs._sequence_type_key = " + searchDomain.getSequenceTypeKey();
		}
		if (searchDomain.getStartCoordinate() != null && !searchDomain.getStartCoordinate().isEmpty()) {
			where = where + "\nand vs.startCoordinate = " + searchDomain.getStartCoordinate();
		}
		if (searchDomain.getEndCoordinate() != null && !searchDomain.getEndCoordinate().isEmpty()) {
			where = where + "\nand vs.endCoordinate = " + searchDomain.getEndCoordinate();
		}
		if (searchDomain.getReferenceSequence() != null && !searchDomain.getReferenceSequence().isEmpty()) {
			where = where + "\nand vs.referenceSequence ilike '" + searchDomain.getReferenceSequence() + "'";
		}
		if (searchDomain.getVariantSequence() != null && !searchDomain.getVariantSequence().isEmpty()) {
			where = where + "\nand vs.variantSequence ilike '" + searchDomain.getVariantSequence() + "'";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				VariantSequenceDomain domain = new VariantSequenceDomain();
				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setVariantSequenceKey(rs.getString("_variantsequence_key"));			
				domain.setVariantKey(rs.getString("_variant_key"));
				domain.setSequenceTypeKey(rs.getString("_sequence_type_key"));
				domain.setSequenceTypeTerm(rs.getString("term"));
			    domain.setStartCoordinate(rs.getString("startCoordinate"));
			    domain.setEndCoordinate(rs.getString("endCoordinate"));
				domain.setReferenceSequence(rs.getString("referenceSequence"));
				domain.setVariantSequence(rs.getString("variantSequence"));	
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdBy"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedBy"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	@Transactional
	public Boolean process(String parentKey, List<VariantSequenceDomain> domains, User user) {
		// process variant sequence (create, delete, update)
		Boolean modified = false;
		
		log.info("processVariantSequence");
		
		if (domains == null || domains.isEmpty()) {
			log.info("processVariantSequence/nothing to process");
			return modified;
		}
				
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domains.size(); i++) {
				
			if (domains.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processVariantSequence create");
				
				VariantSequence sequenceEntity = new VariantSequence();
				log.info("processVariantSequence getting next VariantSequenceDomain");
				VariantSequenceDomain domain = domains.get(i);
				//What is value of parentKey and domain.getVariantKey
				log.info("processVariantSequence create parentKey" + parentKey);
				log.info("processVariantSequence create domain.getVariantKey" + domain.getVariantKey());
				log.info("processVariantSequence setting variant key in the VariantSequenceDomain");
				sequenceEntity.set_variant_key(Integer.valueOf(parentKey));
				log.info("processVariantSequence setting sequence type");
				sequenceEntity.setSequenceType(termDAO.get(Integer.valueOf(domain.getSequenceTypeKey())));
				log.info("processVariantSequence setting start coordinate");
				if(domain.getStartCoordinate() != null) { 
					sequenceEntity.setStartCoordinate(Integer.valueOf(domain.getStartCoordinate()));
				}
				log.info("processVariantSequence setting end coordinate");
				if(domain.getEndCoordinate() != null) {
				    sequenceEntity.setEndCoordinate(Integer.valueOf(domain.getEndCoordinate()));
				}
				log.info("processVariantSequence setting reference sequence");
				if(domain.getReferenceSequence() != null) {
					sequenceEntity.setReferenceSequence(domain.getReferenceSequence());
				}
				log.info("processVariantSequence setting variant sequence");
				if (domain.getVariantSequence() != null) {
					sequenceEntity.setVariantSequence(domain.getVariantSequence());
				}
				log.info("processVariantSequence setting version");
				if(domain.getVersion() != null) {
					sequenceEntity.setVersion(domain.getVersion());
				}
				log.info("processVariantSequence setting createdBy");
				sequenceEntity.setCreatedBy(user);
				log.info("processVariantSequence setting creation date");
				sequenceEntity.setCreation_date(new Date());
				log.info("processVariantSequence setting modifiedBy");
				sequenceEntity.setModifiedBy(user);
				log.info("processVariantSequence mod date");
				sequenceEntity.setModification_date(new Date());
				log.info("processSequenceVariant persisting the sequence entity");
				variantSequenceDAO.persist(sequenceEntity);
			
				// create accession ids of source variant sequence
				// assuming there is only 1 accession id per Variant Sequence			
			    log.info("VariantSequenceService processing accIDs: ");
				if (domain.getAccessionIds() != null) {
					modified = accessionService.process(
							String.valueOf(sequenceEntity.get_variantsequence_key()), 
							domain.getAccessionIds(),
							"Allele Variant Sequence", user);				
				}
			}
			else if (domains.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				
				log.info("processVariantSequence delete");
				
				VariantSequence entity = variantSequenceDAO.get(Integer.valueOf(domains.get(i).getVariantSequenceKey()));
				variantSequenceDAO.remove(entity);
				
								
				log.info("processVariantSequence delete successful");
			}
			else if (domains.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processVariantSequence update");

				
				VariantSequence entity = variantSequenceDAO.get(Integer.valueOf(domains.get(i).getVariantSequenceKey()));
				
				log.info("StartCoordinate");
				
				// the case where we are updating by setting to null  (deleting startCoordinate)
				if (domains.get(i).getStartCoordinate() == null && entity.getStartCoordinate() != null) {
					entity.setStartCoordinate(null);
					modified = true;
				}
				// the case where we are updating by setting to a different value
				else if (domains.get(i).getStartCoordinate() != null && !String.valueOf(entity.getStartCoordinate()).equals(domains.get(i).getStartCoordinate())) {
						entity.setStartCoordinate(Integer.valueOf(domains.get(i).getStartCoordinate()));
						modified = true;
				}
				
				log.info("EndCoordinate");
				// the case where we are updating by setting to null  (deleting endCoordinate)
				if (domains.get(i).getEndCoordinate() == null && entity.getEndCoordinate() != null) {
					entity.setEndCoordinate(null);
					modified = true;
				}
				else if (domains.get(i).getEndCoordinate() != null && !String.valueOf(entity.getEndCoordinate()).equals(domains.get(i).getEndCoordinate())) {
					entity.setEndCoordinate(Integer.valueOf(domains.get(i).getEndCoordinate()));
					modified = true;
				}
				log.info("sequenceTypeKey");
				//sequenceEntity.setSequenceType(termDAO.get(Integer.valueOf(domain.getSequenceTypeKey())));
				if (String.valueOf(entity.getSequenceType().get_term_key()) != domains.get(i).getSequenceTypeKey())  {
					entity.setSequenceType((termDAO.get(Integer.valueOf(domains.get(i).getSequenceTypeKey()))));
					modified = true;
				}
				log.info("ReferenceSequence");
				if (domains.get(i).getReferenceSequence() != null) {
					if ((entity.getReferenceSequence() == null) || !entity.getReferenceSequence().equals(domains.get(i).getReferenceSequence())) {
						entity.setReferenceSequence(domains.get(i).getReferenceSequence());
						modified = true;
					}
				}
				log.info("VariantSequence");
				if (domains.get(i).getVariantSequence() != null) {
					if ((entity.getVariantSequence() == null) || !entity.getVariantSequence().equals(domains.get(i).getVariantSequence())) {
						entity.setVariantSequence(domains.get(i).getVariantSequence());
						modified = true;
					}
				}
				log.info("VariantSequence Accession");
				if (domains.get(i).getAccessionIds() != null) {
					modified = accessionService.process(
							String.valueOf(entity.get_variantsequence_key()), 
							domains.get(i).getAccessionIds(),
							"Allele Variant Sequence", user);				
				}
				log.info("Version");
				if (domains.get(i).getVersion() != null) {
					if (entity.getVersion()!= null || !entity.getVersion().equals(domains.get(i).getVersion())) {
						entity.setVersion(domains.get(i).getVersion());
						modified = true;
					}
				}
				
				log.info("reference: check if modified");
				if (modified == true) {
					log.info("processVariantSequence modified == true");
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					variantSequenceDAO.update(entity);
					log.info("processVariantSequence/changes processed: " + domains.get(i).getVariantKey());
				}
			
				else {
					log.info("processVariant/no changes processed: " + domains.get(i).getVariantKey());
				}
			}
			else {
				log.info("processVariantSequence/no changes processed: " + domains.get(i).getVariantKey());
			}
		}
		return modified;
	}
}
