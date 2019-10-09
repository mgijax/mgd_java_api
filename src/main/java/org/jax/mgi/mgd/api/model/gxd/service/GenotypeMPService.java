package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
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
		
		log.info("GenotypeMPService.create");
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		
		return results;
	}
	
	@Transactional
	public SearchResults<GenotypeMPDomain> update(GenotypeMPDomain domain, User user) {
		
		log.info("GenotypeMPService.update");
		
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		Genotype entity = genotypeDAO.get(Integer.valueOf(domain.getGenotypeKey()));
		
		if (domain.getMpAnnots() != null && !domain.getMpAnnots().isEmpty()) {
			// we don't need to capture the Boolean return value (modified-true/false)
			// because we don't need to update the genotype modBy/modDate
			annotationService.process(domain.getMpAnnots(), user);		
		}
		
		log.info("get the results by translating the entity");
		// get the results by translating the entity
		results.setItem(translator.translate(entity));
		
		return results;
	}

	@Transactional
	public SearchResults<GenotypeMPDomain> delete(Integer key, User user) {
		
		log.info("GenotypeMPService.delete");
		SearchResults<GenotypeMPDomain> results = new SearchResults<GenotypeMPDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		
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
		// sc - 10/4/19 removed the 'order by description' as we can't do that using
		// select "distinct on" because the description is arbitrary
		String cmd = "";
		String select = "select distinct on (v._object_key) v._object_key, v.description";
		String from = "from gxd_genotype_summary_view v";		
		String where = "where v._mgitype_key = " + mgiTypeKey;			
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
			where = where + "\nand lower(a.accID) = '" + mgiid.toLowerCase() + "'";
			from_accession = true;
		}
		// mp annotations and their evidence and sex specificity property
		if (searchDomain.getMpAnnots() != null && !searchDomain.getMpAnnots().isEmpty()) {
	
			value = searchDomain.getMpAnnots().get(0).getTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand va._term_key = " + value;
				from_annot = true;
			}
			value = searchDomain.getMpAnnots().get(0).getQualifierKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand va._qualifier_key = " + value;
				from_annot = true;
			}
			if (searchDomain.getMpAnnots().get(0).getEvidence() != null && !searchDomain.getMpAnnots().get(0).getEvidence().isEmpty()) {
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
				
				if (first.getMpSexSpecificity() != null ) {
					//value = first.getMpSexSpecificity().get(0).getPropertyTermKey();
				
					if (value != null) {
						where = where + "\nand p._propertyterm_key = " + value;
						from_evidence = true;
						from_property = true;
					}
				}
				if (first.getMpSexSpecificity() != null ) {
					value = first.getMpSexSpecificity().get(0).getValue();
					if (value != null) {
						where = where + "\nand p.value ilike '" + value + "'";
						from_evidence = true;
						from_property = true;
					}
				}
				value = first.getEvidenceTermKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand e._evidenceterm_key = " + value;
					from_evidence = true;			
				}

				value = first.getRefsKey();
				String jnumid = first.getJnumid();		
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
			from = from + ", voc_annot va";
			where = where + "\nand v._object_key = va._object_key" 
					+ "\nand v._logicaldb_key = 1"
					+ "\nand v.preferred = 1"
					+ "\nand va._annottype_key = 1002";
		}
		if (from_evidence == true) {
			from = from + ", voc_evidence_view e";
			where = where + "\nand va._annot_key = e._annot_key";
		}
		if (from_property == true) {
			from = from + ", voc_evidence_property p";
			where = where + "\nand e._annotevidence_key = p._annotevidence_key";
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + limit;
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
			
			// now we order by description - see note above at first 'select = '
			results.sort(Comparator.comparing(SlimGenotypeDomain::getGenotypeDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
