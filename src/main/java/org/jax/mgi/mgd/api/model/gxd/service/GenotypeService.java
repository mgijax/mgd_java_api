package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GenotypeService extends BaseService<GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GenotypeDAO genotypeDAO;

	private GenotypeTranslator translator = new GenotypeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "12";
	
	@Transactional
	public SearchResults<GenotypeDomain> create(GenotypeDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// thumbnailEntity will only be created if necessary
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		Genotype entity = new Genotype();
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		genotypeDAO.persist(entity);

		// return entity translated to domain
		log.info("processGenotype/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<GenotypeDomain> update(GenotypeDomain domain, User user) {

		// update existing entity object from in-coming domain
		
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		Genotype entity = genotypeDAO.get(Integer.valueOf(domain.getGenotypeKey()));
		Boolean modified = false;
		//String mgiTypeName = "Genotype";
		
		log.info("processGenotype/update");

		//if (!entity.getFigureLabel().equals(domain.getFigureLabel())) {
		//	entity.setFigureLabel(domain.getFigureLabel());
		//	modified = true;
		//}
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			genotypeDAO.update(entity);
			log.info("processGenotype/changes processed: " + domain.getGenotypeKey());
		}
		else {
			log.info("processGenotype/no changes processed: " + domain.getGenotypeKey());
		}
				
		// return entity translated to domain
		log.info("processGenotype/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processGenotype/update/returned results succsssful");
		return results;
	}

	@Transactional
	public GenotypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GenotypeDomain domain = new GenotypeDomain();
		if (genotypeDAO.get(key) != null) {
			domain = translator.translate(genotypeDAO.get(key));
		}
		return domain;
	}

	@Transactional
	public SearchResults<GenotypeDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		results.setItem(translator.translate(genotypeDAO.get(key)));
		return results;
	}
	
	@Transactional
	public SearchResults<GenotypeDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		Genotype entity = genotypeDAO.get(key);
		results.setItem(translator.translate(genotypeDAO.get(key)));
		genotypeDAO.remove(entity);
		return results;
	}

	@Transactional	
	public String getObjectCount() {
		// return the object count from the database
		
		String results = "";
		String cmd = "select count(*) as objectCount from gxd_genotype";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results = rs.getString("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional	
	public List<SlimGenotypeDomain> search(GenotypeDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct g._genotype_key, ps.strain, a1.symbol" +
				", concat(ps.strain,',',a1.symbol,',',a2.symbol) as genotypeDisplay";
		String from = "from gxd_genotype g, prb_strain ps, gxd_allelepair ap" +
				" inner join all_allele a1 on (ap._allele_key_1 = a1._allele_key)" +
				" left outer join all_allele a2 on (ap._allele_key_2 = a2._allele_key)" +
				", mrk_marker m";
		String where = "where g._strain_key = ps._strain_key" +
				"\nand g._genotype_key = ap._genotype_key" +
				"\nand ap._marker_key = m._marker_key";
		String 	orderBy = "order by strain, symbol nulls first";			
		String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		
		//Boolean from_allelepair = false;
		Boolean from_alleleDetailNote = false;
		Boolean from_generalNote = false;
		Boolean from_privateCuratorialNote = false;		
		Boolean from_accession = false;
		
		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("g", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getGenotypeKey() != null && !searchDomain.getGenotypeKey().isEmpty()) {
			where = where + "\nand g._genotype_key = " + searchDomain.getGenotypeKey();
		}
		if (searchDomain.getStrain() != null && !searchDomain.getStrain().isEmpty()) {
			where = where + "\nand ps.strain ilike '" + searchDomain.getStrain() + "'";
		}
		if (searchDomain.getIsConditional() != null && !searchDomain.getIsConditional().isEmpty()) {
			where = where + "\nand g.isConditional = " + searchDomain.getIsConditional();
		}
		if (searchDomain.getExistsAsKey() != null && !searchDomain.getExistsAsKey().isEmpty()) {
			where = where + "\nand g._ExistsAs_key = " + searchDomain.getExistsAsKey();
		}
		
		// notes
		if (searchDomain.getAlleleDetailNote() != null && !searchDomain.getAlleleDetailNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getAlleleDetailNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note1._notetype_key = 1016 and note1.note ilike '" + value + "'" ;
			from_alleleDetailNote = true;
		}
		if (searchDomain.getGeneralNote() != null && !searchDomain.getGeneralNote().getNoteChunk().isEmpty() 
				&& searchDomain.getGeneralNote().getNoteChunk().contains("%")) {
			value = searchDomain.getGeneralNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note2._notetype_key = 1027 and note2.note ilike '" + value + "'" ;
			from_generalNote = true;
		}
		if (searchDomain.getPrivateCuratorialNote() != null && !searchDomain.getPrivateCuratorialNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getPrivateCuratorialNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note3._notetype_key = 1028 and note3.note ilike '" + value + "'" ;
			from_privateCuratorialNote = true;
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
				
		// use views to match the teleuse implementation
		
		if (from_alleleDetailNote == true) {
			from = from + ", mgi_note_genotype_view note1";
			where = where + "\nand g._genotype_key = note1._object_key";
		}
		if (from_generalNote == true) {
			from = from + ", mgi_note_genotype_view note2";
			where = where + "\nand g._genotype_key = note2._object_key";
		}	
		if (from_privateCuratorialNote == true) {
			from = from + ", mgi_note_genotype_view note3";
			where = where + "\nand g._genotype_key = note3._object_key";
		}		
		if (from_accession == true) {
			from = from + ", gxd_genotype_acc_view a";
			where = where + "\nand g._genotype_key = a._object_key" 
					+ "\nand a._mgitype_key = " + mgiTypeKey;
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimGenotypeDomain domain = new SlimGenotypeDomain();
				domain.setGenotypeKey(rs.getString("_genotype_key"));
				domain.setGenotypeDisplay(rs.getString("genotypeDisplay"));
				// domain.setMgiAccessionIds() intentionally not set
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
