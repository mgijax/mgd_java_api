package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeMPDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeMPTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGenotypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GenotypeMPService extends BaseService<GenotypeMPDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GenotypeDAO genotypeDAO;

	@Inject
	private AnnotationService annotationService;
	
	private GenotypeMPTranslator translator = new GenotypeMPTranslator();
	private SlimGenotypeTranslator slimtranslator = new SlimGenotypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "12";
	
	@Transactional
	public SearchResults<GenotypeMPDomain> create(GenotypeMPDomain domain, User user) {
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		Genotype entity = genotypeDAO.get(Integer.valueOf(domain.getGenotypeKey()));
		//results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		log.info("processGenotypeMP/create");
		if (domain.getMpAnnots() != null && !domain.getMpAnnots().isEmpty()) {
			annotationService.process(domain.getMpAnnots(), user);
		}
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<GenotypeMPDomain> update(GenotypeMPDomain domain, User user) {

		// update existing entity object from in-coming domain
		
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		Genotype entity = genotypeDAO.get(Integer.valueOf(domain.getGenotypeKey()));

		log.info("processGenotypeMP/update");

		// process mp annotations
		// sc - 9/18 when uncommented the following 4 lines, had to update GenotypeMPDomain to have a list of 
		// AnnotationDomains instead of GenotypeMPAnnotationDomains (see that class for further explanation
		// also updated GenotypeMPTranslator to have an AnnotationTranslator rather than GenotypeMPAnnotationTranslator
		if (domain.getMpAnnots() != null && !domain.getMpAnnots().isEmpty()) {
			annotationService.process(domain.getMpAnnots(), user);
		}
		
		// return entity translated to domain
		log.info("processGenotype/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processGenotype/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<GenotypeMPDomain> delete(Integer key, User user) {
		log.info("processGenotypeMP/delete");
		log.info("GenotypeMPService delete key: " + key + " user login: " + user.getLogin());
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		// get and Annotation Service and here ?????
		return results;
	}
	
	@Transactional
	public GenotypeMPDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GenotypeMPDomain domain = new GenotypeMPDomain();
		if (genotypeDAO.get(key) != null) {
			domain = translator.translate(genotypeDAO.get(key));
		}
		return domain;
	}

	@Transactional
	public SearchResults<GenotypeMPDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		results.setItem(translator.translate(genotypeDAO.get(key)));
		return results;
	}
	
	@Transactional	
	public SearchResults<GenotypeMPDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		String cmd = "select count(*) as objectCount from voc_annot where _annottype_key = 1002";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional	
	public List<SlimGenotypeDomain> search(GenotypeMPDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select v._object_key, v.description";
		String from = "from gxd_genotype_summary_view v";		
		String where = "where v._mgitype_key = " + mgiTypeKey;
		String orderBy = "order by description";			
		String limit = Constants.SEARCH_RETURN_LIMIT;
		
		String value;
		
		Boolean from_accession = false;
		Boolean from_annot = false;
		Boolean from_evidence = false;
		Boolean from_property = false;
		
		// if parameter exists, then add to where-clause
		
		if (searchDomain.getGenotypeKey() != null && !searchDomain.getGenotypeKey().isEmpty()) {
			where = where + "\nand v._object_key = " + searchDomain.getGenotypeKey();
		}
	
		// accession id
		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().get(0).getAccID().isEmpty()) {
			String mgiid = searchDomain.getMgiAccessionIds().get(0).getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand a.accID ilike '" + mgiid + "'";
			from_accession = true;
		}
		
		if (searchDomain.getMpAnnots() != null && !searchDomain.getMpAnnots().isEmpty()) {
	
			value = searchDomain.getMpAnnots().get(0).getTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand a._term_key = " + value;
				from_annot = true;
			}
			value = searchDomain.getMpAnnots().get(0).getQualifierKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand a._qualifier_key = " + value;
				from_annot = true;
			}
			
			if (searchDomain.getMpAnnots().get(0).getEvidence() != null) {
				List<EvidenceDomain> evidenceDomains = searchDomain.getMpAnnots().get(0).getEvidence();
				// we may have more than one evidence in the list - get values for the first one
				EvidenceDomain first = evidenceDomains.get(0);
				String cmResults[] = DateSQLQuery.queryByCreationModification("e", 
						first.getCreatedBy(), 
						first.getModifiedBy(), 
						first.getCreation_date(), 
						first.getModification_date());
				if (cmResults.length > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
					from_evidence = true;
				}
				
				value = first.getMpSexSpecificity().getValue();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p.value ilike '" + value + "'";
					from_evidence = true;
					from_property = true;
			
				}
				
				value = first.getEvidenceTermKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand e.value = " + value;
					from_evidence = true;			
				}

				value = searchDomain.getMpAnnots().get(0).getEvidence().get(0).getRefsKey();
				String jnumid = first.getJnumid();
				String shortCitation = first.getShort_citation();				
				if (value != null && !value.isEmpty()) {
					where = where + "\nand e._Refs_key = " + value;
					from_evidence = true;
				}
				else if (jnumid != null && !jnumid.isEmpty()) {
					jnumid = jnumid.toUpperCase();
					if (!jnumid.contains("J:")) {
						jnumid = "J:" + jnumid;
					}
					where = where + "\nand e.jnumid = '" + jnumid + "'";
					from_evidence = true;
				}
				if (shortCitation != null && !shortCitation.isEmpty()) {
					shortCitation = shortCitation.replace("'",  "''");
					where = where + "\nand e.short_citation ilike '" + shortCitation + "'";
					from_evidence = true;
				}				
			}
		}
		
		if (from_evidence == true) {
			from_annot = true;
		}
		
		if (from_accession == true) {
			from = from + ", gxd_genotype_acc_view a";
			where = where + "\nand v._object_key = a._object_key" 
					+ "\nand a._mgitype_key = " + mgiTypeKey;
		}
		if (from_annot == true) {
			from = from + ", voc_annot a";
			where = where + "\nand v._object_key = a._object_key" 
					+ "\nand v._logicaldb_key = 1"
					+ "\nand v.preferred = 1"
					+ "\nand a._annottype_key = 1002";
		}
		if (from_evidence == true) {
			from = from + ", voc_evidence e";
			where = where + "\nand a._annot_key = e._annot_key";
		}
		if (from_property == true) {
			from = from + ", voc_evidence_property p";
			where = where + "\nand e._annotevidence_key = p._annotevidence_key";
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
	 
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimGenotypeDomain domain = new SlimGenotypeDomain();
				domain = slimtranslator.translate(genotypeDAO.get(rs.getInt("_object_key")));				
				domain.setGenotypeDisplay(rs.getString("description"));
				genotypeDAO.clear();				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
