package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntigenDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyPrepDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAntibodyTranslator;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class AntibodyService extends BaseService<AntibodyDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyDAO antibodyDAO;
	@Inject 
	private TermDAO termDAO;
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private AntigenDAO antigenDAO;
	@Inject
	private MGIReferenceAssocService referenceAssocService;
	@Inject
	private AntibodyAliasService aliasService;
	@Inject
	private AntibodyMarkerService antibodyMarkerService;
	@Inject 
	private AntibodyPrepService antibodyPrepService;
	
	private AntibodyTranslator translator = new AntibodyTranslator();
	private SlimAntibodyTranslator slimtranslator = new SlimAntibodyTranslator();

	String mgiTypeKey = "6";
	
	// vocabulary keys
	String antibodyClassNS = "107080501";
	String antibodyTypeNS = "107367160";
	
	@Transactional
	public SearchResults<AntibodyDomain> create(AntibodyDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		Antibody entity = new Antibody();
		String note = "";
		
		log.info("Antibody/create");
		
		// may not be null
		entity.setAntibodyName(domain.getAntibodyName());
		
		log.info("antibody note");
		// may be null
		if(domain.getAntibodyNote() != null && !domain.getAntibodyNote().isEmpty()) {
			note = DecodeString.setDecodeToLatin9(domain.getAntibodyNote());
			note = note.replace("''", "'");
			entity.setAntibodyNote(note);
		}
		else {
			entity.setAntibodyNote(null);
		}
		
		log.info("antibody class");
		// has default if not set
		if(domain.getAntibodyClassKey() ==  null || domain.getAntibodyClassKey().isEmpty()){
			// 'Not Specified'
			domain.setAntibodyClassKey(antibodyClassNS);			
		}
		entity.setAntibodyClass(termDAO.get(Integer.valueOf(domain.getAntibodyClassKey())));
		
		log.info("antibody type");
		// has default if not set
		if(domain.getAntibodyTypeKey() ==  null || domain.getAntibodyTypeKey().isEmpty()) {
			// 'Not Specified'
			domain.setAntibodyTypeKey(antibodyTypeNS);
		}
	    entity.setAntibodyType(termDAO.get(Integer.valueOf(domain.getAntibodyTypeKey())));
	    
	    // has default if not set
	    log.info("antibody organism");
		if(domain.getOrganismKey() == null || domain.getOrganismKey().isEmpty()) {
			// 'Not Specified'
			domain.setOrganismKey("76");
		}
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		
		log.info("antibody antigen key: " + domain.getAntigen().getAntigenKey());
		if (domain.getAntigen().getAntigenKey() != null && !domain.getAntigen().getAntigenKey().isEmpty()) {
			entity.setAntigen(antigenDAO.get(Integer.valueOf(domain.getAntigen().getAntigenKey())));
		}
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		antibodyDAO.persist(entity);

		// process antibody references, can be null
		log.info("Antibody/create references");
		if (domain.getRefAssocs() != null && ! domain.getRefAssocs().isEmpty()) {
			
			log.info("create references");
			log.info("antibody key: " + String.valueOf(entity.get_antibody_key()));
			log.info("refAssocDomain mgitypeKey: " + domain.getRefAssocs().get(0).getMgiTypeKey() + " refsKey: " + domain.getRefAssocs().get(0).getRefsKey());
			referenceAssocService.process(String.valueOf(entity.get_antibody_key()), domain.getRefAssocs(), mgiTypeKey, user);
		}
		
		// process antibody aliases, can be null
		log.info("Antibody/create aliases");
		if (domain.getAliases() != null && !domain.getAliases().isEmpty()) {
			log.info("create aliases");
			log.info("antibody key: " + String.valueOf(entity.get_antibody_key()));
			log.info("aliasDomain alias:" + domain.getAliases().get(0).getAlias());
			aliasService.process(String.valueOf(entity.get_antibody_key()), domain.getAliases(), user);
		}
		
		// process antibody markers, can be null
		log.info("Antibody/create antibodymarkers");
		if (domain.getMarkers() != null && !domain.getMarkers().isEmpty()) {
			antibodyMarkerService.process(String.valueOf(entity.get_antibody_key()), domain.getMarkers(), user);
			log.info("create markers");
			log.info("antibody key: " + String.valueOf(entity.get_antibody_key()));
			log.info("antibodyMarkerDomain markerKey: " + domain.getMarkers().get(0).getMarkerKey());
		}
				
		// return entity translated to domain
		log.info("processAntibody/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<AntibodyDomain> update(AntibodyDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		Antibody entity = antibodyDAO.get(Integer.valueOf(domain.getAntibodyKey()));
		String note = "";
		
		log.info("Antibody/update");

		entity.setAntibodyName(domain.getAntibodyName());
		
		log.info("antibody note: " + domain.getAntibodyNote());
		if(domain.getAntibodyNote() != null && !domain.getAntibodyNote().isEmpty()) {
			note = DecodeString.setDecodeToLatin9(domain.getAntibodyNote());
			note = note.replace("''", "'");
			entity.setAntibodyNote(note);					
		}
		else {
			entity.setAntibodyNote(null);	
		}
	    
		log.info("antibody class: " + domain.getAntibodyClassKey());
		// has default if not set
		if(domain.getAntibodyClassKey() ==  null || domain.getAntibodyClassKey().isEmpty()){
			// 'Not Specified'
			domain.setAntibodyClassKey(antibodyClassNS);		
		}
		entity.setAntibodyClass(termDAO.get(Integer.valueOf(domain.getAntibodyClassKey())));
		
		log.info("antibody type");
		// has default if not set
		if(domain.getAntibodyTypeKey() ==  null || domain.getAntibodyTypeKey().isEmpty()) {
			// 'Not Specified'
			domain.setAntibodyTypeKey(antibodyTypeNS);
		}
	    entity.setAntibodyType(termDAO.get(Integer.valueOf(domain.getAntibodyTypeKey()))); 
	    
	    // has default if not set
	    log.info("antibody organism");
		if(domain.getOrganismKey() == null ||  domain.getOrganismKey().isEmpty()) {
			// 'Not Specified'
			domain.setOrganismKey("76");
		}
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		
		log.info("antibody antigen key: " + domain.getAntigen().getAntigenKey());
		if (domain.getAntigen().getAntigenKey() != null && !domain.getAntigen().getAntigenKey().isEmpty()) {
			entity.setAntigen(antigenDAO.get(Integer.valueOf(domain.getAntigen().getAntigenKey())));
		}
			
		// process antibody aliases, can be null
		if (domain.getAliases() != null && !domain.getAliases().isEmpty()) {
			log.info("Antibody/update aliases");
			log.info("antibody key: " + String.valueOf(entity.get_antibody_key()));
			log.info("aliasDomain alias: " + domain.getAliases().get(0).getAlias());
			log.info("alisDomain creation_date:" + domain.getAliases().get(0).getCreation_date());
			aliasService.process(String.valueOf(entity.get_antibody_key()), domain.getAliases(), user);
		}
		
		// process antibody markers, can be null
		if (domain.getMarkers() != null && !domain.getMarkers().isEmpty()) {
			log.info("Antibody/update markers");
			log.info("antibody key: " + String.valueOf(entity.get_antibody_key()));
			log.info("antibodyMarkerDomain markerKey: " + domain.getMarkers().get(0).getMarkerKey());
			antibodyMarkerService.process(String.valueOf(entity.get_antibody_key()), domain.getMarkers(), user);
		}
		
		// process antibody references, can be null
		if (domain.getRefAssocs() != null && ! domain.getRefAssocs().isEmpty()) {
			log.info("Antibody/update references");
			log.info("antibody key: " + String.valueOf(entity.get_antibody_key()));
			log.info("refAssocDomain mgitypeKey: " + domain.getRefAssocs().get(0).getMgiTypeKey() + " refsKey: " + domain.getRefAssocs().get(0).getRefsKey());
			referenceAssocService.process(String.valueOf(entity.get_antibody_key()), domain.getRefAssocs(), mgiTypeKey, user);
		}
		
		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		antibodyDAO.update(entity);
		log.info("processAntibody/changes processed: " + domain.getAntibodyKey());
			
		// return entity translated to domain
		log.info("processAntibody/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAntibody/update/returned results succsssful");
		return results;
	}

	@Transactional
	public SearchResults<AntibodyDomain> delete(Integer key, User user) {
		log.info("Antibody/delete");
		
		// remove all AntibodyPrep referencing this antibody
		log.info("getting antibodyPrep objects for antibody");
		AntibodyPrepDomain inPrepDomain = new AntibodyPrepDomain();
		inPrepDomain.setAntibodyKey(String.valueOf(key));
		
		List<AntibodyPrepDomain> prepDomainList =  antibodyPrepService.search(inPrepDomain);
		for (int i = 0; i < prepDomainList.size(); i++) {
			String prepKey = prepDomainList.get(i).getAntibodyPrepKey();
			log.info(" deleting antibody prep key: " + prepKey);
			antibodyPrepService.delete(Integer.valueOf(prepKey), user);
			log.info("done deleting antibody prep key: " + prepKey);
		}
		
		// now delete the antibody.
		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		Antibody entity = antibodyDAO.get(key);
		
		results.setItem(translator.translate(antibodyDAO.get(key)));
		antibodyDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public AntibodyDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyDomain domain = new AntibodyDomain();
		if (antibodyDAO.get(key) != null) {
			domain = translator.translate(antibodyDAO.get(key));
	        
	        // determine hasExpression
	    	String cmd = "select case when exists (select 1 from gxd_antibodyprep p, gxd_assay e" +
	    				"\nwhere p._antibody_key = " + key +
	    				"\nand p._antibodyprep_key = e._antibodyprep_key)" +
	    				"\nthen 1 else 0 end as hasExpression";
	    	log.info(cmd);	
	    	try {
	    		ResultSet rs = sqlExecutor.executeProto(cmd);
	    		while (rs.next()) {
	    			domain.setHasExpression(rs.getString("hasExpression"));
	    		}
	    		sqlExecutor.cleanup();
	    	}
	    	catch (Exception e) {
	    		e.printStackTrace();
	    	}				
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyDomain> getResults(Integer key) {
        SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
        results.setItem(translator.translate(antibodyDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		String cmd = "select count(*) as objectCount from gxd_antibody";
		
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
	public List<SlimAntibodyDomain> search(AntibodyDomain searchDomain) {

		List<SlimAntibodyDomain> results = new ArrayList<SlimAntibodyDomain>();
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_antibody a";
		String where = "where a._antibody_key is not null";
		String orderBy = "order by antibodyName";
		String value;
		Boolean from_accession = false;
		Boolean from_antigenaccession = false;
		Boolean from_reference = false;
		Boolean from_alias = false;
		Boolean from_aliasref = false;
		Boolean from_antigen = false;
		Boolean from_marker = false;
		Boolean do_union = false;
		
		String union = "union \nselect a.*" + 
				"\nfrom gxd_antibody a, gxd_antibodyalias aa" + 
				"\nwhere a._antibody_key = aa._antibody_key";

		//log.info("Antibody name: " + searchDomain.getAntibodyName());
		if(searchDomain.getAntibodyName() != null && ! searchDomain.getAntibodyName().isEmpty()) {
			value = searchDomain.getAntibodyName().replaceAll("'", "''");
			where = where + "\n and a.antibodyName ilike '" + value +  "'";
			do_union = true;
		}	
		//log.info("Antibody typeKey: " + searchDomain.getAntibodyTypeKey());
		if(searchDomain.getAntibodyTypeKey() != null && ! searchDomain.getAntibodyTypeKey().isEmpty()) {
			where = where + "\n and a._antibodyType_key = " + searchDomain.getAntibodyTypeKey();
		}		
		//log.info("Antibody classKey: " + searchDomain.getAntibodyClassKey());
		if(searchDomain.getAntibodyClassKey() != null && ! searchDomain.getAntibodyClassKey().isEmpty()) {
			where = where + "\n and a._antibodyClass_key = " + searchDomain.getAntibodyClassKey();
		}		
		//log.info("Antibody organismKey: " + searchDomain.getOrganismKey());
		if(searchDomain.getOrganismKey() != null && ! searchDomain.getOrganismKey().isEmpty()) {
			where = where + "\n and a._organism_key = " + searchDomain.getOrganismKey();
		}		
		//log.info("antibody note: " + searchDomain.getAntibodyNote());
		if(searchDomain.getAntibodyNote() != null && ! searchDomain.getAntibodyNote().isEmpty()) {
			value = searchDomain.getAntibodyNote().replace("''", "'");
			where = where + "\n and a.antibodyNote ilike '" + value + "'";
		}
		
		if (searchDomain.getAntigen() != null)  {
			if( searchDomain.getAntigen().getAccID() != null && ! searchDomain.getAntigen().getAccID().isEmpty()) {
				//log.info("antigen ID is specified");
				if (! searchDomain.getAntigen().getAccID().startsWith("MGI:")) {
					where = where + "\nand acc2v.numericPart = '" + searchDomain.getAntigen().getAccID() + "'";
				}
				else {
					where = where + "\nand acc2v.accID ilike '" + searchDomain.getAntigen().getAccID() + "'";
				}
				from_antigenaccession = true;
			}
			else { // no antigen key check for antigen and antigen source attributes
				//log.info("antigen is not specified, check antigen attributes");
				if (searchDomain.getAntigen().getAntigenName() != null && ! searchDomain.getAntigen().getAntigenName().isEmpty()) {					
					//log.info("antigen name: " + searchDomain.getAntigen().getAntigenName());
					value = searchDomain.getAntigen().getAntigenName().replaceAll("'", "''");
					where = where + "\n and av.antigenname ilike '" + value + "'";
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getRegionCovered() != null && ! searchDomain.getAntigen().getRegionCovered().isEmpty()) {
					//log.info("antigen regioncovered: " + searchDomain.getAntigen().getRegionCovered());
					where = where + "\n and av.regioncovered ilike '" + searchDomain.getAntigen().getRegionCovered()+ "'";
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getAntigenNote() != null && ! searchDomain.getAntigen().getAntigenNote().isEmpty()) {
					value = searchDomain.getAntigen().getAntigenNote().replace("''", "'");					
					where = where + "\n and av.antigennote ilike '" + value + "'";
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getProbeSource().getOrganismKey() != null && ! searchDomain.getAntigen().getProbeSource().getOrganismKey().isEmpty()) {
					where = where + "\n and sv._organism_key = " + searchDomain.getAntigen().getProbeSource().getOrganismKey();
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getProbeSource().getStrainKey() != null && ! searchDomain.getAntigen().getProbeSource().getStrainKey().isEmpty()) {
					where = where + "\n and sv._strain_key = " + searchDomain.getAntigen().getProbeSource().getStrainKey();
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getProbeSource().getTissueKey() != null && ! searchDomain.getAntigen().getProbeSource().getTissueKey().isEmpty()) {
					where = where + "\n and sv._tissue_key = " + searchDomain.getAntigen().getProbeSource().getTissueKey();
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getProbeSource().getDescription() != null && ! searchDomain.getAntigen().getProbeSource().getDescription().isEmpty()) {
					where = where + "\n and sv.description = '" + searchDomain.getAntigen().getProbeSource().getDescription() + "'";
					from_antigen = true;
				}
				// cell line key is actually a VOC_Term._term_key
				//log.info("antigen celline key : " + searchDomain.getAntigen().getProbeSource().getCellLineKey());
				if (searchDomain.getAntigen().getProbeSource().getCellLineKey() != null && ! searchDomain.getAntigen().getProbeSource().getCellLineKey().isEmpty()) {
					where = where + "\n and sv._cellline_key = " + searchDomain.getAntigen().getProbeSource().getCellLineKey();
					from_antigen = true;
				}
				if (searchDomain.getAntigen().getProbeSource().getGenderKey() != null && ! searchDomain.getAntigen().getProbeSource().getGenderKey().isEmpty()) {
					where = where + "\n and sv._gender_key = " + searchDomain.getAntigen().getProbeSource().getGenderKey();
					from_antigen = true;
				}
				String ageSearch = "";
				if (searchDomain.getAntigen().getProbeSource().getAgePrefix() != null && ! searchDomain.getAntigen().getProbeSource().getAgePrefix().isEmpty() ) {
					ageSearch = searchDomain.getAntigen().getProbeSource().getAgePrefix();
				}
				if (searchDomain.getAntigen().getProbeSource().getAgeStage() != null && ! searchDomain.getAntigen().getProbeSource().getAgeStage().isEmpty() ) {
					ageSearch = ageSearch + "%" + searchDomain.getAntigen().getProbeSource().getAgeStage();
				}			
				if (ageSearch.length() > 0) {
					where = where + "\nand sv.age ilike '%" + ageSearch + "%'";
					from_antigen = true;	
				}		
			}
		}	
		
		// create/mode by/date
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			if (! searchDomain.getAccID().startsWith("MGI:")) {
				where = where + "\nand acc.numericPart = '" + searchDomain.getAccID() + "'";
			}
			else {
				where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			}
			from_accession = true;
		}						
	
		// reference associations
		if (searchDomain.getRefAssocs() != null && !searchDomain.getRefAssocs().isEmpty()) {
			//log.info("searchDomain.getRefAssocs() is not null");
            if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
            		//log.info("adding refsKey clause");
                    where = where + "\nand ref._Refs_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
                    from_reference = true;
            }
            if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
                    //log.info("adding short citation clause");
            		value = searchDomain.getRefAssocs().get(0).getShort_citation().replace("'",  "''");
                    where = where + "\nand ref.short_citation ilike '" + value + "'";
                    from_reference = true;
            }
            if (searchDomain.getRefAssocs().get(0).getJnumid() != null && !searchDomain.getRefAssocs().get(0).getJnumid().isEmpty()) {
            		//log.info("Adding jnum id/assoc type clause");
            		String jnumid = searchDomain.getRefAssocs().get(0).getJnumid().toUpperCase();
                    if (!jnumid.contains("J:")) {
                    	jnumid = "J:" + jnumid;
                    }
            	
                    where = where + "\nand ref.jnumid ilike '" + jnumid + "'";
                    from_reference = true;
            }
		}

		// aliases
		if (searchDomain.getAliases() != null && ! searchDomain.getAliases().isEmpty()) {
			//log.info("searchDomain.getAliases() is not null");
			//log.info("searchDomain.getAliases().get(0).getAlias(): " + searchDomain.getAliases().get(0).getAlias());
			if (searchDomain.getAliases().get(0).getAlias()!= null && !searchDomain.getAliases().get(0).getAlias().isEmpty()) {
        		log.info("adding aliasKey clause");
                where = where + "\nand al.alias ilike '" + searchDomain.getAliases().get(0).getAlias() + "'";
                from_alias = true;
			}
			if (searchDomain.getAliases().get(0).getRefsKey() != null && ! searchDomain.getAliases().get(0).getRefsKey().isEmpty()) {
				//log.info("adding alias refsKey clause");
				where = where+ "\nand  al._Refs_key = " + searchDomain.getAliases().get(0).getRefsKey();
				from_alias = true;
			}
			if (searchDomain.getAliases().get(0).getShort_citation() != null && !searchDomain.getAliases().get(0).getShort_citation().isEmpty()) {
                //log.info("adding alias short citation clause");
        		value = searchDomain.getAliases().get(0).getShort_citation().replace("'",  "''");
                where = where + "\nand aref.short_citation ilike '" + value + "'";
                from_aliasref = true;
			}
			if (searchDomain.getAliases().get(0).getJnumid() != null && !searchDomain.getAliases().get(0).getJnumid().isEmpty()) {
        		//log.info("Adding alias jnum id/assoc type clause");
                where = where + "\nand aref.jnumid = '" + searchDomain.getAliases().get(0).getJnumid() + "'";
	            from_aliasref = true;
	        }
		}
		
		// markers
		
		if (searchDomain.getMarkers() != null && ! searchDomain.getMarkers().isEmpty()) {
			//log.info("getMarkers is not null");
			//log.info(searchDomain.getMarkers().get(0).getMarkerKey());
			if (searchDomain.getMarkers().get(0).getMarkerSymbol() != null & ! searchDomain.getMarkers().get(0).getMarkerSymbol().isEmpty()) {
				//log.info("Adding marker symbol to clause");
				where = where + "\nand mv.symbol ilike '" + searchDomain.getMarkers().get(0).getMarkerSymbol() + "'";
				from_marker = true;
			}
			if (searchDomain.getMarkers().get(0).getMarkerKey() != null & ! searchDomain.getMarkers().get(0).getMarkerKey().isEmpty()) {
				//log.info("Adding marker to clause");
				where = where + "\nand mv._marker_key = " + searchDomain.getMarkers().get(0).getMarkerKey();
				from_marker = true;
			}
			if (searchDomain.getMarkers().get(0).getChromosome() != null & ! searchDomain.getMarkers().get(0).getChromosome().isEmpty()) {
				//log.info("Adding chromosome to clause");
				where = where + "\nand mv.chromosome = '" + searchDomain.getMarkers().get(0).getChromosome() + "'";
				from_marker = true;
			}
		}
		
		if (from_accession == true) {
			from = from + ", gxd_antibody_acc_view acc";
			where = where + "\nand a._antibody_key = acc._object_key"; 
		}
		if (from_antigenaccession == true) {
			from = from + ", gxd_antibodyantigen_view acc2, gxd_antigen_acc_view acc2v";
			where = where + "\nand a._antibody_key = acc2._antibody_key";
			where = where + "\nand acc2._antigen_key = acc2v._object_key";
		}		
		if (from_reference == true) {
            from = from + ", mgi_reference_antibody_view ref";
            where = where + "\nand a._antibody_key = ref._object_key";
		}
		if (from_alias == true) {
			from = from + ", gxd_antibodyalias al";
			where = where + "\nand a._antibody_key = al._antibody_key";
		}
		if (from_aliasref == true) {
			from = from + ", gxd_antibodyaliasref_view aref";
			where = where + "\nand a._antibody_key = aref._antibody_key";
		}
		if (from_antigen == true) {
			from = from + ", gxd_antigen_view av, prb_source sv";
			where = where + "\nand a._antigen_key = av._antigen_key";
			where = where + "\nand av._source_key = sv._source_key";
		}
		if (from_marker == true) {
			from = from + ", gxd_antibodymarker_view mv";
			where = where + "\nand a._antibody_key = mv._antibody_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		if (do_union == true) {
			value = searchDomain.getAntibodyName().replaceAll("'", "''");
			String union_text = union + "\nand aa.alias ilike '" + value +  "'";
			cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + union_text + "\n" + orderBy;
		}
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAntibodyDomain domain = new SlimAntibodyDomain();
				domain = slimtranslator.translate(antibodyDAO.get(rs.getInt("_antibody_key")));				
				antibodyDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public List<SlimAntibodyDomain> validateAntibody(SlimAntibodyDomain searchDomain) {
		
		List<SlimAntibodyDomain> results = new ArrayList<SlimAntibodyDomain>();
		
		String value = searchDomain.getAccID().toUpperCase();
		if (!value.contains("MGI:")) {
			value = "MGI:" + value;
		}
		
		String cmd = "select accID, _object_key, description from GXD_Antibody_Acc_View"
					+ "\nwhere accID = '" + value + "'";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimAntibodyDomain slimdomain = new SlimAntibodyDomain();
				slimdomain.setAccID(rs.getString("accID"));
				slimdomain.setAntibodyKey(rs.getString("_object_key"));
				slimdomain.setAntibodyName(rs.getString("description"));
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional	
	public List<SummaryAntibodyDomain> getAntibodyByMarker(String accid) {
		// return list of antibody domains by marker acc id

		List<SummaryAntibodyDomain> results = new ArrayList<SummaryAntibodyDomain>();
		
		String cmd = "\nselect distinct a._antibody_key, a.antibodyName, a1.accid as antibodyid,  a2.accid as markerid" +
				"\nfrom GXD_Antibody a, GXD_AntibodyMarker am, ACC_Accession a1, ACC_Accession a2" +		
				"\nwhere a._antibody_key = am._antibody_key" +
				"\nand a._antibody_key = a1._object_key" +
				"\nand a1._mgitype_key = 6" +
				"\nand a1._logicaldb_key = 1" +
				"\nand am._marker_key = a2._object_key" +
				"\nand a2._mgitype_key = 2" +
				"\nand a2._logicaldb_key = 1" +
				"\nand a2.preferred = 1" +
				"\nand a2.accID = '" + accid + "'" +
				"\norder by antibodyName, antibodyid";

		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryAntibodyDomain domain = new SummaryAntibodyDomain();
				AntibodyDomain adomain = new AntibodyDomain();
				adomain = translator.translate(antibodyDAO.get(rs.getInt("_antibody_key")));	
				antibodyDAO.clear();
				domain.setAntibodyKey(adomain.getAntibodyKey());
				domain.setAntibodyID(adomain.getAccID());
				domain.setAntibodyName(adomain.getAntibodyName());
				domain.setAntibodyClass(adomain.getAntibodyClass());
				domain.setAntibodyType(adomain.getAntibodyType());
				domain.setAntibodyOrganism(adomain.getOrganism());
				domain.setAntibodyNote(adomain.getAntibodyNote());
				domain.setAntigenID(adomain.getAntigen().getAccID());
				domain.setAntigenName(adomain.getAntigen().getAntigenName());			
				domain.setAntigenOrganism(adomain.getAntigen().getProbeSource().getOrganism());
				domain.setAntigenNote(adomain.getAntigen().getAntigenNote());
				domain.setRegionCovered(adomain.getAntigen().getRegionCovered());
	 
				List<String> markerIDs = new ArrayList<String>();
				List<String> markerSymbols = new ArrayList<String>();
				for (int i = 0; i < adomain.getMarkers().size(); i++) {
					markerIDs.add(adomain.getMarkers().get(i).getMarkerMGIID());
					markerSymbols.add(adomain.getMarkers().get(i).getMarkerSymbol());
				}
				domain.setMarkerSymbol(String.join(",", markerSymbols));				

				for (int i = 0; i < adomain.getRefAssocs().size(); i++) {
					if (adomain.getRefAssocs().get(i).getAllowOnlyOne() == 1) {
						domain.setJnumID(adomain.getRefAssocs().get(i).getJnumid());
						domain.setShortCitation(adomain.getRefAssocs().get(i).getShort_citation());
					}
				}
				
				List<String> aliases = new ArrayList<String>();
				if (adomain.getAliases() != null) {
					for (int i = 0; i < adomain.getAliases().size(); i++) {
						aliases.add(adomain.getAliases().get(i).getAlias());
					}
				}
				domain.setAliases(String.join(",", aliases));				
				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return results;
	}

	@Transactional	
	public List<SummaryAntibodyDomain> getAntibodyByRef(String jnumid) {
		// return list of probe domains by reference jnumid

		List<SummaryAntibodyDomain> results = new ArrayList<SummaryAntibodyDomain>();
		
		String cmd = "\nselect distinct a._antibody_key, a.antibodyName, a1.accid as antibodyid" +
				"\nfrom GXD_Antibody a, ACC_Accession a1, MGI_Reference_Assoc r, BIB_Citation_Cache rc" +		
				"\nwhere a._antibody_key = a1._object_key" +
				"\nand a1._mgitype_key = 6" +
				"\nand a1._logicaldb_key = 1" +				
				"\nand a._antibody_key = r._object_key" +				
				"\nand r._mgitype_key = 6" +
				"\nand r._refs_key = rc._refs_key" +
				"\nand rc.jnumid = '" + jnumid + "'" +
				"\norder by antibodyName, antibodyid";

		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryAntibodyDomain domain = new SummaryAntibodyDomain();
				AntibodyDomain adomain = new AntibodyDomain();
				adomain = translator.translate(antibodyDAO.get(rs.getInt("_antibody_key")));	
				antibodyDAO.clear();
				domain.setAntibodyKey(adomain.getAntibodyKey());
				domain.setAntibodyID(adomain.getAccID());
				domain.setAntibodyName(adomain.getAntibodyName());
				domain.setAntibodyClass(adomain.getAntibodyClass());
				domain.setAntibodyType(adomain.getAntibodyType());
				domain.setAntibodyOrganism(adomain.getOrganism());
				domain.setAntibodyNote(adomain.getAntibodyNote());
				domain.setAntigenID(adomain.getAntigen().getAccID());
				domain.setAntigenName(adomain.getAntigen().getAntigenName());			
				domain.setAntigenOrganism(adomain.getAntigen().getProbeSource().getOrganism());
				domain.setAntigenNote(adomain.getAntigen().getAntigenNote());
				domain.setRegionCovered(adomain.getAntigen().getRegionCovered());
	 
				List<String> markerIDs = new ArrayList<String>();
				List<String> markerSymbols = new ArrayList<String>();
				if (adomain.getMarkers() != null) {
					for (int i = 0; i < adomain.getMarkers().size(); i++) {
						markerIDs.add(adomain.getMarkers().get(i).getMarkerMGIID());
						markerSymbols.add(adomain.getMarkers().get(i).getMarkerSymbol());
					}
				}
				domain.setMarkerID(String.join(",", markerIDs));
				domain.setMarkerSymbol(String.join(",", markerSymbols));				

				for (int i = 0; i < adomain.getRefAssocs().size(); i++) {
					if (adomain.getRefAssocs().get(i).getAllowOnlyOne() == 1) {
						domain.setJnumID(adomain.getRefAssocs().get(i).getJnumid());
						domain.setShortCitation(adomain.getRefAssocs().get(i).getShort_citation());
					}
				}
				
				List<String> aliases = new ArrayList<String>();
				if (adomain.getAliases() != null) {
					for (int i = 0; i < adomain.getAliases().size(); i++) {
						aliases.add(adomain.getAliases().get(i).getAlias());
					}
				}
				domain.setAliases(String.join(",", aliases));				

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
